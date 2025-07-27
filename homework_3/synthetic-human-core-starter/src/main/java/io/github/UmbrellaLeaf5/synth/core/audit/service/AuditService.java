package io.github.UmbrellaLeaf5.synth.core.audit.service;

import io.github.UmbrellaLeaf5.synth.core.audit.model.AuditEvent;

public interface AuditService {
  void audit(AuditEvent event);
}
