package ru.t1.jwt_assymetric.role;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.*;
import lombok.experimental.SuperBuilder;
import ru.t1.jwt_assymetric.common.BaseEntity;
import ru.t1.jwt_assymetric.user.User;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(name = "ROLES")
public class Role extends BaseEntity {
  private String name;

  @ManyToMany(mappedBy = "roles") private List<User> users;
}
