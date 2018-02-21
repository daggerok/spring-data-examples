package daggerok

import com.google.gson.Gson
import io.lettuce.core.RedisClient
import io.lettuce.core.RedisURI
import org.slf4j.LoggerFactory
import reactor.core.publisher.Flux
import redis.embedded.RedisExecProvider
import redis.embedded.RedisServer
import java.time.Duration
import java.util.*
import java.util.concurrent.atomic.AtomicLong


data class MyString(val id: UUID = UUID.randomUUID(), val payload: String = "")

val gson = Gson()
fun MyString.toJson() = gson.toJson(this)
fun String.fromJson() = gson.fromJson(this, MyString::class.java)

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

            val redisServer = RedisServer.builder()
                    .port(6379)
                    .setting("maxmemory 128M")
                    .build()

//            val redisServer = RedisServer.builder().build()
            redisServer.start()

            val log = LoggerFactory.getLogger(App::class.java.name)
            log.info("connecting to redis....")
            val client = ConnectionFactory().createConnection()
            val connection = client.connectPubSub()
//            val connection = client.connect() as StatefulRedisConnection<String, String>
            val reactiveCommands = connection.reactive()
            val id = UUID.randomUUID()
            val jsonFlux = Flux.generate<String, AtomicLong>(
                    { AtomicLong() },
                    { state: AtomicLong, sink ->

                        val i = state.getAndIncrement()
                        val message = "3 x " + i + " = " + 3 * i
                        sink.next(message)
                        reactiveCommands.set(id.toString(), MyString(id, payload = "$i: $message").toJson())
                                .block()
                        reactiveCommands.get(id.toString()).subscribe({
                            log.info("{}", it.fromJson())
                        })
                        if (i > 10) sink.complete()
                        Thread.sleep(111)
                        state
                    },
                    { state -> log.info("""consuming last state: $state""") }
            )


            jsonFlux.subscribe()

            log.info("shutting down...")
            client.shutdown(Duration.ofSeconds(1), Duration.ofSeconds(1))
            redisServer.stop()
            Runtime.getRuntime().exit(0)
        }
    }
}
