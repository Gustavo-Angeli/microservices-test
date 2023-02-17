package com.gusta.test.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserCredentialsVO {
    private String username;
    private String password;
}
