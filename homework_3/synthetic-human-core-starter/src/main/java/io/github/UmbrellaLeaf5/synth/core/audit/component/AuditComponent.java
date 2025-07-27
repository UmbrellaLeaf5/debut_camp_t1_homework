package io.github.UmbrellaLeaf5.synth.core.audit.component;

import io.github.UmbrellaLeaf5.synth.core.audit.event.AuditEvent;

public interface AuditComponent {
  void audit(AuditEvent event);
}
