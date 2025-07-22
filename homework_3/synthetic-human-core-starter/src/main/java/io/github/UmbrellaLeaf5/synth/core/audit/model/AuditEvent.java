package io.github.UmbrellaLeaf5.synth.core.audit.model;

import java.util.Map;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuditEvent {
  private String time;
  private String method;
  private Map<String, Param> params;
  private MethodExecutionState state;

  public record Param(String type, Object value) {}

  public enum MethodExecutionState { SUCCESS, EXCEPTION }
}
