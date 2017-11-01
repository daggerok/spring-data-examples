package daggerok.domain.audit;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;

import static com.fasterxml.jackson.databind.util.StdDateFormat.DATE_FORMAT_STR_ISO8601;
import static javax.persistence.GenerationType.IDENTITY;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME;

@Data
@MappedSuperclass
@Accessors(chain = true)
@EqualsAndHashCode(exclude = "id")
@JsonIgnoreProperties(ignoreUnknown = true)
@EntityListeners(AuditingEntityListener.class)
public class AuditionEntity implements Serializable {

  private static final long serialVersionUID = 5853656832908318660L;

  @Id
  @GeneratedValue(strategy = IDENTITY)
  Long id;

  @CreatedDate
  @Column(nullable = false)
  @DateTimeFormat(iso = DATE)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "UTC")
  LocalDate createdDate;

  @LastModifiedDate
  @DateTimeFormat(iso = DATE_TIME)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_FORMAT_STR_ISO8601, timezone = "UTC")
  @Column(nullable = false, columnDefinition = "timestamp with time zone")
  ZonedDateTime modifiedAt;
}
