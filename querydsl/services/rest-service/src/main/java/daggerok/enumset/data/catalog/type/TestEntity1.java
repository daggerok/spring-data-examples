package daggerok.enumset.data.catalog.type;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import daggerok.audit.AuditionEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.Entity;

@Data
@Entity
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TestEntity1 extends AuditionEntity {

  String one = "test entity 1";
}
