package daggerok.relationships.data;

import daggerok.history.service.EventService;
import lombok.val;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface EngineerRepository extends JpaRepository<Engineer, Long>, QueryDslPredicateExecutor<Engineer> {

  Page<Engineer> findAllByOrderByModifiedAtDesc(final Pageable pageable);

  @Transactional
  default <T extends Engineer> T auditableSave(T engineer) {
    val result = save(engineer);
    EventService.of(result);
    return result;
  }
}
