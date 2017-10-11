package daggerok.enumset.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import daggerok.audit.AuditionEntity;
import daggerok.enumset.data.catalog.Status;
import daggerok.enumset.data.catalog.Type;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import static daggerok.enumset.data.catalog.Status.NOK;
import static daggerok.enumset.data.catalog.Status.OK;
import static daggerok.enumset.data.catalog.Type.TEST_ENTITY_1;
import static daggerok.enumset.data.catalog.Type.TEST_ENTITY_2;
import static java.util.Arrays.asList;
import static java.util.Collections.singleton;
import static javax.persistence.EnumType.STRING;

@Data
@Entity
@NoArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class JpaEnum extends AuditionEntity implements Serializable {

  private static final long serialVersionUID = 7725716335794007074L;

  @Enumerated(STRING)
  @Column(nullable = false)
  Catalog catalog;

  @RequiredArgsConstructor
  public enum Catalog {

    TEST_1(TEST_ENTITY_1, new HashSet<>(asList(OK, NOK))),
    TEST_2(TEST_ENTITY_2, singleton(NOK));

    public final Type type;
    public final Set<Status> statuses;
  }
}
