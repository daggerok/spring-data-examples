package daggerok.config;

import daggerok.SpringDataEmbeddedRedisApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Protocol;
import redis.embedded.RedisServer;

@Configuration
@ComponentScan(basePackageClasses = SpringDataEmbeddedRedisApplication.class)
public class EmbeddedRedisServerConfig {

  @Bean(name = "embedded")
  public RedisServer redisServer() {
///*
    RedisServer.builder()
               .reset();
//*/
    return RedisServer.builder()
                      .port(Protocol.DEFAULT_PORT)
                      .build();
  }
}
