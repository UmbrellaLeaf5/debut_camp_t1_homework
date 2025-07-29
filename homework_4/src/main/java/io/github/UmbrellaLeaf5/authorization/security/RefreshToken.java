package io.github.UmbrellaLeaf5.authorization.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RedisHash(value = "RefreshToken")
@Builder
public class RefreshToken {
  @Id @Indexed private String id;

  private String refreshToken;
}
