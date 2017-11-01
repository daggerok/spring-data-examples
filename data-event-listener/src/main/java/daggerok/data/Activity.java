package daggerok.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Activity implements Serializable {

  private static final long serialVersionUID = -8874175142990046027L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;

  @OneToMany
  List<Task> tasks;
}
