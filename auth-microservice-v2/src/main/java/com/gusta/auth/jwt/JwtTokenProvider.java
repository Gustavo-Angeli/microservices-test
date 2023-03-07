package com.gusta.auth.jwt;

import com.auth0.jwt.*;
import com.auth0.jwt.algorithms.*;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.auth0.jwt.interfaces.*;
import com.gusta.auth.model.vo.*;
import jakarta.annotation.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
public class JwtTokenProvider {
    @Value("${security.jwt.token.secretKey}")
    private String secretKey;
    @Value("${security.jwt.token.validityInMilliseconds}")
    private Long validityInMilliseconds;

    @PostConstruct
    public void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    private String generateJwt(String username, List<String> roles) {
        Date issuedAt = new Date();
        Date expiresAt = new Date(issuedAt.getTime() + validityInMilliseconds);

        return JWT.create()
                .withClaim("roles", roles)
                .withIssuedAt(issuedAt)
                .withExpiresAt(expiresAt)
                .withSubject(username)
                .sign(Algorithm.HMAC512(secretKey))
                .strip();
    }

    private String generateRefreshJwt(String username, List<String> roles) {
        Date issuedAt = new Date();
        Date expiresAt = new Date(issuedAt.getTime() + (validityInMilliseconds * 2));

        return JWT.create()
                .withClaim("roles", roles)
                .withIssuedAt(issuedAt)
                .withExpiresAt(expiresAt)
                .withSubject(username)
                .sign(Algorithm.HMAC512(secretKey))
                .strip();
    }

    public TokenVO createTokenVO(String username, List<String> roles) {
        return TokenVO.builder()
                .jwtToken(generateJwt(username, roles))
                .jwtRefreshToken(generateRefreshJwt(username, roles))
                .build();
    }

    public TokenVO refreshTokenVO(String refreshJwt) {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC512(secretKey)).build();
        DecodedJWT decodedJWT = verifier.verify(refreshJwt);
        String username = decodedJWT.getSubject();
        List<String> roles = decodedJWT.getClaim("roles").asList(String.class);
        return createTokenVO(username, roles);
    }

    protected DecodedJWT decodedToken(String jwtToken) {
        Algorithm alg = Algorithm.HMAC512(secretKey.getBytes());
        JWTVerifier verifier = JWT.require(alg).build();
        DecodedJWT decodedJWT = null;
        try {
            decodedJWT = verifier.verify(jwtToken);
        } catch (Exception e) {
            return null;
        }
        return decodedJWT;
    }

    public boolean validateToken(String token) {
        DecodedJWT decodedJWT = decodedToken(token);

        if (decodedJWT == null) return false;

        return !decodedJWT.getExpiresAt().before(new Date());
    }

}
