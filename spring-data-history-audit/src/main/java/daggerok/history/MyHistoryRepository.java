package daggerok.history;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "my-entities-history", path = "my-entities-history")
public interface MyHistoryRepository extends JpaRepository<MyHistory, Long> {}
