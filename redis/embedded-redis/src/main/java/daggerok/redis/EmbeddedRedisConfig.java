package daggerok.redis;

import daggerok.SpringDataEmbeddedRedisApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@Configuration
@ComponentScan(basePackageClasses = SpringDataEmbeddedRedisApplication.class)
public class EmbeddedRedisConfig {

  @Bean
  public JedisConnectionFactory jedisConnectionFactory() {
    return new JedisConnectionFactory();
  }
}
