package daggerok.embedded.flatten;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import daggerok.audit.AuditionEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.ZonedDateTime;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DeNormalized extends AuditionEntity {

  private static final long serialVersionUID = -8414913811359526469L;

  String deNormalizedField = "deNormalizedField";
  String firstField1 = "firstField1";
  String firstField2 = "firstField2";
  String secondField1 = "secondField1";
  String secondField2 = "secondField2";

  public DeNormalized(final Long id,
                      final LocalDate createdDate,
                      final ZonedDateTime modifiedAt,
                      final String deNormalizedField,
                      final String firstField1,
                      final String firstField2,
                      final String secondField1,
                      final String secondField2) {
    setId(id);
    setCreatedDate(createdDate);
    setModifiedAt(modifiedAt);
    setDeNormalizedField(deNormalizedField);
    setFirstField1(firstField1);
    setFirstField2(firstField2);
    setSecondField1(secondField1);
    setSecondField2(secondField2);
  }
}
