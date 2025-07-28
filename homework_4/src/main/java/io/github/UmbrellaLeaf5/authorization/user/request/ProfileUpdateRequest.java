package io.github.UmbrellaLeaf5.authorization.user.request;

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
