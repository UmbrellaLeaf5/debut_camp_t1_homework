package io.github.UmbrellaLeaf5.command;

import io.github.UmbrellaLeaf5.api.model.CommandType;
import io.github.UmbrellaLeaf5.api.model.Initiator;
import io.github.UmbrellaLeaf5.command.exception.UnavailableCommandException;
import io.github.UmbrellaLeaf5.synth.core.audit.aspect.WaylandWatchingYou;
import io.github.UmbrellaLeaf5.synth.core.command.model.SynthCommand;
import io.github.UmbrellaLeaf5.synth.core.command.model.SynthCommandPriority;
import io.github.UmbrellaLeaf5.synth.core.command.service.CommandService;
import io.github.UmbrellaLeaf5.synth.core.command.service.exception.ExecutionQueueIsFullException;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BishopCommandService {
  private final CommandService commandService;

  @WaylandWatchingYou
  public void runCommand(CommandType commandType, Initiator initiator)
      throws UnavailableCommandException, ExecutionQueueIsFullException {
    final SynthCommand.SynthCommandBuilder synthCommandBuilder = SynthCommand.builder();

    synthCommandBuilder.description(chooseDescription(commandType));
    synthCommandBuilder.author(initiator.name());
    synthCommandBuilder.priority(choosePriority(initiator));
    synthCommandBuilder.time(DateTimeFormatter.ISO_INSTANT.format(Instant.now()));

    commandService.processCommand(synthCommandBuilder.build());
  }

  private String chooseDescription(CommandType commandType) throws UnavailableCommandException {
    return switch (commandType) {
      case ALERT -> "ALERT! ALERT! ALERT!";
      case HELP -> "Try to help...";
      case KILL ->
        throw new UnavailableCommandException("I cannot killed yet... May be another time...");
      case ENGINE -> "Processing engine... Try to fix...";
    };
  }

  private SynthCommandPriority choosePriority(Initiator initiator) {
    return switch (initiator) {
      case REGULAR_HUMAN -> SynthCommandPriority.COMMON;
      case WAYLAND_YUTANI_OFFICER -> SynthCommandPriority.CRITICAL;
    };
  }
}
