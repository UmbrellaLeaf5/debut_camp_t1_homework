package io.github.UmbrellaLeaf5.authorization.auth.request;

import io.github.UmbrellaLeaf5.authorization.validation.NonDisposableEmail;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegistrationRequest {
  @NotBlank(message = "VALIDATION.REGISTRATION.EMAIL.NOT_BLANK")
  @Email(message = "VALIDATION.REGISTRATION.EMAIL.FORMAT")
  @NonDisposableEmail(message = "VALIDATION.REGISTRATION.EMAIL.DISPOSABLE")
  @Schema(example = "mike@summer_school.ru")
  private String email;

  @NotBlank(message = "VALIDATION.REGISTRATION.PASSWORD.NOT_BLANK")
  @Size(min = 1, max = 255, message = "VALIDATION.REGISTRATION.PASSWORD.SIZE")
  @Schema(example = "1")
  @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*\\W).*$",
      message = "VALIDATION.REGISTRATION.PASSWORD.WEAK")
  private String password;

  @NotBlank(message = "VALIDATION.REGISTRATION.PASSWORD.NOT_BLANK")
  @Size(min = 1, max = 255, message = "VALIDATION.REGISTRATION.PASSWORD.SIZE")
  @Schema(example = "1")
  private String confirmPassword;

  @NotBlank(message = "VALIDATION.REGISTRATION.FIRSTNAME.NOT_BLANK")
  @Size(min = 1, max = 255, message = "VALIDATION.REGISTRATION.FIRSTNAME.SIZE")
  @Schema(example = "Mike")
  private String firstName;

  @NotBlank(message = "VALIDATION.REGISTRATION.LASTNAME.NOT_BLANK")
  @Size(min = 1, max = 255, message = "VALIDATION.REGISTRATION.LASTNAME.SIZE")
  @Schema(example = "Sobolev")
  private String lastName;
}
