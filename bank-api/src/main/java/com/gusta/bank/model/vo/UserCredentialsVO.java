package com.gusta.bank.model.vo;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCredentialsVO {
    private String username;
    private String password;
}
