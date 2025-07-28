package ru.t1.jwt_assymetric.auth.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthenticationResponse {
  @JsonProperty("access_token") private String accessToken;
  @JsonProperty("refresh_token") private String refreshToken;
  @JsonProperty("token_type") private String tokenType;
}
