package ru.t1.jwt_assymetric.user.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChangePasswordRequest {
  private String oldPassword;
  private String newPassword;
  private String newPasswordConfirm;
}
