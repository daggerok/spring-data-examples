package daggerok;

import com.google.common.collect.Maps;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import redis.embedded.RedisServer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Collections;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

@Slf4j
@SpringBootApplication
public class SpringDataEmbeddedRedisApplication {

  @Autowired
  @Qualifier("embedded")
  RedisServer redisServer;

  @SneakyThrows
  @PostConstruct
  private void start() {

    if (System.getProperty("os.name").toLowerCase().contains("windows")) {
      log.error("sorry, but this embedded server doesn't work on windows...");
      System.exit(0);
    }

    log.info("starting redis...");
    if (nonNull(redisServer) && !redisServer.isActive()) redisServer.start();
    log.info(String.format("redis listen ports: %s", redisServer.ports()
                                                                .stream()
                                                                .map(port -> port.toString())
                                                                .collect(Collectors.joining(","))));
  }

  @PreDestroy
  public void stop() {
    if (nonNull(redisServer) && redisServer.isActive()) redisServer.stop();
  }

  public static void main(String[] args) {

    val properties = Maps.newHashMap();
    properties.put("server.port", 8082);
    properties.put("spring.redis.port", 0);

    new SpringApplicationBuilder(SpringDataEmbeddedRedisApplication.class)
        .properties(String.valueOf(properties))
        .registerShutdownHook(true)
        .run(args);
  }
}
