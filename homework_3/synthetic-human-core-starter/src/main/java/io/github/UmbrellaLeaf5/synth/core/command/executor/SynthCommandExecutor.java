package io.github.UmbrellaLeaf5.synth.core.command.executor;

import io.github.UmbrellaLeaf5.synth.core.command.model.SynthCommand;

public interface SynthCommandExecutor {
  void execute(SynthCommand synthCommand);
}
