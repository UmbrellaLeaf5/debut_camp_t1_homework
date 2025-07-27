package io.github.UmbrellaLeaf5.api;

import io.github.UmbrellaLeaf5.api.model.CommandType;
import io.github.UmbrellaLeaf5.api.model.Initiator;
import io.github.UmbrellaLeaf5.command.BishopCommandService;
import io.github.UmbrellaLeaf5.command.exception.UnavailableCommandException;
import io.github.UmbrellaLeaf5.synth.core.command.service.exception.ExecutionQueueIsFullException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/command")
@RequiredArgsConstructor
public class BishopApiV1 {
  private final BishopCommandService commandService;

  @PostMapping
  public void processCommand(
      @RequestParam CommandType commandType, @RequestParam Initiator initiator)
      throws UnavailableCommandException, ExecutionQueueIsFullException {
    commandService.runCommand(commandType, initiator);
  }
}
