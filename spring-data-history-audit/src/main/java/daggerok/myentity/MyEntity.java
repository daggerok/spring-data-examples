package daggerok.myentity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import daggerok.history.MyHistoryListener;
import daggerok.audit.Auditable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import java.time.LocalDateTime;

@Data
@Entity
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@EntityListeners(MyHistoryListener.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MyEntity extends Auditable {

  private static final long serialVersionUID = 7622172669741124338L;

  @NotEmpty(message = "value must not be null, empty or trimmed") String value;

  @LastModifiedDate
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  @JsonSerialize(using = LocalDateTimeSerializer.class)
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  LocalDateTime modifiedAt;
}
