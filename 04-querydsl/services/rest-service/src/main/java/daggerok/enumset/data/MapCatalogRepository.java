package daggerok.enumset.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface MapCatalogRepository extends JpaRepository<MapCatalog, Long>, QueryDslPredicateExecutor<MapCatalog> {}
