package daggerok;

import daggerok.domain.data.Domain;
import daggerok.domain.data.DomainRepository;
import daggerok.domain.data.OtherDomain;
import daggerok.domain.data.OtherDomainRepository;
import daggerok.embedded.data.DeNormalized;
import daggerok.embedded.data.DeNormalizedRepository;
import daggerok.embedded.data.partials.First;
import daggerok.embedded.data.partials.Second;
import daggerok.relationships.data.Engineer;
import daggerok.relationships.data.EngineerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.HashSet;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class TestDataConfig {

  final DomainRepository domainRepository;
  final OtherDomainRepository otherDomainRepository;
  final DeNormalizedRepository deNormalizedRepository;
  final EngineerRepository engineerRepository;

  @Bean
  @Transactional
  public CommandLineRunner testData() {

    engineersTestData();

    if (domainRepository.count() > 0) return args -> log.info("skipping test data.");

    IntStream.range(1, 3)
             .mapToObj(value -> "" + value)
             .map(value -> new DeNormalized().setDeNormalizedField(value)
                                             .setFirst(new First().setFirstField1(value)
                                                                  .setFirstField2(value))
                                             .setSecond(new Second().setSecondField1(value)
                                                                    .setSecondField2(value)))
             .forEach(deNormalizedRepository::save);

    return args -> Stream.of(1, 2, 3)
                         .map(String::valueOf)
                         .map("test"::concat)
                         .map(n -> new Domain().setUsername(n)
                                               .setLastName(n)
                                               .setFirstName(n)
                                               .setOtherDomain(
                                                   otherDomainRepository.save(
                                                       new OtherDomain().setTest(n))))
                         .forEach(domainRepository::save);
  }

  @SuppressWarnings("serial")
  private void engineersTestData() {

    if (engineerRepository.count() > 0) return;

    val tags = new HashMap<String, String>() {{
      put("t1", "v1");
      put("t2", "v2");
      put("t3", "v3");
    }};

    engineerRepository.save(
        asList(
            new Engineer().setUsername("u1")
                          .setEmails(new HashSet<>(asList(
                              "u1.1@mail.com",
                              "u1.2@mail.com"
                          )))
                          .setLabels(new HashMap<String, String>() {{
                            put("l1", "v1");
                            put("l3", "v3");
                          }})
                          .setTags(tags),
            new Engineer().setUsername("u2")
                          .setEmails(new HashSet<>(asList(
                              "u2.1@mail.com",
                              "u2.2@mail.com",
                              "u2.3@mail.com"
                          )))
                          .setLabels(new HashMap<String, String>() {{
                            put("l1", "v1");
                            put("l2", "v2");
                          }})
                          .setTags(tags),
            new Engineer().setUsername("u3")
                          .setEmails(new HashSet<>(singletonList("u3@mail.com")))
                          .setLabels(new HashMap<String, String>() {{
                            put("l2", "v2");
                            put("l3", "v3");
                          }})
                          .setTags(tags)
        )
    );
  }
}
