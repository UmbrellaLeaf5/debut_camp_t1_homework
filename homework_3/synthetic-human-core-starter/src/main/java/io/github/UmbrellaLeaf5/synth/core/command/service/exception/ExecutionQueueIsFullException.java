package io.github.UmbrellaLeaf5.synth.core.command.service.exception;

public class ExecutionQueueIsFullException extends Exception {
  public ExecutionQueueIsFullException(String message, Exception e) {
    super(message, e);
  }
}
