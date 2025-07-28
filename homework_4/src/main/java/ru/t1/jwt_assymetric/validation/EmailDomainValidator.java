package ru.t1.jwt_assymetric.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;

public class EmailDomainValidator implements ConstraintValidator<NonDisposableEmail, String> {
  private final Set<String> blockedEmails;

  public EmailDomainValidator(@Value("${security.disposable-email}") List<String> blockedEmails) {
    this.blockedEmails =
        blockedEmails.stream().map(String::toLowerCase).collect(Collectors.toSet());
  }
  @Override
  public boolean isValid(String email, ConstraintValidatorContext context) {
    if (email == null || !email.contains("@")) {
      return true;
    }

    final int atIndex = email.indexOf("@") + 1;
    final int dotIndex = email.indexOf(".", atIndex);
    final String domain = email.substring(atIndex, dotIndex);

    return !this.blockedEmails.contains(domain);
  }
}
