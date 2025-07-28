package ru.t1.jwt_assymetric.handler;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ValidationError {
  private String field;
  private String message;
  private String code;
}
