package daggerok.keyvalue.user;

import lombok.Cleanup;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/u")
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserResource {

  final UserRepository userRepository;

  @GetMapping("/page")
  public PagedResources<Resource<User>> getPageOfUsers(final Pageable pageable,
                                                       final PagedResourcesAssembler<User> assembler) {
    val page = userRepository.findAll(pageable);
    return assembler.toResource(page);
  }

  @GetMapping
  public List<User> getUsers() {

    @Cleanup val stream = userRepository.findAny();
    return stream.collect(toList());
  }

  @PostMapping
  @Transactional
  public User postUsers(@RequestBody final User user, final PagedResourcesAssembler assembler) {
    return userRepository.save(user);
  }
}
