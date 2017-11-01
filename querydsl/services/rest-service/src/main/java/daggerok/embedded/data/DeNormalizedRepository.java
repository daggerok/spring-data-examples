package daggerok.embedded.data;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.stream.Stream;

@Repository
public interface DeNormalizedRepository
    extends JpaRepository<DeNormalized, Long>, QueryDslPredicateExecutor<DeNormalized> {

  String QUERY = " select new daggerok.embedded.flatten.DeNormalized( " +
      "                         dn.id,                                " +
      "                         dn.createdDate,                       " +
      "                         dn.modifiedAt,                        " +
      "                         dn.deNormalizedField,                 " +
      "                         dn.first.firstField1,                 " +
      "                         dn.first.firstField2,                 " +
      "                         dn.second.secondField1,               " +
      "                         dn.second.secondField2 )              " +
      "              from #{#entityName} dn                           ";

  @Query(QUERY + " where dn.id = :id ")
  Optional<daggerok.embedded.flatten.DeNormalized> findById(@Param("id") final Long id);

  @Query(QUERY)
  Stream<daggerok.embedded.flatten.DeNormalized> streamAll();

  /**
   * example:
   * val stream = deNormalizedRepository.sortedStream(new Sort(Sort.Direction.DESC, "modifiedAt"));
   * <br/>
   * or:
   * val sort = new Sort(Sort.Direction.DESC, "modifiedAt").add(
   * new Sort(Sort.Direction.ASC, "deNormalizedField"))
   * val stream = deNormalizedRepository.sortedStream(sort);
   *
   * @param sort can be passed via @RequestParam, ie: ?sort=modifiedAt,desc
   * @return sorted stream nameOf mapped daggerok.embedded.flatten.DeNormalized objects.
   */
  @Query(QUERY)
  Stream<daggerok.embedded.flatten.DeNormalized> sortedStream(final Sort sort);

  @Query(
      value = " select new daggerok.embedded.flatten.DeNormalized( " +
          "                      dn.id,                                " +
          "                      dn.createdDate,                       " +
          "                      dn.modifiedAt,                        " +
          "                      dn.deNormalizedField,                 " +
          "                      dn.first.firstField1,                 " +
          "                      dn.first.firstField2,                 " +
          "                      dn.second.secondField1,               " +
          "                      dn.second.secondField2 )              " +
          "           from #{#entityName} dn                           ",
      countQuery = " select count(dn) from DeNormalized dn         "
  )
  Page<daggerok.embedded.flatten.DeNormalized> getPage(final Pageable pageable);
}
