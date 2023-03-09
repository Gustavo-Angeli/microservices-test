package com.gusta.bank.exceptions;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidValueException extends RuntimeException {

    public InvalidValueException(String str) {
        super(str);
    }

    public InvalidValueException() {
        super("Put a valid value!");
    }

}
