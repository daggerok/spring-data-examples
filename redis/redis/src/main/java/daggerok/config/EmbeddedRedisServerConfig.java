package daggerok.config;
/*
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import redis.clients.jedis.Protocol;
import redis.embedded.RedisServer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Configuration
public class RedisServerConfig {

  private static final String REDIS_NAME = "devRedisServer";

  @Lazy
  @Autowired
  @Qualifier(REDIS_NAME)
  RedisServer redisServer;

  @PostConstruct
  public void init() {
    if (redisServer.isActive()) return;
    redisServer.start();
  }

  @PreDestroy
  public void down() {
    if (redisServer.isActive()) redisServer.stop();
  }

  @Bean(name = REDIS_NAME)
  public RedisServer redisServer() {

    RedisServer.builder()
               .reset();

    return RedisServer.builder()
                      .port(Protocol.DEFAULT_PORT)
                      .build();
  }
}
*/
