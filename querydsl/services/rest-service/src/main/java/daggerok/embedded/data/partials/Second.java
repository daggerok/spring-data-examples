package daggerok.embedded.data.partials;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Data
@Embeddable
@NoArgsConstructor
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Second {

  @Column(nullable = false)
  String secondField1 = "secondField1";

  @Column(nullable = false)
  String secondField2 = "secondField2";
}
