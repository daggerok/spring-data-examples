package daggerok.elastic;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Data
@Accessors(chain = true)
@Document(indexName = "user", type = "customer")
//@Document(indexName = "daggerok", type = "user")
//@Document(indexName = "user", type = "user", shards = 1, replicas = 0, refreshInterval = "-1")
//@Document(indexName = "user")
//@Document(
//    indexName = "user-index",
//    indexStoreType = "mmapfs",
//    type = "user",
//    replicas = 0,
//    shards = 1,
//    createIndex = true,
//    refreshInterval = "-1"
//)
public class User {

  @Id
  String id;
  String username, name;
}
