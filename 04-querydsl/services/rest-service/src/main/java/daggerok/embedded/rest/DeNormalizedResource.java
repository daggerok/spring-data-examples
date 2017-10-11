package daggerok.embedded.rest;

import daggerok.embedded.data.DeNormalized;
import daggerok.embedded.data.DeNormalizedRepository;
import daggerok.embedded.rest.exception.NotFound;
import lombok.Cleanup;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DeNormalizedResource {

  final DeNormalizedRepository repository;

  @GetMapping("/api/v2/de-normalized")
  public List<DeNormalized> get() {
    return repository.findAll();
  }

  @GetMapping("/api/v2/pagination")
  public List<daggerok.embedded.flatten.DeNormalized> pagination(final Pageable pageable) {
    val page = repository.getPage(pageable);
    return page.getContent();
  }

  @GetMapping("/api/v2/sorted")
  public List<daggerok.embedded.flatten.DeNormalized> sorted(final Sort sort) {
    @Cleanup val stream = repository.sortedStream(sort);
    return stream.collect(toList());
  }

  @GetMapping("/api/v2/flatten")
  public List<daggerok.embedded.flatten.DeNormalized> flatten() {
    @Cleanup val stream = repository.streamAll();
    return stream.collect(toList());
  }

  @GetMapping("/api/v2/flatten/{id}")
  public daggerok.embedded.flatten.DeNormalized flatten(@PathVariable final Long id) {
    return repository.findById(id)
                     .orElseThrow(() -> NotFound.byId(id));
  }
}
