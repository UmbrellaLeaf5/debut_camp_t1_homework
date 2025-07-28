package ru.t1.jwt_assymetric.user;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.t1.jwt_assymetric.user.request.ChangePasswordRequest;
import ru.t1.jwt_assymetric.user.request.ProfileUpdateRequest;

public interface UserService extends UserDetailsService {
  void updateProfileInfo(ProfileUpdateRequest request, String userId);

  void changePassword(ChangePasswordRequest request, String userId);

  void deactivateAccount(String userId);

  void reactivateAccount(String userId);
}
