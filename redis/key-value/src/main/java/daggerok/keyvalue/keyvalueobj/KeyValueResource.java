package daggerok.keyvalue.keyvalueobj;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/kv")
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class KeyValueResource {

  final KeyValueObjRepository keyValueObjRepository;

  @GetMapping
  public List<KeyValueObj> getKeyValueObjs() {

    return StreamSupport.stream(keyValueObjRepository.findAll().spliterator(), true)
                        .collect(toList());
  }

  @PostMapping
  @Transactional
  public KeyValueObj postKeyValueObj(@RequestBody final KeyValueObj user) {
    return keyValueObjRepository.save(user);
  }
}
