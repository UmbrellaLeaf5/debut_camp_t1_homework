package io.github.UmbrellaLeaf5.authorization.user;

import io.github.UmbrellaLeaf5.authorization.auth.request.RegistrationRequest;
import io.github.UmbrellaLeaf5.authorization.user.request.ProfileUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {
  private final PasswordEncoder passwordEncoder;

  public void mergeUserInfo(final User savedUser, final ProfileUpdateRequest request) {
    if (StringUtils.isNotBlank(request.getFirstName())
        && !savedUser.getFirstName().equals(request.getFirstName())) {
      savedUser.setFirstName(request.getFirstName());
    }
    if (StringUtils.isNotBlank(request.getLastName())
        && !savedUser.getLastName().equals(request.getLastName())) {
      savedUser.setLastName(request.getLastName());
    }
    if (request.getDateOfBirth() != null
        && !request.getDateOfBirth().equals(savedUser.getDateOfBirth())) {
      savedUser.setDateOfBirth(request.getDateOfBirth());
    }
  }

  public User toUser(RegistrationRequest request) {
    return User.builder()
        .firstName(request.getFirstName())
        .lastName(request.getLastName())
        .email(request.getEmail())
        .password(passwordEncoder.encode(request.getPassword()))
        .enabled(true)
        .locked(false)
        .credentialsExpired(false)
        .build();
  }
}
