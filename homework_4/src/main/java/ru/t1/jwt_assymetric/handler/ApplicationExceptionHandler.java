package ru.t1.jwt_assymetric.handler;

import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.t1.jwt_assymetric.exception.BusinessException;
import ru.t1.jwt_assymetric.exception.ErrorCode;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class ApplicationExceptionHandler {
  @ExceptionHandler(BusinessException.class)
  public ResponseEntity<ErrorResponse> handleBusinessException(final BusinessException e) {
    final ErrorResponse body =
        ErrorResponse.builder().message(e.getMessage()).code(e.getErrorCode().getCode()).build();

    log.info("BusinessException: {}", e.getMessage());
    log.debug(e.getMessage(), e);

    return ResponseEntity
        .status(e.getErrorCode().getStatus() != null ? e.getErrorCode().getStatus()
                                                     : HttpStatus.BAD_REQUEST)
        .body(body);
  }

  @ExceptionHandler(DisabledException.class)
  public ResponseEntity<ErrorResponse> handleDisabledException(final DisabledException e) {
    final ErrorResponse body = ErrorResponse.builder()
                                   .message(e.getMessage())
                                   .code(ErrorCode.ERR_USER_DISABLED.getCode())
                                   .build();
    return ResponseEntity.status(ErrorCode.ERR_USER_DISABLED.getStatus()).body(body);
  }

  @ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<ErrorResponse> handleBadCredentialsException(
      final BadCredentialsException e) {
    log.debug(e.getMessage(), e);
    final ErrorResponse body = ErrorResponse.builder()
                                   .message(ErrorCode.BAD_CREDENTIALS.getDefaultMessage())
                                   .code(ErrorCode.BAD_CREDENTIALS.getCode())
                                   .build();
    return ResponseEntity.status(ErrorCode.BAD_CREDENTIALS.getStatus()).body(body);
  }

  @ExceptionHandler(UsernameNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleUsernameNotFoundException(
      final UsernameNotFoundException e) {
    final ErrorResponse body = ErrorResponse.builder()
                                   .message(ErrorCode.USERNAME_NOT_FOUND.getDefaultMessage())
                                   .code(ErrorCode.USERNAME_NOT_FOUND.getCode())
                                   .build();
    return ResponseEntity.status(ErrorCode.USERNAME_NOT_FOUND.getStatus()).body(body);
  }

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleEntityNotFoundException(
      final EntityNotFoundException e) {
    log.debug(e.getMessage(), e);
    final ErrorResponse body = ErrorResponse.builder()
                                   .message(e.getMessage())
                                   .code(HttpStatus.NOT_FOUND.toString())
                                   .build();
    return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
      final MethodArgumentNotValidException e) {
    final List<ValidationError> errors = new ArrayList<>();
    e.getBindingResult().getAllErrors().forEach(error -> {
      final String fieldName = ((FieldError) error).getField();
      final String errorCode = error.getDefaultMessage();
      errors.add(
          ValidationError.builder().field(fieldName).message(errorCode).code(errorCode).build());
    });
    final ErrorResponse errorResponse = ErrorResponse.builder().validationErrors(errors).build();
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleException(final Exception e) {
    log.error(e.getMessage(), e);
    final ErrorResponse body = ErrorResponse.builder()
                                   .message(ErrorCode.INTERNAL_EXCEPRION.getDefaultMessage())
                                   .code(ErrorCode.INTERNAL_EXCEPRION.getCode())
                                   .build();
    return ResponseEntity.status(ErrorCode.INTERNAL_EXCEPRION.getStatus()).body(body);
  }
}
