package daggerok.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MyEntityRepository extends JpaRepository<MyEntity, Long> {

  @Query(
      value = " select me.name from #{#entityName} me ",
      //// uncomment this to reproduce an issue:
      //countQuery = " select count(me.id) from #{#entityName} me "
      countQuery = " select count(me.id) from MyEntity me "
  )
  Page<String> findAllNames(final Pageable pageable);
}
