package daggerok.config;

import daggerok.domain.data.Domain;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Configuration
@ComponentScan(basePackageClasses = Domain.class)
public class FallbackConfig {

  @Bean
  public EmbeddedServletContainerCustomizer containerCustomizer() {
    return container -> container.addErrorPages(new ErrorPage(NOT_FOUND, "/"));
  }
}
