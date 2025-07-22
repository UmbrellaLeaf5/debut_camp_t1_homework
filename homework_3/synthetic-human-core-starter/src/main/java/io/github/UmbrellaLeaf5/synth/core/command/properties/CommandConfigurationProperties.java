package io.github.UmbrellaLeaf5.synth.core.command.properties;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("synth.core.command")
@Data
public class CommandConfigurationProperties {
  private ThreadPoolExecutorProperties poolProperties = new ThreadPoolExecutorProperties();

  @Data
  public static class ThreadPoolExecutorProperties {
    @Min(1) private int minSize = 3;
    @Min(1) private int maxSize = 3;
    @Min(3) private int queueCapacity = 6;
    @Min(0L) private long idleThreadKeepAliveTime = 0L;
    @NotNull private Duration terminationTimeout = Duration.ofMinutes(1);
  }
}
