package io.github.danielcampossantos.exception;

import java.util.List;

public record ValidationErrorMessage(
        int status,
        String message,
        List<ValidationFieldAndError> errors
) {
}
