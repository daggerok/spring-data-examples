package daggerok

import com.google.gson.Gson
import daggerok.MyString.Companion.gson
import io.lettuce.core.RedisClient
import io.lettuce.core.RedisURI
import org.slf4j.LoggerFactory
import reactor.core.publisher.Flux
import redis.embedded.RedisExecProvider
import redis.embedded.RedisServer
import java.time.Duration
import java.util.*
import java.util.concurrent.atomic.AtomicLong

val log = LoggerFactory.getLogger(App::class.java.name)

data class MyString(val id: UUID = UUID.randomUUID(), val payload: String = "") {
  companion object {
    val gson = Gson()
  }
}

fun MyString.serialize() = gson.toJson(this)
fun String.deserialize() = gson.fromJson(this, MyString::class.java)

class ConnectionFactory {

  fun createConnection(port: Int = 6379, host: String = "127.0.0.1"): RedisClient =
      RedisClient.create(RedisURI.builder()
          .withTimeout(Duration.ofMinutes(1))
          .withHost(host)
          .withPort(port)
          .build())
}

class App {

  companion object {

    @JvmStatic
    fun main(args: Array<String>) {

      println(RedisExecProvider.defaultProvider().get())

      //val redisServer = RedisServer.builder().build()
      val redisServer = RedisServer.builder()
          .setting("maxmemory 128M")
          .port(6379)
          .build()

      redisServer.start()
      log.info("connecting to redis....")

      val client = ConnectionFactory().createConnection()
      //val connection = client.connect() as StatefulRedisConnection<String, String>
      val connection = client.connectPubSub()
      val reactiveCommands = connection.reactive()
      val id = UUID.randomUUID()

      Flux.generate<String, AtomicLong>(

          { AtomicLong() },

          { state: AtomicLong, sink ->

            val i = state.getAndIncrement()
            val message = "3 x " + i + " = " + 3 * i
            sink.next(message)
            reactiveCommands.set(id.toString(), MyString(id, payload = "$i: $message").serialize()).block()
            reactiveCommands.get(id.toString()).subscribe({
              log.info("{}", it.deserialize())
            })
            if (i > 10) sink.complete()
            Thread.sleep(111)
            state
          },

          { state -> log.info("consuming last state: $state") }

      ).subscribe()

      log.info("shutting down...")
      client.shutdown(Duration.ofSeconds(1), Duration.ofSeconds(1))

      println("server is active: ${redisServer.isActive}")
      if (redisServer.isActive) redisServer.stop()
      println("server is active: ${redisServer.isActive}")

      Runtime.getRuntime().exit(0)
    }
  }
}
