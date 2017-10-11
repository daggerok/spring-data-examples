package daggerok.config;

import daggerok.data.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class TestConfig {

  @Bean
  CommandLineRunner test(final UserRepository repo) {

    return args -> repo.findAll()
                       .forEach(e -> log.info("{}", e));
  }
}
