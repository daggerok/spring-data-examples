package daggerok.enumset.data;

import daggerok.enumset.data.catalog.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CatalogRepository extends JpaRepository<Catalog, Long> {
  Page<Catalog> findAllByType(@Param("type") final Type type, final Pageable pageable);
}
