package io.github.UmbrellaLeaf5.synth.core.audit.service.kafka;

import io.github.UmbrellaLeaf5.synth.core.audit.model.AuditEvent;
import io.github.UmbrellaLeaf5.synth.core.audit.properties.AuditConfigurationProperties;
import io.github.UmbrellaLeaf5.synth.core.audit.service.AuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(name = "synth.core.audit.mode", havingValue = "KAFKA")
@RequiredArgsConstructor
public class KafkaAuditService implements AuditService {
  private final KafkaTemplate<String, AuditEvent> kafkaTemplate;
  private final AuditConfigurationProperties auditConfigurationProperties;

  @Override
  public void audit(AuditEvent event) {
    kafkaTemplate.send(auditConfigurationProperties.getTopic(), event);
  }
}
