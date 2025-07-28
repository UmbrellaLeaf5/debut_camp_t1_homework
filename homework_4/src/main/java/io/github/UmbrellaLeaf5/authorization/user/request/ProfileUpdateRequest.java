package io.github.UmbrellaLeaf5.authorization.user.request;

import java.time.LocalDate;
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
public class ProfileUpdateRequest {
  private String firstName;
  private String lastName;
  private LocalDate dateOfBirth;
}
