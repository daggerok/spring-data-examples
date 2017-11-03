package daggerok.elastic;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.repositories.Repository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Slf4j
@Configuration
@EnableElasticsearchRepositories(basePackageClasses = {
    Repository.class,
    UserRepository.class,
})
public class Config {

  @Bean
  ApplicationRunner runner(final ElasticsearchTemplate elasticsearchTemplate) {
    return args -> {

      log.info("delete index: {}", elasticsearchTemplate.deleteIndex(User.class));
      log.info("create index: {}", elasticsearchTemplate.createIndex(User.class));

      log.info("put mapping: {}", elasticsearchTemplate.putMapping(User.class));
      log.info("get mapping: {}", elasticsearchTemplate.getMapping(User.class));

      elasticsearchTemplate.refresh(User.class);

//      log.info("has type: {}", elasticsearchTemplate.typeExists("user-index", "user"));
      log.info("settings: {}", elasticsearchTemplate.getSetting(User.class));
    };
  }
}
