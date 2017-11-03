package daggerok.elastic;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequiredArgsConstructor
@Transactional(readOnly = true)
@RequestMapping(produces = APPLICATION_JSON_VALUE)
public class UserResource {

  final UserRepository userRepository;
  final ElasticsearchTemplate elasticsearchTemplate;

  @GetMapping
  List<User> get(@PageableDefault final Pageable pageable) {

    log.info("p: {}", pageable);

    return userRepository.findAll(pageable)
                         .getContent();
  }

  @GetMapping("/settings")
  Object settings() {
    return elasticsearchTemplate.getSetting(User.class);
  }

  @GetMapping("/failed")
  List<User> todo(@PageableDefault final Pageable pageable) {

    log.info("p: {}", pageable);

    return userRepository.streamPagedUsers(null, null, pageable)
                         .collect(toList());
  }

  @GetMapping("/users")
  HttpEntity<PagedResources<Resource<User>>> resource(@PageableDefault final Pageable pageable,
                                                      final PagedResourcesAssembler assembler) {
    log.info("p: {}", pageable);

    val pageOfUsers = userRepository.findAll(pageable);
    return ResponseEntity.ok(assembler.toResource(pageOfUsers));
  }

  @PostMapping
  @Transactional
  Optional<User> post(@RequestBody @Validated final User user) {

    log.info("u: {}", user);

    if (!ofNullable(user).isPresent()) return Optional.empty();

    val result = userRepository.save(user);
    return Optional.of(result);
  }
}
