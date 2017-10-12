package daggerok.config;

import daggerok.ReactiveEventDrivenDataApplication;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = ReactiveEventDrivenDataApplication.class)
public class MessagesConfig {

  @Bean
  public Queue activityQueue() {
    return new Queue("activity");
  }

  @Bean
  public Queue taskQueue() {
    return new Queue("task");
  }
}
