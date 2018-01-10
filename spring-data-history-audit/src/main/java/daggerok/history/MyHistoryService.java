package daggerok.history;

import daggerok.myentity.MyEntity;
import daggerok.myentity.MyEntityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;

@Slf4j
@Service
@RequiredArgsConstructor
public class MyHistoryService {

  final MyEntityRepository myEntityRepository;
  final MyHistoryRepository myHistoryRepository;

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void createHistory(@NotNull final MyEntity curr) {

    log.info("processing history for: {}", curr);
    // not necessary for this case (id), but useful for any other kind of non unique (non PK) search criteria
    myEntityRepository.findFirstByIdOrderByModifiedAtDesc(curr.getId())
                      .ifPresent(prev -> myHistoryRepository.save(new MyHistory().setEntityId(prev.getId())
                                                                                 .setOldValue(prev.getValue())
                                                                                 .setNewValue(curr.getValue())));
  }
}
