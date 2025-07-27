package io.github.UmbrellaLeaf5.synth.core.command.validator;

import io.github.UmbrellaLeaf5.synth.core.command.SynthCommand;

public interface SynthCommandValidator {
  void validate(SynthCommand command);
}
