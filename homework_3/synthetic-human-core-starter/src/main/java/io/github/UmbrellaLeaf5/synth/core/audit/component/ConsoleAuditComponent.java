package io.github.UmbrellaLeaf5.synth.core.audit.component;

import io.github.UmbrellaLeaf5.synth.core.audit.model.AuditEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ConditionalOnProperty(
    name = "synth.core.audit.mode", havingValue = "CONSOLE", matchIfMissing = true)
public class ConsoleAuditComponent implements AuditComponent {
  @Override
  public void audit(AuditEvent event) {
    log.info("""
                        Audit event:
                        \tTime - {}
                        \tMethod- {}
                        \tParameters - {}
                        \tState - {}""", event.getTime(), event.getMethod(), event.getParams(), event.getState());
  }
}
