package daggerok;

import lombok.*;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.List;
import java.util.Optional;

import static javax.persistence.GenerationType.IDENTITY;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
class User {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  Long id;

  @NotBlank
  String email;

  String field1, field2, field3, field4, field5, field6;

  public static User of(final String input) {
    return new User(null, input, input, input, input, input, input, input);
  }
}

interface UserRepository extends JpaRepository<User, Long> {
  // don't use that crazy long method! use getByEmail instead
  User findFirstByEmailContainsIgnoreCaseAndField1NotNullAndField2NotNullAndField3NotNullAndField4NotNullAndField5NotNullAndField6NotNull(final String email);

  default User getByEmail(final String email) {
    return findFirstByEmailContainsIgnoreCaseAndField1NotNullAndField2NotNullAndField3NotNullAndField4NotNullAndField5NotNullAndField6NotNull(email);
  }
}

@SpringBootApplication
public class ShadovApplication {

  @RestController
  @RequiredArgsConstructor
  @Transactional(readOnly = true)
  public static class Resource {

    final UserRepository users;

    @PostConstruct
    @Transactional
    void init() {
      val user = User.of("default@email.com");
      users.save(user);
    }

    @PostMapping
    @Transactional
    User post(@RequestBody @Validated final User user) {
      return users.save(user);
    }

    @GetMapping("/{in}")
    public User get(@PathVariable(required = false) final Optional<String> in) {
      val input = in.orElse("default");
      return users.getByEmail(input);
    }

    @GetMapping
    public List<User> getAll() {
      return users.findAll();
    }
  }

  public static void main(String[] args) {
    SpringApplication.run(ShadovApplication.class, args);
  }
}
