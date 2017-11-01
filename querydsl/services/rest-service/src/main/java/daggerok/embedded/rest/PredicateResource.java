package daggerok.embedded.rest;

import com.querydsl.core.types.Predicate;
import daggerok.embedded.data.DeNormalized;
import daggerok.embedded.data.DeNormalizedRepository;
import lombok.Cleanup;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.StreamSupport;

import static daggerok.utils.Json.stringify;
import static java.util.stream.Collectors.toList;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PredicateResource {

  final DeNormalizedRepository deNormalizedRepository;

  @GetMapping("/api/v3/predicate")
  public List<DeNormalized> getByPredicate(@QuerydslPredicate(root = DeNormalized.class) final Predicate predicate,
                                           @RequestParam final MultiValueMap<String, String> parameters) {

    val uri = ServletUriComponentsBuilder.fromCurrentRequest()
                                         .build();

    log.info("\nuri: {}\nparameters: {}\npredicate: {}", uri, parameters, predicate);

    val data = deNormalizedRepository.findAll(predicate);
    @Cleanup val stream = StreamSupport.stream(data.spliterator(), true);

    return stream.map(this::processor)
                 .collect(toList());
  }

  /**
   * 1. Parse object to Json string.
   * 2. Simulate latency. Timeout: 1 second.
   * 3. Print intermediate result.
   *
   * @param object to be printed
   * @param <T>    object type
   * @return same object
   */
  private <T extends Object> T processor(final T object) {

    val jsonString = stringify(object);
    print(jsonString);
    delay(1);
    return object;
  }

  private void print(final String jsonString) {
    log.info("data\n{}", jsonString);
  }

  @SneakyThrows
  private void delay(final int seconds) {
    TimeUnit.SECONDS.sleep(seconds);
  }
}
