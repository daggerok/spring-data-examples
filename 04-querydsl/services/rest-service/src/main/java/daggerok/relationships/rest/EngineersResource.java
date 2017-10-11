package daggerok.relationships.rest;

import daggerok.relationships.data.Engineer;
import daggerok.relationships.data.EngineerRepository;
import daggerok.relationships.rest.model.EngineersRequest;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static daggerok.relationships.rest.model.predicate.EngineersPredicateUtils.getPredicate;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
public class EngineersResource {

  final EngineerRepository engineerRepository;

  @GetMapping("/api/v4/engineers")
  public List<Engineer> getEngineers(final Pageable pageable) {

    return engineerRepository.findAll(pageable)
                             .getContent();
  }

  @GetMapping("/api/v4/engineers/{type}")
  public List<Engineer> filterAnyEngineers(@PathVariable("type") final String type,
                                           @Validated final EngineersRequest filter,
                                           final Pageable pageable) {

    val predicate = getPredicate(type, filter);
    return engineerRepository.findAll(predicate, pageable)
                             .getContent();
  }

  @ResponseStatus(CREATED)
  @PostMapping("/api/v4/engineers")
  public void saveOrUpdate(@RequestBody @Validated final Engineer engineer) {
    engineerRepository.auditableSave(engineer);
  }
}
