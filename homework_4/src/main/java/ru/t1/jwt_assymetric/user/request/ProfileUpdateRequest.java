package ru.t1.jwt_assymetric.user.request;

import java.time.LocalDate;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileUpdateRequest {
  private String firstName;
  private String lastName;
  private LocalDate dateOfBirth;
}
