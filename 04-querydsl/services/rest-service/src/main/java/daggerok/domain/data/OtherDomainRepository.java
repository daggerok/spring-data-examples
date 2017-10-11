package daggerok.domain.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface OtherDomainRepository
    extends JpaRepository<OtherDomain, Long>, QueryDslPredicateExecutor<OtherDomain> {}
