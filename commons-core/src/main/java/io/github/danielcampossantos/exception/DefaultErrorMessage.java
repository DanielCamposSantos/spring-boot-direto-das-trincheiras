package io.github.danielcampossantos.exception;

public record DefaultErrorMessage(
        int status,
        String message
) {
}
