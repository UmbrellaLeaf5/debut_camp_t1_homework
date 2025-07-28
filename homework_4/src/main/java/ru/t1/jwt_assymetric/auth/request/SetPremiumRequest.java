package ru.t1.jwt_assymetric.auth.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SetPremiumRequest {
  private String email;
}
