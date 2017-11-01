package daggerok.keyvalue.user;

import org.springframework.data.keyvalue.repository.KeyValueRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Repository
public interface UserRepository extends
    KeyValueRepository<User, String>,
    PagingAndSortingRepository<User, String> {

  default Stream<User> findAny() {
    return StreamSupport.stream(findAll().spliterator(), true);
  }
}
