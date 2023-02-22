package com.gusta.test.exceptions;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class InvalidJWTException extends IllegalArgumentException {

    public InvalidJWTException(String str) {
        super(str);
    }

}
