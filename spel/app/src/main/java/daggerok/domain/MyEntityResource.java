package daggerok.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static java.util.Collections.singletonMap;

@RestController
@RequiredArgsConstructor
public class MyEntityResource {

  final MyEntityRepository myEntityRepository;

  @GetMapping
  public Map<String, Page<MyEntity>> getAll(@PageableDefault final Pageable pageable) {
    return singletonMap("data", myEntityRepository.findAll(pageable));
  }

  @GetMapping("/names")
  public Map<String, Page<String>> getNames(@PageableDefault final Pageable pageable) {
    return singletonMap("data", myEntityRepository.findAllNames(pageable));
  }
}
