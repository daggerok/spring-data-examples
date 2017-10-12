package daggerok;

import com.fasterxml.jackson.databind.ObjectMapper;
import daggerok.data.Activity;
import daggerok.data.ActivityRepository;
import daggerok.data.Task;
import daggerok.data.TaskRepository;
import daggerok.data.HistoryRestRepository;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;

@Slf4j
@SpringBootApplication
public class ReactiveEventDrivenDataApplication {

  @Configuration
  @RequiredArgsConstructor
  public static class TestDataConfig {

    final ObjectMapper objectMapper;

    private String json(Object o) {

      return Try.of(() -> objectMapper.writerWithDefaultPrettyPrinter()
                                      .writeValueAsString(o))
                .get();
    }

    @Bean
    @Transactional
    public CommandLineRunner testData(final TaskRepository taskRepository,
                                      final ActivityRepository activityRepository,
                                      final HistoryRestRepository historyRestRepository) {

      historyRestRepository.deleteAll(historyRestRepository.findAll());
/*
      Flux.just("one", "two")
          .map(string -> singletonMap("data", string))
          .map(this::json)
          .map(s -> new History().setJson(s))
          .map(historyRestRepository::save)
          .subscribe(o -> log.info("done: {}", o));
*/
      val items = Stream.of(taskRepository.findAll(),
                            activityRepository.findAll())
                        .map(Iterable::spliterator)
                        .map(spliterator -> StreamSupport.stream(spliterator, false))
                        .flatMap(stream -> stream)
                        .map(this::json)
                        .collect(toList());

      items.forEach(this::json);

      if (items.size() > 4) {
        activityRepository.deleteAll();
        taskRepository.deleteAll();
      }

      return args -> Flux.just("one", "two", "three")
                         .map(i -> new Task().setBody(i))
                         .map(taskRepository::save)
                         .collectList()
                         .map(l -> new Activity().setTasks(l))
                         .map(activityRepository::save)
                         .subscribe();
    }
  }

  public static void main(String[] args) {
    SpringApplication.run(ReactiveEventDrivenDataApplication.class, args);
  }
}
