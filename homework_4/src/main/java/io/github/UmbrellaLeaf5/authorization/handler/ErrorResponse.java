package io.github.UmbrellaLeaf5.authorization.handler;

import java.util.List;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ErrorResponse {
  private String message;
  private String code;
  private List<ValidationError> validationErrors;
}
