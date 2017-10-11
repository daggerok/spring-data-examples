package daggerok.history.service;

import daggerok.history.data.History;
import daggerok.history.data.HistoryRepository;
import daggerok.history.eventsourcing.applicationevent.CustomEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventService {

  final HistoryRepository historyRepository;

  public static CustomEvent of(final Object entity) {
    return new CustomEvent(entity);
  }

  public void onSave(final Object entity) {
    this.historyRepository.save(toHistory("update", entity));
  }

  private History toHistory(final String event, final Object entity) {

    return new History().setEvent(event)
                        .setName(event.getClass().getSimpleName());
  }
}
