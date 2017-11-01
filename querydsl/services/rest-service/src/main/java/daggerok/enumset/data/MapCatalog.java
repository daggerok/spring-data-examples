package daggerok.enumset.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import daggerok.audit.AuditionEntity;
import daggerok.enumset.data.catalog.Status;
import daggerok.enumset.data.catalog.Type;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.EAGER;

@Data
@Entity
@NoArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MapCatalog extends AuditionEntity {

  private static final long serialVersionUID = -1395618513017133251L;

  @Enumerated(STRING)
  @ElementCollection(fetch = EAGER)
  @Column(name = "status", nullable = false)
  @CollectionTable(foreignKey = @ForeignKey(name = "map_catalog_statuses_to_map_catalog_fk"))
  Map<Status, Type> statuses = new HashMap<>();
}
