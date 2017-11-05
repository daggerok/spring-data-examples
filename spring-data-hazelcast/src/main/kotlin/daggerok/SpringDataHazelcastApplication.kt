package daggerok

import com.hazelcast.core.Hazelcast
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.annotation.Id
import org.springframework.data.hazelcast.HazelcastKeyValueAdapter
import org.springframework.data.hazelcast.repository.config.EnableHazelcastRepositories
import org.springframework.data.keyvalue.annotation.KeySpace
import org.springframework.data.keyvalue.core.KeyValueOperations
import org.springframework.data.keyvalue.core.KeyValueTemplate
import org.springframework.data.keyvalue.repository.KeyValueRepository
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import java.io.Serializable
import java.net.URI

/*
   http://docs.hazelcast.org/docs/latest-development/manual/html/Distributed_Query/How_Distributed_Query_Works/Querying_with_SQL.html

   valid:

    1) SIMPLE_PROPERTY("Is", "Equals")
    2)
       TRUE(0, "IsTrue", "True")
       FALSE(0, "IsFalse", "False")
    3)
       LESS_THAN("IsLessThan", "LessThan")
       LESS_THAN_EQUAL("IsLessThanEqual", "LessThanEqual")
       GREATER_THAN("IsGreaterThan","GreaterThan")
       GREATER_THAN_EQUAL("IsGreaterThanEqual", "GreaterThanEqual")
    4)
       LIKE("IsLike", "Like")
    5)
       IS_NOT_NULL(0, "IsNotNull", "NotNull")
       IS_NULL(0, "IsNull", "Null")

   invalid:

    AFTER:
    BEFORE:
    BETWEEN:
    CONTAINING:
    ENDING_WITH:
    EXISTS:
    IN:
    NEAR:
    NEGATING_SIMPLE_PROPERTY:
    NOT_CONTAINING:
    NOT_IN:
    NOT_LIKE:
    REGEX:
    STARTING_WITH:
    WITHIN:
 */

@KeySpace("user")
data class User(@Id var id: String? = null,
                var name: String? = null,
                var username: String? = null) : Serializable

fun User.of(name: String) = User(
    null,
    name.capitalize(),
    name.toLowerCase()
)

interface UserRepository : KeyValueRepository<User, String> {

  fun findFirstByName(name: String): User?

  fun findAllByNameLikeOrUsernameLikeOrIdLike(name: String, username: String, id: String): List<User>?
}

fun UserRepository.getAny(name: String): List<User>? {
  val query = "%$name%"
  return this.findAllByNameLikeOrUsernameLikeOrIdLike(query, query, query)
}

@ComponentScan
@SpringBootApplication
@EnableHazelcastRepositories(basePackageClasses = arrayOf(UserRepository::class))
class SpringDataHazelcastApplication {

  @Bean
  fun hazelcastInstance() = Hazelcast.newHazelcastInstance()

  @Bean
  fun keyValueTemplate(): KeyValueOperations =
      KeyValueTemplate(HazelcastKeyValueAdapter(hazelcastInstance()))

  @Bean
  fun hazelcastKeyValueAdapter() =
      HazelcastKeyValueAdapter(hazelcastInstance())

  @Bean
  @Transactional
  fun init(users: UserRepository) = ApplicationRunner {
    arrayOf("max", "dag", "daggi", "daggerok")
        .map { User().of(it) }
        .forEach { users.save(it) }
  }
}

@RestController
@Transactional(readOnly = true)
class UserResource(val repo: UserRepository) {

  @PostMapping
  @Transactional
  fun post(@RequestBody user: User): ResponseEntity<User> {
    val id = repo.save(user).id
    return ResponseEntity.created(URI.create("/id/$id"))
                         .build()
  }

  @GetMapping("/id/{id}")
  fun getById(@PathVariable("id") id: String) = repo.findOne(id)

  @GetMapping("/name/{name}")
  fun getName(@PathVariable("name") name: String): ResponseEntity<User> {
    val user = repo.findFirstByName(name)
    return if (null == user) ResponseEntity.notFound().build()
    else ResponseEntity.ok(user)
  }

  @GetMapping("/max")
  fun getMax() = repo.findFirstByName("Max")

  @GetMapping("/any/{name}")
  fun getAny(@PathVariable("name") name: String) =
      repo.getAny(name)

  @GetMapping
  fun get() = repo.findAll()
}

fun main(args: Array<String>) {
  System.setProperty("hazelcast.logging.type", "slf4j")
  SpringApplication.run(SpringDataHazelcastApplication::class.java, *args)
}
