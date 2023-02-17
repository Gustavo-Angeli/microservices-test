package com.gusta.auth.model.vo;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TokenVO {
    private String jwtToken;
    private String jwtRefreshToken;
}
