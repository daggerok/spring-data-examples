package daggerok.rest;

import daggerok.data.User;
import daggerok.data.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserResource {

  final UserRepository userRepository;

  @GetMapping
  public Map<String, List<User>> get() {
    return Collections.singletonMap("data", userRepository.findAll());
  }

  @PostMapping
  @Transactional
  @ResponseStatus(HttpStatus.CREATED)
  public Map<String, User> save(@RequestBody final User user) {
    return Collections.singletonMap("data", userRepository.save(user));
  }
}
