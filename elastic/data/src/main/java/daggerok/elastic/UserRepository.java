package daggerok.elastic;

import org.elasticsearch.common.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.stream.Stream;

@Repository
public interface UserRepository extends ElasticsearchRepository<User, String> {

  Page<User> findAllByNameContainingIgnoreCase(final String name, final Pageable pageable);

  long countAllByNameContainingIgnoreCase(final String name);

  Page<User> findAllByUsernameContainingIgnoreCase(final String username, final Pageable pageable);

  long countAllByUsernameContainingIgnoreCase(final String username);

  @Query("{                                     " +
      "     \"bool\" : {                        " +
      "       \"should\" : {                    " +
      "         \"field\" : {                   " +
      "           \"name\" : {                  " +
      "             \"query\" : \"*?0\",        " +
      "             \"analyze_wildcard\" : true " +
      "           },                            " +
      "           \"username\" : {              " +
      "             \"query\" : \"*?1\",        " +
      "             \"analyze_wildcard\" : true " +
      "           }                             " +
      "         }                               " +
      "       }                                 " +
      "     }                                   " +
      "   }                                     ")
  @Nullable
  Stream<User> streamPagedUsers(@Nullable final Stream name,
                                @Nullable final Stream username,
                                final Pageable pageable);
}
