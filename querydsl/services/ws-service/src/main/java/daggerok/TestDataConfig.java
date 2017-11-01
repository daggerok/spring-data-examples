package daggerok;

import daggerok.domain.country.Country;
import daggerok.domain.country.CountryRepository;
import daggerok.gen.Currency;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Stream;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class TestDataConfig {

  final CountryRepository countryRepository;

  @Bean
  @Transactional
  public CommandLineRunner testData() {

    if (countryRepository.count() > 0) return args -> log.info("skipping test data.");

    return args -> Stream.of(Country.of("Spain", "Madrid", Currency.EUR, 46704314L),
                             Country.of("Poland", "Warsaw", Currency.PLN, 38186860L),
                             Country.of("United Kingdom", "London", Currency.GBP, 63705000L))
                         .forEach(countryRepository::save);
  }
}
