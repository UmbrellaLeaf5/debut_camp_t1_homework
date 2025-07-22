package io.github.UmbrellaLeaf5.api;

import io.github.UmbrellaLeaf5.command.exception.UnavailableCommandException;
import io.github.UmbrellaLeaf5.synth.core.errorhandling.SyntheticHumanGlobalExceptionHandler;
import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class BishopExceptionHandler extends SyntheticHumanGlobalExceptionHandler {
  @ExceptionHandler(UnavailableCommandException.class)
  public ErrorResponse handleExecutionQueueIsFullException(UnavailableCommandException e) {
    return super.buildErrorResponse(e, HttpStatus.BAD_REQUEST, "COMMAND IS UNAVAILABLE");
  }
}
