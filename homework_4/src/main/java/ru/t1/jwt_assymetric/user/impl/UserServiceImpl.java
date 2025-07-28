package ru.t1.jwt_assymetric.user.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.t1.jwt_assymetric.exception.BusinessException;
import ru.t1.jwt_assymetric.exception.ErrorCode;
import ru.t1.jwt_assymetric.user.User;
import ru.t1.jwt_assymetric.user.UserMapper;
import ru.t1.jwt_assymetric.user.UserRepository;
import ru.t1.jwt_assymetric.user.UserService;
import ru.t1.jwt_assymetric.user.request.ChangePasswordRequest;
import ru.t1.jwt_assymetric.user.request.ProfileUpdateRequest;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final UserMapper userMapper;

  @Override
  public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
    return userRepository.findByEmailIgnoreCase(userEmail).orElseThrow(
        () -> new UsernameNotFoundException("User with userEmail " + userEmail + " not found"));
  }

  @Override
  public void updateProfileInfo(ProfileUpdateRequest request, String userId) {
    final User savedUser = userRepository.findById(userId).orElseThrow(
        () -> new BusinessException(ErrorCode.USER_NOT_FOUND, userId));

    this.userMapper.mergeUserInfo(savedUser, request);
    this.userRepository.save(savedUser);
  }

  @Override
  public void changePassword(ChangePasswordRequest request, String userId) {
    if (!request.getNewPassword().equals(request.getNewPasswordConfirm())) {
      throw new BusinessException(ErrorCode.CHANGE_PASSWORD_MISMATCH);
    }

    final User savedUser = userRepository.findById(userId).orElseThrow(
        () -> new BusinessException(ErrorCode.USER_NOT_FOUND, userId));

    if (!this.passwordEncoder.matches(request.getOldPassword(), savedUser.getPassword())) {
      throw new BusinessException(ErrorCode.INVALID_CURRENT_PASSWORD);
    }

    final String encodedPassword = passwordEncoder.encode(request.getNewPassword());
    savedUser.setPassword(encodedPassword);
    this.userRepository.save(savedUser);
  }

  @Override
  public void deactivateAccount(String userId) {
    final User savedUser = userRepository.findById(userId).orElseThrow(
        () -> new BusinessException(ErrorCode.USER_NOT_FOUND, userId));

    if (!savedUser.isEnabled()) {
      throw new BusinessException(ErrorCode.ACCOUNT_ALREADY_DEACTIVATED);
    }
    savedUser.setEnabled(false);
    this.userRepository.save(savedUser);
  }

  @Override
  public void reactivateAccount(String userId) {
    final User savedUser = userRepository.findById(userId).orElseThrow(
        () -> new BusinessException(ErrorCode.USER_NOT_FOUND, userId));

    if (savedUser.isEnabled()) {
      throw new BusinessException(ErrorCode.ACCOUNT_ALREADY_ACTIVATED);
    }
    savedUser.setEnabled(true);
    this.userRepository.save(savedUser);
  }
}
