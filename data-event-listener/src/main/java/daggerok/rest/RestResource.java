package daggerok.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import daggerok.data.Activity;
import daggerok.data.ActivityRepository;
import daggerok.data.Task;
import daggerok.data.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static java.util.Collections.singletonMap;
import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.transaction.annotation.Propagation.REQUIRES_NEW;

@RestController
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RestResource {

  final ObjectMapper objectMapper;
  final TaskRepository taskRepository;
  final ActivityRepository activityRepository;

  @GetMapping("/all")
  public Flux getAll() {
    return Flux.concat(
        Flux.fromIterable(activityRepository.findAll()),
        Flux.fromIterable(taskRepository.findAll())
    );
  }

  @GetMapping("/all/{id}")
  public Flux get(@PathVariable("id") final Long id) {
    return Flux.concat(
        Mono.justOrEmpty(activityRepository.findById(id)),
        Mono.justOrEmpty(taskRepository.findById(id))
    );
  }

  @Transactional
  @PostMapping("/tasks")
  @ResponseStatus(ACCEPTED)
  public void saveTasks(@RequestBody final Map<String, Task> map) {

    Optional.ofNullable(map.get("task"))
            .ifPresent(taskRepository::save);
  }

  @Transactional
  @ResponseStatus(ACCEPTED)
  @PostMapping("/activities")
  public void save(@RequestBody final Map<String, Activity> map) {

    Optional.ofNullable(map.get("activity")).ifPresent(activity -> {
      Optional.ofNullable(activity.getTasks())
              .ifPresent(taskRepository::saveAll);
      saveAndFlush(activity);
    });
  }

  @Transactional
  @DeleteMapping("/all/{id}")
  @ResponseStatus(ACCEPTED)
  public void deleteAll(@PathVariable("id") final Long id) {

    activityRepository.findById(id)
                      .ifPresent(activity -> activityRepository.deleteById(activity.getId()));

    taskRepository.findById(id).ifPresent(
        task -> activityRepository.findAll().forEach(activity -> {
          final List<Task> tasks = activity.getTasks();
          if (tasks.contains(task))
            saveAndFlush(activity.setTasks(StreamSupport.stream(tasks.spliterator(), true)
                                                        .filter(t -> !task.equals(t))
                                                        .collect(toList())));
        }));
  }

  @Transactional(propagation = REQUIRES_NEW)
  public void saveAndFlush(final Activity activity) {
    activityRepository.saveAndFlush(activity);
  }

  @Transactional
  @DeleteMapping("/all")
  @ResponseStatus(ACCEPTED)
  public void deleteAll() {
    activityRepository.deleteAll();
    taskRepository.deleteAll();
  }

  @GetMapping({ "", "/"})
  public Flux<Map> other() {
    return Flux.just(
        singletonMap("GET", "http://localhost:8080/"),
        singletonMap("GET", "http://localhost:8080/{id}"),
        singletonMap("POST", "http://localhost:8080/"),
        singletonMap("DELETE", "http://localhost:8080/{id}"),
        singletonMap("DELETE", "http://localhost:8080/")
    );
  }
}
