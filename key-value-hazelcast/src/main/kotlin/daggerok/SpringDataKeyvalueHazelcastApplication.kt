package daggerok

import com.hazelcast.core.Hazelcast
import com.hazelcast.core.HazelcastInstance
import com.hazelcast.core.IMap
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.data.annotation.Id
import org.springframework.data.keyvalue.annotation.KeySpace
import org.springframework.http.MediaType
import org.springframework.stereotype.Repository
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.body
import org.springframework.web.reactive.function.server.router
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.io.Serializable
import java.net.URI
import java.util.*

@KeySpace("user")
data class User(@Id var id: String? = null,
                var name: String? = null,
                var username: String? = null) : Serializable

fun User.of(name: String) = User(
    this.id ?: UUID.randomUUID().toString(),
    name.capitalize(),
    name.toLowerCase()
)

@Repository
class NaiveUserRepository(val hazelcastInstance: HazelcastInstance,
                          val repo: IMap<String, User> = hazelcastInstance.getMap("naiveUserKeySpace")) {

  fun save(input: String): User {
    val user = User().of(input)
    return save(user)
  }

  fun save(user: User): User {
    if (user.id == null) user.id = UUID.randomUUID().toString()
    repo.put(user.id, user)
    return user
  }

  fun findAll(): Iterable<User> = repo.values

  fun findOne(id: String) = repo[id]
}

//@Repository
//interface UserRepository : HazelcastRepository<User, String> {
//
//  @Query("name=Max")
//  fun usersWiththeirNameIsMax(): List<User>
//
//  @Query("name=%s")
//  fun usersWiththeirName(name: String): List<User>
//
//  @Query("name=%s or username=%s")
//  fun usersWithNameOrUsername(name: String, username: String): List<User>
//}

@SpringBootApplication
//@EnableHazelcastRepositories(basePackageClasses = arrayOf(UserRepository::class))
class SpringDataKeyvalueHazelcastApplication {

  @Bean
  fun hazelcastInstance() = Hazelcast.newHazelcastInstance()

//  @Bean
//  fun hazelcastKeyValueAdapter() = HazelcastKeyValueAdapter(hazelcastInstance())
//
//  @Bean
//  fun keyValueTemplate(hazelcastKeyValueAdapter: HazelcastKeyValueAdapter) =
//      KeyValueTemplate(HazelcastKeyValueAdapter(hazelcastInstance()))
//
////  @Bean
////  fun keyValueTemplate(hazelcastKeyValueAdapter: HazelcastKeyValueAdapter) = KeyValueTemplate(hazelcastKeyValueAdapter)
//
////  @Bean
////  fun initializer(repo: UserRepository) = ApplicationRunner {
////    arrayOf("max", "dag", "daggerok")
////        .map { User().of(it) }
////        .forEach { repo.save(it) }
////  }

  @Bean
  fun routes(repo: NaiveUserRepository) = router {

    fun postData(req: ServerRequest) =
        req.bodyToMono(User::class.java)
            .map { repo.save(it) }
            .map { it?.id }
            .flatMap {
              ServerResponse.accepted()
                  .location(URI.create("/$it"))
                  .build()
            }

    fun postOne(req: ServerRequest): Mono<ServerResponse> {
      val name = req.pathVariable("id")
      val user = repo.save(name)
      return ServerResponse.accepted()
          .location(URI.create("/${user.id}"))
          .build()
    }

    fun getData(@Suppress("UNUSED_PARAMETER") req: ServerRequest) =
        ServerResponse.ok().body(Flux.fromIterable(repo.findAll()))

    fun getOne(req: ServerRequest): Mono<ServerResponse> {
      val id = req.pathVariable("id")
      val user = repo.findOne(id)
      return if (null == user) ServerResponse.notFound().build()
      else ServerResponse.ok().body(Mono.just(user))
    }

    (accept(MediaType.APPLICATION_JSON_UTF8)).nest {
      ("/{id}").nest {
        POST("", ::postOne)
        GET("", ::getOne)
      }

      ("/").nest {
        GET("**", ::getData)
        POST("**", ::postData)
      }
    }
  }
}

fun main(args: Array<String>) {
  SpringApplication.run(SpringDataKeyvalueHazelcastApplication::class.java, *args)
}
