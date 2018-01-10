package daggerok.myentity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource(collectionResourceRel = "my-entities", path = "my-entities")
public interface MyEntityRepository extends JpaRepository<MyEntity, Long> {

  Optional<MyEntity> findFirstByIdOrderByModifiedAtDesc(@Param("id") final Long id);
}
