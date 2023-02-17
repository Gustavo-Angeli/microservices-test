package com.gusta.auth.model.vo;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserCredentialsVO {
    private String username;
    private String password;
}
