package io.github.UmbrellaLeaf5.synth.core.command.validator;

import io.github.UmbrellaLeaf5.synth.core.command.model.SynthCommand;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JakartaSynthCommandValidator implements SynthCommandValidator {
  private final Validator validator;

  @Override
  public void validate(SynthCommand command) {
    final var violations = validator.validate(command);

    if (!violations.isEmpty())
      throw new ConstraintViolationException(violations);
  }
}
