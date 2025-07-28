package io.github.UmbrellaLeaf5.authorization.user;

import io.github.UmbrellaLeaf5.authorization.role.Role;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.CollectionUtils;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "USERS")
@EntityListeners(AuditingEntityListener.class)
public class User implements UserDetails {
  @Id @GeneratedValue(strategy = GenerationType.UUID) private String id;

  @Column(name = "FIRST_NAME", nullable = false) private String firstName;

  @Column(name = "LAST_NAME", nullable = false) private String lastName;

  @Column(name = "EMAIL", nullable = false, unique = true) private String email;

  @Column(name = "PASSWORD", nullable = false) private String password;

  @Column(name = "DATE_OF_BIRTH") private LocalDate dateOfBirth;

  @Column(name = "IS_ENABLED") private boolean enabled;

  @Column(name = "IS_ACCOUNT_LOCKED") private boolean locked;

  @Column(name = "IS_CREDENTIALS_EXPIRED") private boolean credentialsExpired;

  @CreatedDate
  @Column(name = "CREATED_DATE", updatable = false, nullable = false)
  private LocalDateTime createdDate;

  @LastModifiedDate
  @Column(name = "LAST_MODIFIED_DATE", insertable = false)
  private LocalDateTime lastModifiedDate;

  @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
  @JoinTable(name = "USERS_ROLES", joinColumns = { @JoinColumn(name = "USERS_ID") },
      inverseJoinColumns = { @JoinColumn(name = "ROLES_ID") })
  private List<Role> roles;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    if (CollectionUtils.isEmpty(this.roles)) {
      return List.of();
    }
    return this.roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).toList();
  }

  @Override
  public String getUsername() {
    return this.email;
  }

  @Override
  public String getPassword() {
    return this.password;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return !this.locked;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return !this.credentialsExpired;
  }
}
