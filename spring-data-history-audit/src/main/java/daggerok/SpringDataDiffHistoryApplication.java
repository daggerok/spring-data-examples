package daggerok;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.val;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.TimeZone;

@EnableJpaAuditing
@SpringBootApplication
@ComponentScan(basePackageClasses = {
    Jsr310JpaConverters.class,
    SpringDataDiffHistoryApplication.class
})
public class SpringDataDiffHistoryApplication {

  public static void main(String[] args) {

    TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    SpringApplication.run(SpringDataDiffHistoryApplication.class, args);
  }

  @Bean
  public ObjectMapper objectMapper() {
    val objectMapper = new ObjectMapper();
    objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
    return objectMapper;
  }
}
