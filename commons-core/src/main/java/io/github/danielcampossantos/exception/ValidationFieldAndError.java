package io.github.danielcampossantos.exception;

public record ValidationFieldAndError(
        String field,
        String message
) {
}
