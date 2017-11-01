package daggerok.relationships.rest.model.validate;

import daggerok.relationships.rest.model.EngineersRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Map;
import java.util.Set;

import static java.util.Objects.nonNull;
import static org.springframework.util.CollectionUtils.isEmpty;

@Slf4j
@Component
public class EngineersRequestValidator implements ConstraintValidator<ValidEngineersRequest, EngineersRequest> {

  @Override
  public void initialize(final ValidEngineersRequest constraintAnnotation) {}

  @Override
  public boolean isValid(final EngineersRequest request, final ConstraintValidatorContext c) {

    if (nonNull(request)) request.map();

    return nonNull(request)
        && validSet(request.getEmails())
        || validMap(request.getLabels())
        || validMap(request.getTags());
  }

  private boolean validSet(final Set<String> list) {

    return !isEmpty(list) && list.stream()
                                 .map(String::trim)
                                 .noneMatch(String::isEmpty);
  }

  private boolean validMap(final Map<String, String> map) {

    return isEmpty(map) || map.entrySet()
                              .stream()
                              .noneMatch(
                                  entry ->
                                      StringUtils.isEmpty(entry.getKey())
                                          || StringUtils.isEmpty(entry.getValue()));
  }
}
