package daggerok.keyvalue.keyvalueobj;

import daggerok.keyvalue.user.User;
import org.springframework.data.keyvalue.repository.KeyValueRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.stream.Stream;

@Repository
public interface KeyValueObjRepository extends
    KeyValueRepository<KeyValueObj, String>,
    PagingAndSortingRepository<KeyValueObj, String> {

  Stream<User> findAllById(@Param("id") String id);
}
