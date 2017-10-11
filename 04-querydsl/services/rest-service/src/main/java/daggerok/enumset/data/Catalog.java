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
import java.util.Set;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.EAGER;

@Data
@Entity
@NoArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Catalog extends AuditionEntity {

  private static final long serialVersionUID = 3782807491816210269L;

  @Enumerated(STRING)
  @Column(nullable = false)
  Type type;

  @JoinTable(
      name = "statuses",
      joinColumns = @JoinColumn(name = "id"),
      foreignKey = @ForeignKey(name = "statuses_to_status_catalog_fk")
  )
  @Enumerated(STRING)
  @ElementCollection(fetch = EAGER)
  @Column(name = "status", nullable = false)
  Set<Status> statuses;
}
