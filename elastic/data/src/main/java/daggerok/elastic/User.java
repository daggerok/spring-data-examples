package daggerok.elastic;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Data
@Accessors(chain = true)
//@Document(indexName = "user", type = "customer")
@Document(
    indexName = "user",
    type = "customer",
    indexStoreType = "mmapfs",
    replicas = 1,
    shards = 3,
    createIndex = true,
    refreshInterval = "-1"
)
public class User {

  @Id
  String id;
  String username, name;
}
