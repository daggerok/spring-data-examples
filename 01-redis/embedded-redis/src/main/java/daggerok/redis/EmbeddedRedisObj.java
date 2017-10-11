package daggerok.redis;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@Data
@RedisHash("redisObj")
@Accessors(chain = true)
@EqualsAndHashCode(exclude = "id")
public class EmbeddedRedisObj implements Serializable {

  private static final long serialVersionUID = -7503936127116605659L;

  @Id
  Long id;
  String data;
}
