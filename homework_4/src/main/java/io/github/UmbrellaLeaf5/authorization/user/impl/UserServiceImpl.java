package io.github.UmbrellaLeaf5.authorization.user.impl;

import io.github.UmbrellaLeaf5.authorization.exception.BusinessException;
import io.github.UmbrellaLeaf5.authorization.exception.ErrorCode;
import io.github.UmbrellaLeaf5.authorization.user.User;
import io.github.UmbrellaLeaf5.authorization.user.UserMapper;
import io.github.UmbrellaLeaf5.authorization.user.UserRepository;
import io.github.UmbrellaLeaf5.authorization.user.UserService;
import io.github.UmbrellaLeaf5.authorization.user.request.ChangePasswordRequest;
import io.github.UmbrellaLeaf5.authorization.user.request.ProfileUpdateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
