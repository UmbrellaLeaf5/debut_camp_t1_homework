package io.github.UmbrellaLeaf5.synth.core.audit.aspect;

import io.github.UmbrellaLeaf5.synth.core.audit.component.AuditComponent;
import io.github.UmbrellaLeaf5.synth.core.audit.model.AuditEvent;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;


@Aspect
@Component
@RequiredArgsConstructor
public class WaylandWatchingYouAspect {
  private final AuditComponent auditService;

  @Around("@annotation(io.github.UmbrellaLeaf5.synth.core.audit.aspect.WaylandWatchingYou)")
  public Object proceedMethodExecution(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
    final AuditEvent.AuditEventBuilder auditEventBuilder = AuditEvent.builder();
    final Method method = ((MethodSignature) proceedingJoinPoint.getSignature()).getMethod();

    auditEventBuilder.time(DateTimeFormatter.ISO_INSTANT.format(Instant.now()));
    auditEventBuilder.method(method.getName());
    auditEventBuilder.params(extractMethodParameters(method, proceedingJoinPoint.getArgs()));

    try {
      final Object result = proceedingJoinPoint.proceed();
      auditEventBuilder.state(AuditEvent.MethodExecutionState.SUCCESS);
      return result;

    } catch (Throwable e) {
      auditEventBuilder.state(AuditEvent.MethodExecutionState.EXCEPTION);
      throw e;

    } finally {
      auditService.audit(auditEventBuilder.build());
    }
  }

  public Map<String, AuditEvent.Param> extractMethodParameters(Method method, Object[] args) {
    final HashMap<String, AuditEvent.Param> result_params = new HashMap<String, AuditEvent.Param>();

    Parameter[] parameters = method.getParameters();

    for (int i = 0; i < parameters.length; i++) {
      Parameter param = parameters[i];
      result_params.put(
          param.getName(), new AuditEvent.Param(param.getType().getSimpleName(), args[i]));
    }

    return result_params;
  }
}
