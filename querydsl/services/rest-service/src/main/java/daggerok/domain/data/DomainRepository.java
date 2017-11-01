package daggerok.domain.data;

import com.querydsl.core.types.Predicate;
import lombok.val;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Repository
public interface DomainRepository extends JpaRepository<Domain, Long>, QueryDslPredicateExecutor<Domain> {

  default Stream<Domain> filter(final Predicate predicate) {
    val source = findAll(predicate).spliterator();
    return StreamSupport.stream(source, false);
  }
}
