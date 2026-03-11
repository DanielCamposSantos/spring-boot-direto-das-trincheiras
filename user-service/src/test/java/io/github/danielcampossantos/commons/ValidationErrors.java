package io.github.danielcampossantos.commons;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public enum ValidationErrors {

    FIRST_NAME_REQUIRED("The field 'firstName' is required", ValidationType.REQUIRED),
    LAST_NAME_REQUIRED("The field 'lastName' is required", ValidationType.REQUIRED),
    EMAIL_REQUIRED("The field 'email' is required", ValidationType.REQUIRED),
    INVALID_EMAIL("The e-mail is not valid", ValidationType.INVALID);

    private final String message;

    private final ValidationType validationType;


    private enum ValidationType {
        REQUIRED,
        INVALID,
    }

    public static List<String> allRequiredErrors() {
        return new ArrayList<>(Arrays.stream(ValidationErrors.values())
                .filter(e -> e.validationType.equals(ValidationType.REQUIRED))
                .map(validationErrors -> validationErrors.message)
                .toList());
    }

    public static List<String> invalidEmailErrors() {
        return Collections.singletonList(INVALID_EMAIL.message);
    }


}
