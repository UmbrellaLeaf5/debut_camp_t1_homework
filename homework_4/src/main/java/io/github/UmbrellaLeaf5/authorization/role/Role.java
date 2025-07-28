package io.github.UmbrellaLeaf5.authorization.role;

import io.github.UmbrellaLeaf5.authorization.common.BaseEntity;
import io.github.UmbrellaLeaf5.authorization.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.*;
import lombok.experimental.SuperBuilder;

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
