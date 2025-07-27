package io.github.UmbrellaLeaf5.synth.core.audit.properties.kafka;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties("synth.core.audit.kafka")
@Data
@Component
@ConditionalOnProperty(name = "synth.core.audit.mode", havingValue = "KAFKA")
public class KafkaAuditConfigurationProperties {
  @NotBlank private String topic;
}
