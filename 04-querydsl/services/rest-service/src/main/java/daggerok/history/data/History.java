package daggerok.history.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import daggerok.audit.AuditionEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.Entity;

//import static daggerok.utils.Json.stringify;

@Data
@Entity
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class History extends AuditionEntity {

  private static final long serialVersionUID = -5211578247798280911L;

  String name;
  String event;
}
