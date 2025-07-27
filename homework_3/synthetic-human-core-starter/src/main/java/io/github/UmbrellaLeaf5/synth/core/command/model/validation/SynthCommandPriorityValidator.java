package io.github.UmbrellaLeaf5.synth.core.command.model.validation;

import io.github.UmbrellaLeaf5.synth.core.command.model.SynthCommandPriority;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class SynthCommandPriorityValidator
    implements ConstraintValidator<ValidSynthCommandPriority, SynthCommandPriority> {
  @Override
  public boolean isValid(SynthCommandPriority value, ConstraintValidatorContext context) {
    if (value == null)
      return false;

    try {
      SynthCommandPriority.valueOf(value.name());
      return true;

    } catch (IllegalArgumentException e) {
      return false;
    }
  }
}
