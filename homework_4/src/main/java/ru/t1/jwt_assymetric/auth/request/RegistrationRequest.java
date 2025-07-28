package ru.t1.jwt_assymetric.auth.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;
import ru.t1.jwt_assymetric.validation.NonDisposableEmail;

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
