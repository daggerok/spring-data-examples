package daggerok.history;

import daggerok.myentity.MyEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.PreUpdate;

@Slf4j
@Service
public class MyHistoryListener {

  /**
   * for some reasons, it working only with static fields... o.O
   */
  private static MyHistoryService myHistoryService;

  @Autowired
  public void init(final MyHistoryService service) {
    MyHistoryListener.myHistoryService = service;
  }

  @PreUpdate
  public void handleUpdate(final Object o) {

    log.info("pre update: {}", o);
    MyHistoryListener.myHistoryService.createHistory(MyEntity.class.cast(o));
  }
}
