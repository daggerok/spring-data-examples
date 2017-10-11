package daggerok.domain.rest.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Set;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class DomainRequest {

  String username;
  String firstName;
  String lastName;
  Set<String> tests;
}
