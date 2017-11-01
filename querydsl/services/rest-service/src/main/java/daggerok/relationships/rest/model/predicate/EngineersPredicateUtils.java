package daggerok.relationships.rest.model.predicate;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.MapPath;
import com.querydsl.core.types.dsl.SetPath;
import com.querydsl.core.types.dsl.StringPath;
import daggerok.relationships.data.QEngineer;
import daggerok.relationships.rest.model.EngineersRequest;
import lombok.NoArgsConstructor;
import lombok.val;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class EngineersPredicateUtils {

  /**
   * Predicate
   */
  public static Predicate getPredicate(final String type, final EngineersRequest filter) {
    return "any".equals(type) ? anyOfPredicate(filter) : allOfPredicate(filter);
  }

  //

  /**
   * Any predicate
   */
  private static Predicate anyOfPredicate(final EngineersRequest filter) {

    val engineer = QEngineer.engineer;
    return ExpressionUtils.anyOf(
        setAnyOfPredicate(engineer.emails, filter.getEmails())
        /*
        , mapAnyOfPredicate(engineer.labels, filter.getLabels())
        , mapAnyOfPredicate(engineer.tags, filter.getTags())
        */
    );
  }

  /**
   * All predicate
   */
  private static Predicate allOfPredicate(final EngineersRequest filter) {

    val engineer = QEngineer.engineer;
    return ExpressionUtils.allOf(
        setAllOfPredicate(engineer.emails, filter.getEmails())
        /*
        , mapAllOfPredicate(engineer.labels, filter.getLabels())
        , mapAllOfPredicate(engineer.tags, filter.getTags())
        */
    );
  }

  //

  /**
   * Set any predicate
   */
  private static Predicate setAnyOfPredicate(final SetPath<String, StringPath> path, final Set<String> set) {

    val predicates = setPredicate(path, set);

    return predicates.size() > 0
        ? ExpressionUtils.anyOf(predicates)
        : path.any()
              .isNotNull()
              .and(path.any()
                       .isNotEmpty());
  }

  /**
   * Map any predicate
   */
  private static Predicate mapAnyOfPredicate(final MapPath<String, String, StringPath> path,
                                             final Map<String, String> map) {

    val predicates = mapPredicate(path, map);

    return predicates.size() > 0
        ? ExpressionUtils.anyOf(predicates)
        : ExpressionUtils.anyOf(map.keySet()
                                   .stream()
                                   .map(key -> path.get(key)
                                                   .isNotNull())
                                   .collect(toList()));
  }

  //

  /**
   * Set all predicate
   */
  private static Predicate setAllOfPredicate(final SetPath<String, StringPath> path, final Set<String> set) {

    val predicates = setPredicate(path, set);

    return predicates.size() > 0
        ? ExpressionUtils.allOf(predicates)
        : path.any()
              .isNotNull()
              .and(path.any()
                       .isNotEmpty());
  }

  /**
   * Map all predicate
   */
  private static Predicate mapAllOfPredicate(final MapPath<String, String, StringPath> path, final Map<String, String> map) {

    val predicates = mapPredicate(path, map);

    return predicates.size() > 0
        ? ExpressionUtils.allOf(predicates)
        : ExpressionUtils.allOf(map.keySet()
                                   .stream()
                                   .map(key -> path.get(key)
                                                   .isNotNull())
                                   .collect(toList()));
  }

  //

  /**
   * Set predicate
   */
  private static Set<Predicate> setPredicate(final SetPath<String, StringPath> path, final Set<String> set) {

    return set.stream()
              .filter(Objects::nonNull)
              .map(path::contains)
              .map(Predicate.class::cast)
              .collect(toSet());
  }

  /**
   * Map predicate
   */
  private static Set<Predicate> mapPredicate(final MapPath<String, String, StringPath> path, final Map<String, String> map) {

    return map.entrySet()
              .stream()
              .map(stringsEntry -> path.contains(stringsEntry.getKey(),
                                                 stringsEntry.getValue()))
              .map(Predicate.class::cast)
              .collect(toSet());
  }
}
