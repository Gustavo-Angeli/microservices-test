package com.gusta.auth.model.vo;

import lombok.*;

@Data
@AllArgsConstructor
public class UserCredentialsVO {
    private String username;
    private String password;
}
