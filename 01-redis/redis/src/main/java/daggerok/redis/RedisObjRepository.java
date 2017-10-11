package daggerok.redis;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.stream.Stream;

/*

Keyword   | Sample                                                    | Redis snippet
----------|-----------------------------------------------------------|-------------------------------------------
And       | findByLastnameAndFirstname                                | SINTER …:firstname:rand …:lastname:al’thor
Or        | findByLastnameOrFirstname                                 | SUNION …:firstname:rand …:lastname:al’thor
Is,Equals | findByFirstname, findByFirstnameIs, findByFirstnameEquals | SINTER …:firstname:rand
Top,First | findFirst10ByFirstname, findTop5ByFirstname

redisTemplate:
- http://docs.spring.io/spring-data/data-keyvalue/docs/current/reference/html/#redis:template

redisCommands:
- https://redis.io/commands

 */

@CrossOrigin
@RepositoryRestResource
public interface RedisObjRepository extends PagingAndSortingRepository<RedisObj, Long> {

  /*
   * "LIKE (1): [IsLike, Like]is not supported for redis query derivation"
   */
  Stream<RedisObj> findAllByDataIsLike(@Param("data") final String data);

  /*
   * CONTAINING (1): [IsContaining, Containing, Contains]is not supported for redis query derivation
   */
  Stream<RedisObj> findAllByDataContaining(@Param("data") final String data);
}
