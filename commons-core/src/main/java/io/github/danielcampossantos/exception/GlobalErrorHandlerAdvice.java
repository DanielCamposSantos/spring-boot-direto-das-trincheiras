package io.github.danielcampossantos.exception;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

@Log4j2
@RestControllerAdvice
public class GlobalErrorHandlerAdvice {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<DefaultErrorMessage> handleBadRequestException(BadRequestException e) {
        var error = new DefaultErrorMessage(HttpStatus.BAD_REQUEST.value(), e.getReason());
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<DefaultErrorMessage> handleSQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException e) {
        var error = new DefaultErrorMessage(HttpStatus.BAD_REQUEST.value(), "Duplicate entry for one of the unique fields");
        return ResponseEntity.badRequest().body(error);
    }

}
