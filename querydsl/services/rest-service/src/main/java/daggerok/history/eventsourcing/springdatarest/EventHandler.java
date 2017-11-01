package daggerok.history.eventsourcing.springdatarest;

import daggerok.history.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.core.event.AbstractRepositoryEventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventHandler extends AbstractRepositoryEventListener {

  final EventService eventService;

  @Override
  protected void onBeforeCreate(final Object entity) {
    eventService.onSave(entity);
  }
}
