package io.github.danielcampossantos.docs;

import io.github.danielcampossantos.exception.ApiError;
import io.github.danielcampossantos.exception.DefaultErrorMessage;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.MediaType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@ApiResponse(
        description = "Bad Request",
        responseCode = "400",
        content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(oneOf = {ApiError.class, DefaultErrorMessage.class}),
                examples = {
                @ExampleObject(
                        name = "Unique field already exists",
                        value = """
                                {
                                  "timestamp": "2026-03-31T02:03:27.250Z",
                                  "status": 400,
                                  "error": "Bad Request",
                                  "message": "Email pauloalcantara@email.com already exists",
                                  "path": "/v1/users"
                                }
                                """
                ),
                        @ExampleObject(
                                name = "Invalid fields",
                                value = """
                                        {
                                           "timestamp": "2026-03-30T23:05:26.0629022-03:00",
                                           "status": 400,
                                           "error": "Bad Request",
                                           "messages": [
                                             {
                                               "field": "lastName",
                                               "message": "The field 'lastName' is required"
                                             },
                                             {
                                               "field": "firstName",
                                               "message": "The field 'firstName' is required"
                                             },
                                             {
                                               "field": "email",
                                               "message": "The e-mail is not valid"
                                             },
                                             {
                                               "field": "email",
                                               "message": "The field 'email' is required"
                                             }
                                           ],
                                           "path": "/v1/users"
                                         }
                                """
                        )
                }
        )

)
public @interface ApiBadResponses {

}
