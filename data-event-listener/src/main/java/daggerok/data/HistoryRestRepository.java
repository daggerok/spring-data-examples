package daggerok.data;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface HistoryRestRepository extends CrudRepository<History, Long> {}
