package com.gusta.auth.exceptions;

import lombok.*;

import java.util.*;

@AllArgsConstructor
@Data
public class ExceptionResponse {
    private Date timeStamp;
    private String message;
    private String details;
}
