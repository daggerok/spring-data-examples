package daggerok.relationships.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import daggerok.audit.AuditionEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;

/*

CREATE TABLE engineer
(
  id           BIGSERIAL                NOT NULL
    CONSTRAINT engineer_pkey
    PRIMARY KEY,
  created_date DATE                     NOT NULL,
  modified_at  TIMESTAMP WITH TIME ZONE NOT NULL,
  username     VARCHAR(255)             NOT NULL
);

CREATE TABLE engineer_emails
(
  engineer_id BIGINT NOT NULL
    CONSTRAINT engineers_emails_to_engineers
    REFERENCES engineer,
  emails      VARCHAR(255)
);

CREATE TABLE engineer_labels
(
  engineer_id BIGINT       NOT NULL
    CONSTRAINT engineers_labels_to_engineers
    REFERENCES engineer,
  labels      VARCHAR(255),
  labels_key  VARCHAR(255) NOT NULL,
  CONSTRAINT engineer_labels_pkey
  PRIMARY KEY (engineer_id, labels_key)
);

CREATE TABLE engineer_tags
(
  engineer_id BIGINT       NOT NULL
    CONSTRAINT engineers_tags_to_engineers
    REFERENCES engineer,
  tags        VARCHAR(255),
  tags_key    VARCHAR(255) NOT NULL,
  CONSTRAINT engineer_tags_pkey
  PRIMARY KEY (engineer_id, tags_key)
);

*/
@Data
@Entity
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Engineer extends AuditionEntity {

  private static final long serialVersionUID = 2313470924334128640L;

  @NotBlank
  @Column(nullable = false)
  String username;

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(foreignKey = @ForeignKey(name = "engineers_emails_to_engineers"))
  Set<String> emails = newHashSet();

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(foreignKey = @ForeignKey(name = "engineers_labels_to_engineers"))
  Map<String, String> labels = new HashMap<>();

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(foreignKey = @ForeignKey(name = "engineers_tags_to_engineers"))
  Map<String, String> tags = new HashMap<>();
}
