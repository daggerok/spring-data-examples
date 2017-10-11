package daggerok.domain;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Stream;

@Slf4j
@Configuration
@Transactional(readOnly = true)
public class MyEntityTestdata {

  @Bean
  @Transactional
  CommandLineRunner testdata(final MyEntityRepository repo) {

    Stream.of("one", "two", "three")
          .map(value -> new MyEntity().setName(value))
          .forEach(repo::save);

    return args -> log.info("\ntestdata in db:\n{}", repo.findAll());
  }
}
