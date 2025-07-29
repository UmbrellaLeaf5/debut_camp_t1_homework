package io.github.UmbrellaLeaf5.authorization.config;

import io.github.UmbrellaLeaf5.authorization.user.User;
import java.util.Optional;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class ApplicationAuditorAware implements AuditorAware<String> {
  @Override
  public Optional<String> getCurrentAuditor() {
    final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null || !authentication.isAuthenticated()
        || authentication instanceof AnonymousAuthenticationToken)
      return Optional.empty();

    final User user = (User) authentication.getPrincipal();
    return Optional.ofNullable(user.getId());
  }
}
