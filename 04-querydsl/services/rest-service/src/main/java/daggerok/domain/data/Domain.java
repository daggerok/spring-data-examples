package daggerok.domain.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import daggerok.audit.AuditionEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Data
@Entity
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Domain extends AuditionEntity {

  private static final long serialVersionUID = 7217062414225635846L;

  private String username, firstName, lastName;

  @OneToOne
  @JoinColumn(foreignKey = @ForeignKey(name = "domain_to_other_domain"))
  private OtherDomain otherDomain;
}
