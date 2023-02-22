package com.gusta.test.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TokenVO {
    private String jwtToken;
    private String jwtRefreshToken;
}
