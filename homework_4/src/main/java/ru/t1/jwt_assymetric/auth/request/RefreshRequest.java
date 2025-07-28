package ru.t1.jwt_assymetric.auth.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RefreshRequest {
  private String refreshToken;
}
