package io.github.UmbrellaLeaf5.synth.core.error.handling;

import io.github.UmbrellaLeaf5.synth.core.command.service.exception.ExecutionQueueIsFullException;
import jakarta.validation.ConstraintViolationException;
import java.net.URI;
import java.time.Instant;
import java.util.NoSuchElementException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@RestControllerAdvice
public class SyntheticHumanGlobalExceptionHandler {
  @ExceptionHandler(ExecutionQueueIsFullException.class)
  public ErrorResponse handleExecutionQueueIsFullException(ExecutionQueueIsFullException e) {
    return buildErrorResponse(e, HttpStatus.TOO_MANY_REQUESTS, "ANDROID EXECUTION QUEUE IS FULL");
  }

  @ExceptionHandler({ConstraintViolationException.class, MethodArgumentNotValidException.class})
  public ErrorResponse handleValidationExceptions(Exception e) {
    return buildErrorResponse(e, HttpStatus.BAD_REQUEST, "Validation failed");
  }

  @ExceptionHandler({HttpMessageNotReadableException.class,
      MissingServletRequestParameterException.class, MethodArgumentTypeMismatchException.class})
  public ErrorResponse
  handleInputExceptions(Exception e) {
    return buildErrorResponse(e, HttpStatus.BAD_REQUEST, "Invalid input data");
  }

  @ExceptionHandler(NoSuchElementException.class)
  public ErrorResponse handleNotFoundExceptions(NoSuchElementException e) {
    return buildErrorResponse(e, HttpStatus.NOT_FOUND, "Requested resource not found");
  }

  @ExceptionHandler(Exception.class)
  public ErrorResponse handleAllUncaughtExceptions(Exception e) {
    return buildErrorResponse(e, HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
  }

  public ErrorResponse buildErrorResponse(Exception ex, HttpStatus statusCode, String detail) {
    String message = ex.getMessage();
    if (ex.getCause() != null)
      message += " | Caused by: " + ex.getCause().getMessage();

    logError(ex, statusCode);

    return ErrorResponse.builder(ex, statusCode, message)
        .detail(detail)
        .type(URI.create("/api/errors#" + statusCode.value()))
        .property("errorType", ex.getClass().getSimpleName())
        .property("timestamp", Instant.now())
        .build();
  }

  private void logError(Exception ex, HttpStatusCode statusCode) {
    if (statusCode.is5xxServerError())
      log.error("Server error ({}): {}", statusCode.value(), ex.getMessage(), ex);
    else
      log.warn("Client error ({}): {}", statusCode.value(), ex.getMessage());
  }
}
