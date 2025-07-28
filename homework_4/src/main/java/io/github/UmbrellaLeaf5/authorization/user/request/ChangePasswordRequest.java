package io.github.UmbrellaLeaf5.authorization.user.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
