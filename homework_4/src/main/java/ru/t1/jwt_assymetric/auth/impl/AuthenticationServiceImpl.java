package ru.t1.jwt_assymetric.auth.impl;

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
import ru.t1.jwt_assymetric.auth.AuthenticationService;
import ru.t1.jwt_assymetric.auth.request.AuthenticationRequest;
import ru.t1.jwt_assymetric.auth.request.RefreshRequest;
import ru.t1.jwt_assymetric.auth.request.RegistrationRequest;
import ru.t1.jwt_assymetric.auth.request.SetPremiumRequest;
import ru.t1.jwt_assymetric.auth.response.AuthenticationResponse;
import ru.t1.jwt_assymetric.exception.BusinessException;
import ru.t1.jwt_assymetric.exception.ErrorCode;
import ru.t1.jwt_assymetric.role.Role;
import ru.t1.jwt_assymetric.role.RoleRepository;
import ru.t1.jwt_assymetric.security.JwtService;
import ru.t1.jwt_assymetric.user.User;
import ru.t1.jwt_assymetric.user.UserMapper;
import ru.t1.jwt_assymetric.user.UserRepository;

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
    if (request.getEmail() != null && request.getEmail().contains("admin")) {
      userRole = this.roleRepository.findByName("ROLE_ADMIN")
                     .orElseThrow(() -> new EntityNotFoundException("Role does not exist"));
    } else {
      userRole = this.roleRepository.findByName("ROLE_GUEST")
                     .orElseThrow(() -> new EntityNotFoundException("Role does not exist"));
    }
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
    if (password == null || !password.equals(confirmPassword)) {
      throw new BusinessException(ErrorCode.PASSWORD_MISMATCH);
    }
  }

  private void checkUserEmail(String email) {
    final boolean emailExists = this.userRepository.findByEmailIgnoreCase(email).isPresent();
    if (emailExists) {
      throw new BusinessException(ErrorCode.EMAIL_ALREADY_EXISTS);
    }
  }
}
