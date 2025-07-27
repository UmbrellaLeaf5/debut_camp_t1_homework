package io.github.UmbrellaLeaf5.synth.core.audit.properties;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties("synth.core.audit")
@Data
@Component
public class AuditConfigurationProperties {
  @NotNull public AuditMode mode = AuditMode.CONSOLE;

  // TODO какие идеи по валидации тут?
  public String topic;

  public enum AuditMode { KAFKA, CONSOLE }
}
