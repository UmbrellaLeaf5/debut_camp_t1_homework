package io.github.UmbrellaLeaf5.authorization.security;

import java.io.Serializable;
import java.util.List;
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
@RedisHash(value = "AccessToken")
@Builder
public class AccessToken implements Serializable {
  @Id @Indexed private String id;
  private List<String> accessToken;
}
