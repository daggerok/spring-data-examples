package daggerok.event;

import daggerok.data.History;
import daggerok.data.HistoryRestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SpringDataRestEventListenerService {

  final HistoryRestRepository repository;

  @RabbitListener(queues = {
      "activity", "task"
  })
  public void handleActivity(final History history) {
    log.info("handle: {}", history);
    repository.save(history);
  }
}
