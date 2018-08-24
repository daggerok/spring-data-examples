CREATE KEYSPACE IF NOT EXISTS demo WITH replication = 
{'class':'SimpleStrategy','replication_factor':'1'};

CREATE TABLE IF NOT EXISTS demo.model
(
  timestamp timestamp,
  id varchar,
  PRIMARY KEY (id, timestamp)

) WITH CLUSTERING ORDER BY (timestamp DESC);
