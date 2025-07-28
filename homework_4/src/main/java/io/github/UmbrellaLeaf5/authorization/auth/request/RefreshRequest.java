package io.github.UmbrellaLeaf5.authorization.auth.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RefreshRequest {
  private String refreshToken;
}
