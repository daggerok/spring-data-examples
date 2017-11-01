package daggerok.data;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@NoArgsConstructor
@Table(name = "users")
@Accessors(chain = true)
public class User {

  @Id
  String id;
  String name;
  String password;
  String mail;
  String mail2;
}
