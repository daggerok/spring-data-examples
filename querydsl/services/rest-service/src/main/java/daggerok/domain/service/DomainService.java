package daggerok.domain.service;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.StringPath;
import daggerok.domain.data.Domain;
import daggerok.domain.data.DomainRepository;
import daggerok.domain.data.QDomain;
import daggerok.domain.data.QOtherDomain;
import daggerok.domain.rest.model.DomainRequest;
import daggerok.domain.rest.model.DomainResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Stream;

import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DomainService {

  final DomainRepository domainRepository;

  public List<DomainResponse> getAllDomains() {

    val source = domainRepository.findAll();
    return toResponse(source.stream());
  }

  public List<DomainResponse> filterDomains(final DomainRequest domainRequest) {

    val domain = QDomain.domain;
    val predicates = Stream.of(Pair.of(domain.username, domainRequest.getUsername()),
                               Pair.of(domain.firstName, domainRequest.getFirstName()),
                               Pair.of(domain.lastName, domainRequest.getLastName()))
                           .filter(pair -> nonNull(pair.getRight()))
                           .filter(pair -> !pair.getRight().isEmpty())
                           .map(this::toPredicate)
                           .collect(toList());

    ofNullable(domainRequest.getTests())
        .ifPresent(tests -> tests.stream()
                                 .filter(test -> !test.isEmpty())
                                 .forEach(value -> predicates.add(
                                     toPredicate(Pair.of(QOtherDomain.otherDomain.test, value)))));

    val predicate = ExpressionUtils.anyOf(predicates);
    val source = domainRepository.filter(predicate);

    return toResponse(source);
  }

  private List<DomainResponse> toResponse(final Stream<Domain> source) {

    return source.map(Domain::getUsername)
                 .map(DomainResponse::new)
                 .collect(toList());
  }

  private Predicate toPredicate(Pair<StringPath, String> pair) {

    return ExpressionUtils.allOf(pair.getLeft().isNotNull(),
                                 pair.getLeft().isNotEmpty(),
                                 pair.getLeft().containsIgnoreCase(pair.getRight()));
  }
}
