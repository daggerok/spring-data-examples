package daggerok;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import static java.util.Collections.singletonMap;

@SpringBootApplication
public class SpringDataContactQueryApplication {

  public static void main(String[] args) {

    new SpringApplicationBuilder(SpringDataContactQueryApplication.class)
        .properties(singletonMap("server.port", 8081))
        .registerShutdownHook(true)
        .run(args);
  }
}
