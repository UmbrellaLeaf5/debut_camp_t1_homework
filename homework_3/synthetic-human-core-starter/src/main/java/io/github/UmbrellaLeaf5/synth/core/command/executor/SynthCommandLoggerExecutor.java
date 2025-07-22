package io.github.UmbrellaLeaf5.synth.core.command.executor;

import io.github.UmbrellaLeaf5.synth.core.command.model.SynthCommand;
import io.github.UmbrellaLeaf5.synth.core.metrics.SyntheticHumanMetricService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SynthCommandLoggerExecutor implements SynthCommandExecutor {
  private final SyntheticHumanMetricService metricService;

  @Override
  @SneakyThrows
  public void execute(SynthCommand synthCommand) {
    Thread.sleep(1_000);
    log.info("""
            Start executing command...
            \tCommand from - {}
            \tregistered at - {}
            \tdescription - {}
            \tpriority - {}
            End executing command...""", synthCommand.getAuthor(), synthCommand.getTime(), synthCommand.getDescription(),
        synthCommand.getPriority());
    metricService.publishProceedTasksMetric(synthCommand.getAuthor());
  }
}
