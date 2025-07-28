package ru.t1.jwt_assymetric.auth.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthenticationRequest {
  @NotBlank(message = "VALIDATION.AUTHENTICATION.EMAIL.NOT_BLANK")
  @Email(message = "VALIDATION.AUTHENTICATION.EMAIL.FORMAT")
  @Schema(example = "mike@summer_school.ru")
  private String email;
  @NotBlank(message = "VALIDATION.AUTHENTICATION.PASSWORD.NOT_BLANK")
  @Schema(example = "1")
  private String password;
}
