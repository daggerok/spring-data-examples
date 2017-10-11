package daggerok.enumset.rest;

import com.querydsl.core.types.Predicate;
import daggerok.enumset.data.*;
import daggerok.enumset.data.catalog.Status;
import daggerok.enumset.data.catalog.Type;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static daggerok.enumset.data.JpaEnum.Catalog.TEST_1;
import static daggerok.enumset.data.JpaEnum.Catalog.TEST_2;
import static daggerok.enumset.data.catalog.Status.NOK;
import static daggerok.enumset.data.catalog.Status.OK;
import static daggerok.enumset.data.catalog.Type.TEST_ENTITY_1;
import static daggerok.enumset.data.catalog.Type.TEST_ENTITY_2;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonMap;
import static java.util.stream.Collectors.toList;

@RestController
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EnumSetRestController {

  final CatalogRepository catalogRepository;
  final JpaEnumRepository jpaEnumRepository;
  final MapCatalogRepository mapCatalogRepository;

  @PostConstruct
  @Transactional
  public void init() {

    catalogRepository.save(
        asList(
            new Catalog()
                .setType(TEST_ENTITY_1)
                .setStatuses(
                    new HashSet<>(
                        asList(
                            OK
                        )
                    )
                ),
            new Catalog()
                .setType(TEST_ENTITY_2)
                .setStatuses(
                    new HashSet<>(
                        asList(
                            OK,
                            NOK
                        )
                    )
                )
        )
    );

    mapCatalogRepository.save(
        asList(
            new MapCatalog().setStatuses(
                new HashMap<Status, Type>() {{
                  put(OK, TEST_ENTITY_1);
                  put(NOK, TEST_ENTITY_1);
                }}
            ),
            new MapCatalog().setStatuses(singletonMap(OK, TEST_ENTITY_2))
        )
    );

    jpaEnumRepository.save(
        asList(
            new JpaEnum().setCatalog(TEST_1),
            new JpaEnum().setCatalog(TEST_2)
        )
    );
  }

  @GetMapping("/api/v6")
  ResponseEntity<List<String>> getEnpoints() {
    return ResponseEntity.ok(
        asList(
            "/api/v6/jpa-enum",
            "/api/v6/catalog",
            "/api/v6/enum-collection/{name}",
            "/api/v6/map-catalog",
            "/api/v6/map-catalog/status/{name}",
            "/api/v6/map-catalog/type/{name}"
        )
    );
  }

  @GetMapping("/api/v6/jpa-enum")
  ResponseEntity<List<JpaEnum>> getJpaEnums(final Predicate predicate,
                                            @PageableDefault final Pageable pageable) {

    val page = jpaEnumRepository.findAll(predicate, pageable);
    val body = page.getContent();

    return ResponseEntity.ok(body);
  }

  @GetMapping("/api/v6/map-catalog")
  ResponseEntity<List<MapCatalog>> getMapCatalog(final Predicate predicate,
                                                 @PageableDefault final Pageable pageable) {

    val page = mapCatalogRepository.findAll(predicate, pageable);
    val body = page.getContent();

    return ResponseEntity.ok(body);
  }

  @GetMapping("/api/v6/map-catalog/status/{name}")
  ResponseEntity<List<MapCatalog>> getMapCatalogByStatus(@PathVariable @NotBlank final String name,
                                                         @PageableDefault final Pageable pageable) {

    val status = Try.of(() -> Status.valueOf(name))
                    .getOrElse(Status.NA);
    val body = mapCatalogRepository.findAll(pageable)
                                   .getContent()
                                   .stream()
                                   .filter(mc -> mc.getStatuses().keySet().contains(status))
                                   .collect(toList());
    return ResponseEntity.ok(body);
  }

  @GetMapping("/api/v6/map-catalog/type/{name}")
  ResponseEntity<List<MapCatalog>> getMapCatalogByType(@PathVariable @NotBlank final String name,
                                                       @PageableDefault final Pageable pageable) {

    val type = Try.of(() -> Type.valueOf(name))
                  .getOrElse(Type.NA);
    val body = mapCatalogRepository.findAll(pageable)
                                   .getContent()
                                   .stream()
                                   .filter(mc -> mc.getStatuses().values().contains(type))
                                   .collect(toList());
    return ResponseEntity.ok(body);
  }

  @GetMapping("/api/v6/catalog")
  ResponseEntity<List<Catalog>> getCatalog(@PageableDefault final Pageable pageable) {

    val page = catalogRepository.findAll(pageable);
    val body = page.getContent();

    return ResponseEntity.ok(body);
  }

  @GetMapping("/api/v6/enum-collection/{name}")
  ResponseEntity<List<Set<Status>>> getEnumCollection(@PathVariable final String name,
                                                      @PageableDefault final Pageable pageable) {

    val type = Try.of(() -> Type.valueOf(name))
                  .getOrElse(Type.NA);
    val page = catalogRepository.findAllByType(type, pageable);
    val body = page.getContent()
                   .stream()
                   .map(Catalog::getStatuses)
                   .collect(toList());

    return ResponseEntity.ok(body);
  }
}
