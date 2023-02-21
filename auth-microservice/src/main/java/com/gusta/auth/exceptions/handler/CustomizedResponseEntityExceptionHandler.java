package com.gusta.auth.exceptions.handler;

import com.auth0.jwt.exceptions.*;
import com.gusta.auth.exceptions.*;
import org.springframework.http.*;
import org.springframework.security.authentication.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.*;
import org.springframework.web.servlet.mvc.method.annotation.*;

import java.util.*;

@ControllerAdvice
@RestController
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ExceptionResponse> handleAllExceptions(Exception ex, WebRequest request) {
        ExceptionResponse exResponse = new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(exResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({
            JWTVerificationException.class
            })
    public final ResponseEntity<ExceptionResponse> handleInvalidJwtAuthExceptions(Exception ex, WebRequest request) {
        ExceptionResponse exResponse = new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(exResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(
            {
                    NullPointerException.class,
                    IllegalArgumentException.class,
                    BadCredentialsException.class
            })
    public final ResponseEntity<ExceptionResponse> handleBadRequestExceptions(Exception ex, WebRequest request) {
        ExceptionResponse exResponse = new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(exResponse, HttpStatus.BAD_REQUEST);
    }
}
