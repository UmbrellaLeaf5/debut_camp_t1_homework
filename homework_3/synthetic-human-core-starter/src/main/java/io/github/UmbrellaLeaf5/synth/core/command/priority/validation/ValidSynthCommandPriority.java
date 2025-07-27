package io.github.UmbrellaLeaf5.synth.core.command.priority.validation;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target({FIELD, PARAMETER, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = SynthCommandPriorityValidator.class)
@Documented
public @interface ValidSynthCommandPriority {
  String message() default "Invalid priority value. Must be one of: COMMON, CRITICAL";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
