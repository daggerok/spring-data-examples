package daggerok.keyvalue.keyvalueobj;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.keyvalue.annotation.KeySpace;

import java.io.Serializable;

@Data
@KeySpace("keyValueObj")
@Accessors(chain = true)
@EqualsAndHashCode(exclude = "key")
public class KeyValueObj implements Serializable {

  private static final long serialVersionUID = -4954224176616797887L;

  @Id
  String key;
  String value;
}
