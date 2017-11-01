package daggerok.domain.country;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import daggerok.domain.audit.AuditionEntity;
import daggerok.gen.Currency;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;

import static javax.persistence.EnumType.STRING;

@Data
@Entity
@NoArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@RequiredArgsConstructor(staticName = "of")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Country extends AuditionEntity {

  @NonNull
  @NotBlank
  @Column(nullable = false)
  String name;

  @NonNull
  @NotBlank
  @Column(nullable = false)
  String capital;

  @NonNull
  @Enumerated(STRING)
  @Column(nullable = false)
  Currency currency;

  @NonNull
  @Column(nullable = false)
  Long population;
}
