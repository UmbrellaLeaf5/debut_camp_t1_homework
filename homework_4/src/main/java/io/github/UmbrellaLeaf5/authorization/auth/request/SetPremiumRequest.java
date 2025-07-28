package io.github.UmbrellaLeaf5.authorization.auth.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SetPremiumRequest {
  private String email;
}
