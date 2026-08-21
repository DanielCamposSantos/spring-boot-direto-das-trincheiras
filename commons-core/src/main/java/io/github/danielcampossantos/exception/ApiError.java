package io.github.danielcampossantos.exception;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


@Builder
@Data
@AllArgsConstructor
public class ApiError {

  private String timestamp;
  private int status;
  private String error;
  private List<ErrorMessages> messages;
  private String path;
}
