package daggerok.relationships.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import daggerok.relationships.rest.model.validate.ValidEngineersRequest;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.val;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

import static com.google.common.collect.Maps.newHashMap;
import static com.google.common.collect.Sets.newHashSet;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static org.springframework.util.CollectionUtils.isEmpty;

@Data
@NoArgsConstructor
@ValidEngineersRequest
@Accessors(chain = true)
public class EngineersRequest {

  Set<String> emails = newHashSet();

  @JsonIgnore
  Map<String, String> labels = newHashMap();

  @JsonIgnore
  Map<String, String> tags = newHashMap();

  public void map() {

    if (isEmpty(this.emails)) return;

    val list = emails.stream()
                     .map(e -> e.split("@"))
                     .map(a -> a.length > 0 ? Arrays.copyOf(a, a.length - 1) : a)
                     .map(Arrays::stream)
                     .flatMap(s -> s)
                     .filter(StringUtils::hasLength)
                     .map(String::trim)
                     .collect(toList());

    tags = asMap(list.stream());

    labels = asMap(list.stream()
                       .filter(w -> asList("com", "mail", "email").contains(w)));
  }

  private Map<String, String> asMap(final Stream<String> stream) {
    return stream.collect(toMap(o -> o, Function.identity()));
  }
}
