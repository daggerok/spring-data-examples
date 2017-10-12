package daggerok.rest;

import daggerok.data.Activity;
import daggerok.data.ActivityRepository;
import daggerok.data.Task;
import daggerok.data.TaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

import static java.util.Collections.singletonMap;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Slf4j
@Configuration
public class RoutesConfig {

  @Bean
  public RouterFunction<ServerResponse> routes(final TaskRepository taskRepository,
                                               final ActivityRepository activityRepository) {

    return

        route(

            DELETE("/"),
            request -> ok().body(
                Mono.fromCallable(() -> {
                  activityRepository.deleteAll();
                  taskRepository.deleteAll();
                  return "done.";
                }), String.class))

        .andRoute(

            GET("/tasks"),
            request -> ok().body(Flux.fromIterable(taskRepository.findAll()), Task.class))

        .andRoute(

            GET("/activities"),
            request -> ok().body(Flux.fromIterable(activityRepository.findAll()), Activity.class))

        .andRoute(

            POST("/tasks"),
            request -> ok().body(
                Mono.fromRunnable(() -> {
                  request.bodyToFlux(Map.class)
                         .map(map -> {
                           log.info("posting {}", map.getOrDefault("key", "default tasks value"));
                           activityRepository.deleteAll();
                           taskRepository.deleteAll();
                           return null;
                         }).subscribe(o -> log.info("tasks subscription: {}", o));
                }), String.class))

        .andRoute(

            POST("/activities"),
            request -> ok().body(
                Mono.fromRunnable(() -> {
                  request.bodyToFlux(Map.class)
                         .map(map -> {
                           log.info("posting {}", map.getOrDefault("key", "default activities value"));
                           activityRepository.deleteAll();
                           taskRepository.deleteAll();
                           return null;
                         }).subscribe(o -> log.info("activities subscription: {}", o));
                }), String.class))


        .andRoute(

            GET("/schema"),
            request -> ok().body(Flux.just(
                singletonMap("delete", "http://localhost:8080/"),
                singletonMap("get", "http://localhost:8080/tasks"),
                singletonMap("get", "http://localhost:8080/activities"),
                singletonMap("post", "http://localhost:8080/tasks"),
                singletonMap("post", "http://localhost:8080/activities")
            ), Map.class))
        ;
  }
}
