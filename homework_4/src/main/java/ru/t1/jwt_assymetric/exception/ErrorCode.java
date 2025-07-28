package ru.t1.jwt_assymetric.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
  USER_NOT_FOUND("USER_NOT_FOUND", "User not found with id %s", HttpStatus.NOT_FOUND),
  CHANGE_PASSWORD_MISMATCH("CHANGE_PASSWORD_MISMATCH",
      "Current password and new password are not the same", HttpStatus.BAD_REQUEST),
  INVALID_CURRENT_PASSWORD(
      "INVALID_CURRENT_PASSWORD)", "Current password is invalid", HttpStatus.BAD_REQUEST),
  ACCOUNT_ALREADY_DEACTIVATED(
      "ACCOUNT_ALREADY_DEACTIVATED", "Account already deactivated", HttpStatus.BAD_REQUEST),
  ACCOUNT_ALREADY_ACTIVATED(
      "ACCOUNT_ALREADY_ACTIVATED", "Account already activated", HttpStatus.BAD_REQUEST),
  EMAIL_ALREADY_EXISTS("EMAIL_ALREADY_EXISTS", "Email already exists", HttpStatus.BAD_REQUEST),
  PASSWORD_MISMATCH("PASSWORD_MISMATCH", "Current password and new password are not the same",
      HttpStatus.BAD_REQUEST),
  ERR_USER_DISABLED("ERR_USER_DISABLED", "User disabled", HttpStatus.UNAUTHORIZED),
  BAD_CREDENTIALS(
      "BAD_CREDENTIALS", "Username and / or password is incorrect", HttpStatus.UNAUTHORIZED),
  USERNAME_NOT_FOUND("USERNAME_NOT_FOUND", "Username not found", HttpStatus.NOT_FOUND),
  INTERNAL_EXCEPRION("INTERNAL_EXCEPRION", "Internal error", HttpStatus.INTERNAL_SERVER_ERROR);

  private final String code;
  private final String defaultMessage;
  private final HttpStatus status;

  ErrorCode(final String code, final String defaultMessage, final HttpStatus status) {
    this.code = code;
    this.defaultMessage = defaultMessage;
    this.status = status;
  }
}
