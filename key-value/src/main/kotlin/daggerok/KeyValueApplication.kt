package daggerok

import org.springframework.boot.ApplicationRunner
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.data.annotation.Id
import org.springframework.data.keyvalue.annotation.KeySpace
import org.springframework.data.map.repository.config.EnableMapRepositories
import org.springframework.data.repository.CrudRepository
import org.springframework.http.MediaType
import org.springframework.stereotype.Repository
import org.springframework.web.reactive.function.server.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.stream.Stream

@KeySpace("user")
data class User(@Id var id: String? = null,
                var name: String? = null,
                var username: String? = null)
@Repository
interface UserRepository : CrudRepository<User, String>

@SpringBootApplication
@EnableMapRepositories(basePackageClasses = arrayOf(UserRepository::class))
class KeyValueApplication(val repo: UserRepository) {

  @Bean
  fun initializer() = ApplicationRunner {
    Stream.of("max", "dag", "daggerok")
          .map { User(null, it.capitalize(), it) }
          .forEach { repo.save(it) }
  }

  @Bean
  fun routes() = router {

    fun getData(req: ServerRequest): Mono<ServerResponse> =
        ServerResponse.ok().body(Flux.fromIterable(repo.findAll()))

    (accept(MediaType.APPLICATION_JSON_UTF8) and "/").nest {
      GET("/**", ::getData)
    }
  }
}

fun main(args: Array<String>) {
  SpringApplication.run(KeyValueApplication::class.java, *args)
}
