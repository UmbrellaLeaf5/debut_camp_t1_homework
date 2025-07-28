package io.github.UmbrellaLeaf5.authorization.auth.impl;

import io.github.UmbrellaLeaf5.authorization.auth.AuthenticationService;
import io.github.UmbrellaLeaf5.authorization.auth.request.AuthenticationRequest;
import io.github.UmbrellaLeaf5.authorization.auth.request.RefreshRequest;
import io.github.UmbrellaLeaf5.authorization.auth.request.RegistrationRequest;
import io.github.UmbrellaLeaf5.authorization.auth.request.SetPremiumRequest;
import io.github.UmbrellaLeaf5.authorization.auth.response.AuthenticationResponse;
import io.github.UmbrellaLeaf5.authorization.exception.BusinessException;
import io.github.UmbrellaLeaf5.authorization.exception.ErrorCode;
import io.github.UmbrellaLeaf5.authorization.role.Role;
import io.github.UmbrellaLeaf5.authorization.role.RoleRepository;
import io.github.UmbrellaLeaf5.authorization.security.JwtService;
import io.github.UmbrellaLeaf5.authorization.user.User;
import io.github.UmbrellaLeaf5.authorization.user.UserMapper;
import io.github.UmbrellaLeaf5.authorization.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {
  private final AuthenticationManager authenticationManager;
  private final JwtService jwtService;
  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final UserMapper userMapper;
  @Override
  public AuthenticationResponse login(AuthenticationRequest request) {
    final Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

    final User user = (User) authentication.getPrincipal();
    final String accessToken = this.jwtService.generateAccessToken(user.getUsername());
    final String refreshToken = this.jwtService.generateRefreshToken(user.getUsername());
    final String tokenType = "Bearer";

    return AuthenticationResponse.builder()
        .accessToken(accessToken)
        .refreshToken(refreshToken)
        .tokenType(tokenType)
        .build();
  }

  @Override
  @Transactional
  public void register(RegistrationRequest request) {
    checkUserEmail(request.getEmail());
    checkPasswords(request.getPassword(), request.getConfirmPassword());

    final Role userRole;

    if (request.getEmail() != null && request.getEmail().contains("admin"))
      userRole = this.roleRepository.findByName("ROLE_ADMIN")
                     .orElseThrow(() -> new EntityNotFoundException("Role does not exist"));
    else
      userRole = this.roleRepository.findByName("ROLE_GUEST")
                     .orElseThrow(() -> new EntityNotFoundException("Role does not exist"));

    final List<Role> roles = new ArrayList<>();
    roles.add(userRole);

    final User user = this.userMapper.toUser(request);
    user.setRoles(roles);

    log.debug("Register user: {}", user);

    this.userRepository.save(user);
    log.debug("Registered user: {}", user);
  }

  @Override
  public AuthenticationResponse refreshToken(RefreshRequest request) {
    final String newAccessToken = this.jwtService.refreshToken(request.getRefreshToken());
    final String tokenType = "Bearer";

    return AuthenticationResponse.builder()
        .accessToken(newAccessToken)
        .refreshToken(request.getRefreshToken())
        .tokenType(tokenType)
        .build();
  }

  @Override
  public void logout(String userId) {
    this.jwtService.dropAllTokens(userId);
  }

  @Override
  public void setPremium(SetPremiumRequest request) {
    final User user = this.userRepository.findByEmailIgnoreCase(request.getEmail())
                          .orElseThrow(()
                                           -> new UsernameNotFoundException("User with userEmail "
                                               + request.getEmail() + " not found"));
    final Role userRole =
        this.roleRepository.findByName("ROLE_PREMIUM_USER")
            .orElseThrow(() -> new EntityNotFoundException("Role does not exist"));
    user.getRoles().add(userRole);
    this.userRepository.save(user);
  }

  private void checkPasswords(String password, String confirmPassword) {
    if (password == null || !password.equals(confirmPassword))
      throw new BusinessException(ErrorCode.PASSWORD_MISMATCH);
  }

  private void checkUserEmail(String email) {
    if (this.userRepository.findByEmailIgnoreCase(email).isPresent())
      throw new BusinessException(ErrorCode.EMAIL_ALREADY_EXISTS);
  }
}
