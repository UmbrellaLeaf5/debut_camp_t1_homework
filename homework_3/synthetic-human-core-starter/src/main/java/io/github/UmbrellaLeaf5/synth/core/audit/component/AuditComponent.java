package io.github.UmbrellaLeaf5.synth.core.audit.component;

import io.github.UmbrellaLeaf5.synth.core.audit.model.AuditEvent;

public interface AuditComponent {
  void audit(AuditEvent event);
}
