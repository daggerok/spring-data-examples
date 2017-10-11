package daggerok.data;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@Table(name = "users")
@Accessors(chain = true)
public class User {

  @Id
  String username;
  String name;
  String password;
  String mail;
  String mail2;
}
