package io.github.danielcampossantos.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;


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
