package com.gusta.bank.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RepeatedAccountException extends RuntimeException {

    public RepeatedAccountException(String str) {
        super(str);
    }
    public RepeatedAccountException() {
        super("This name is already being used, please try other name");
    }
}
