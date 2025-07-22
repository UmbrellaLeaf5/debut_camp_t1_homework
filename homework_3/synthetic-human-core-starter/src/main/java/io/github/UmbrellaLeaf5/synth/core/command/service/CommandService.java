package io.github.UmbrellaLeaf5.synth.core.command.service;

import io.github.UmbrellaLeaf5.synth.core.command.model.SynthCommand;
import io.github.UmbrellaLeaf5.synth.core.command.service.exception.ExecutionQueueIsFullException;

public interface CommandService {
  void processCommand(SynthCommand command) throws ExecutionQueueIsFullException;
}
