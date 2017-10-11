package daggerok.history.eventsourcing.applicationevent;

import daggerok.history.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomEventHandler {

  final EventService eventService;

  @EventListener
  public void onCustomEvent(final CustomEvent event) {
    eventService.onSave(event.getSource());
  }
}
