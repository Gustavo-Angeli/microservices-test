package com.gusta.auth.model.vo;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenVO {
    private String jwtToken;
    private String jwtRefreshToken;
}
