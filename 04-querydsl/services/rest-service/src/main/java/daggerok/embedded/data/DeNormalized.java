package daggerok.embedded.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import daggerok.audit.AuditionEntity;
import daggerok.embedded.data.partials.First;
import daggerok.embedded.data.partials.Second;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;

@Data
@Entity
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DeNormalized extends AuditionEntity {

  private static final long serialVersionUID = 5384663832891754139L;

  @Column(nullable = false)
  String deNormalizedField = "deNormalizedField";

  @Embedded
  /*@AttributeOverrides({
      @AttributeOverride(
          name = "firstField1",
          column = @Column(name = "field1", nullable = false)),
      @AttributeOverride(
          name = "firstField2",
          column = @Column(name = "field2", nullable = false)),
  })*/
      First first;

  Second second;
}
