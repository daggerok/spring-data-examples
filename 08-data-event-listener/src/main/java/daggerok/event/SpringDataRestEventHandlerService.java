package daggerok.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import daggerok.data.Activity;
import daggerok.data.History;
import daggerok.data.Task;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitMessageOperations;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleAfterDelete;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Service;

import static java.util.Collections.singletonMap;

@Service
@RepositoryEventHandler({
    Activity.class,
    Task.class,
})
@RequiredArgsConstructor
public class SpringDataRestEventHandlerService {

  final ObjectMapper objectMapper;
  final RabbitMessageOperations rabbitMessageOperations;

  @HandleAfterCreate
  public void onAfterCreateActivity(final Activity activity) {
    rabbitMessageOperations.convertAndSend("activity", to(HandleAfterCreate.class, activity));
  }

  @HandleAfterCreate
  public void onAfterCreateTask(final Task task) {
    rabbitMessageOperations.convertAndSend("task", to(HandleAfterCreate.class, task));
  }

  @HandleAfterDelete
  public void onAfterDeleteActivity(final Activity activity) {
    rabbitMessageOperations.convertAndSend("activity", to(HandleAfterDelete.class, activity));
  }

  @HandleAfterDelete
  public void onAfterDeleteTask(final Task task) {
    rabbitMessageOperations.convertAndSend("task", to(HandleAfterDelete.class, task));
  }

  @HandleAfterSave
  public void onAfterSaveActivity(final Activity activity) {
    rabbitMessageOperations.convertAndSend("activity", to(HandleAfterSave.class, activity));
  }

  @HandleAfterSave
  public void onAfterSaveTask(final Task task) {
    rabbitMessageOperations.convertAndSend("task", to(HandleAfterSave.class, task));
  }

  private History to(final Class annotation, final Object obj) {

    return new History().setJson(json(singletonMap(
        annotation.getSimpleName() + " " + obj.getClass().getSimpleName(), obj)));
  }

  private String json(Object o) {

    return Try.of(() -> objectMapper.writerWithDefaultPrettyPrinter()
                                    .writeValueAsString(o))
              .get();
  }
}
