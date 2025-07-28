package io.github.UmbrellaLeaf5.authorization.user;

import io.github.UmbrellaLeaf5.authorization.user.request.ChangePasswordRequest;
import io.github.UmbrellaLeaf5.authorization.user.request.ProfileUpdateRequest;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
  void updateProfileInfo(ProfileUpdateRequest request, String userId);

  void changePassword(ChangePasswordRequest request, String userId);

  void deactivateAccount(String userId);

  void reactivateAccount(String userId);
}
