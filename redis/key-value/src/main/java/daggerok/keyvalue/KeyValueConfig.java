package daggerok.keyvalue;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.keyvalue.core.KeyValueAdapter;
import org.springframework.data.keyvalue.core.KeyValueOperations;
import org.springframework.data.keyvalue.core.KeyValueTemplate;
import org.springframework.data.map.MapKeyValueAdapter;
import org.springframework.data.map.repository.config.EnableMapRepositories;

import java.util.concurrent.ConcurrentHashMap;

@Configuration
@EnableMapRepositories
public class KeyValueConfig {

  @Bean
  public KeyValueOperations keyValueTemplate() {
    return new KeyValueTemplate(keyValueAdapter());
  }

  @Bean
  public KeyValueAdapter keyValueAdapter() {
    return new MapKeyValueAdapter(ConcurrentHashMap.class);
  }
}
