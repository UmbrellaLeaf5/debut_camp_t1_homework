package io.github.UmbrellaLeaf5.synth.core.audit.component;

import io.github.UmbrellaLeaf5.synth.core.audit.event.AuditEvent;
import io.github.UmbrellaLeaf5.synth.core.audit.properties.kafka.KafkaAuditConfigurationProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "synth.core.audit.mode", havingValue = "KAFKA")
@RequiredArgsConstructor
public class KafkaAuditComponent implements AuditComponent {
  private final KafkaTemplate<String, AuditEvent> kafkaTemplate;
  private final KafkaAuditConfigurationProperties auditConfigurationProperties;

  @Override
  public void audit(AuditEvent event) {
    kafkaTemplate.send(auditConfigurationProperties.getTopic(), event);
  }
}
