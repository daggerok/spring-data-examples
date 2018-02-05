package daggerok;

import io.vavr.collection.HashMap;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import org.infinispan.spring.provider.SpringEmbeddedCacheManagerFactoryBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static java.util.Collections.singletonMap;
import static org.springframework.http.HttpStatus.CREATED;

@SpringBootApplication
public class InfinispanServiceApplication {

  @Configuration
  static class ApplicationConfiguration {

    @Bean
    SpringEmbeddedCacheManagerFactoryBean springCache() {
      return new SpringEmbeddedCacheManagerFactoryBean();
    }
  }

  @Service
  @RequiredArgsConstructor
  static class CachedService {

    final CacheManager cacheManager;

    public void add(final String key, final String value) {

      cacheManager.getCache("messages")
                  .put(key, value);
    }

    public String getMessage(final String key) {

      return String.class.cast(cacheManager.getCache("messages")
                                           .get(key)
                                           .get());
    }
  }

  @RestController
  @RequiredArgsConstructor
  static class CacheResource {

    final CachedService cachedService;

    @PostMapping
    @ResponseStatus(CREATED)
    public ResponseEntity save(final HttpServletRequest req,
                               final @RequestBody Map<String, String> request) {

      final String id = UUID.randomUUID().toString();

      cachedService.add(id, request.getOrDefault("message", "empty"));

      return ResponseEntity.created(UriComponentsBuilder.fromHttpUrl(getBase(req))
                                                        .path(id)
                                                        .build()
                                                        .toUri())
                           .build();
    }

    @GetMapping("/{id}")
    public Map<String, String> get(final @PathVariable String id,
                                   final HttpServletRequest request) {

      return HashMap.of("message", Try.of(() -> cachedService.getMessage(id))
                                      .getOrElseGet(throwable -> null),
                        "_self", getBase(request) + "/" + id)
                    .toJavaMap();
    }

    @GetMapping({ "", "/**" })
    public List<Map<String, Object>> index(final HttpServletRequest request) {

      return Arrays.asList(

          singletonMap("message GET",
                       UriComponentsBuilder.fromHttpUrl(getBase(request))
                                           .path("$id")
                                           .build()
                                           .toUri()),
          singletonMap("message POST",
                       UriComponentsBuilder.fromHttpUrl(getBase(request))
                                           .build()
                                           .toUri()),
          singletonMap("_self GET",
                       UriComponentsBuilder.fromHttpUrl(getBase(request))
                                           .build()
                                           .toUri())
      );
    }

    static String getBase(final HttpServletRequest request) {
      final String url = request.getRequestURL().toString();
      final int lastIndex = url.length() - request.getRequestURI().length();
      return url.substring(0, lastIndex);
    }
  }

  public static void main(String[] args) {
    SpringApplication.run(InfinispanServiceApplication.class, args);
  }
}
