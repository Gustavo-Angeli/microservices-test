package com.gusta.auth.jwt;

import com.auth0.jwt.*;
import com.auth0.jwt.algorithms.*;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.auth0.jwt.interfaces.*;
import com.gusta.auth.model.vo.*;
import lombok.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.*;
import org.springframework.security.core.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.*;

import javax.annotation.*;
import javax.servlet.http.*;
import java.util.*;

@Service
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final UserDetailsService userDetailsService;
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
        Date expiresAt = new Date(issuedAt.getTime() + (validityInMilliseconds * 4));

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
        DecodedJWT decodedJWT = decodedToken(refreshJwt);
        String username = decodedJWT.getSubject();
        List<String> roles = decodedJWT.getClaim("roles").asList(String.class);
        return createTokenVO(username, roles);
    }

    public String resolveToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            return token.substring("Bearer ".length());
        } else if (token != null && !token.startsWith("Bearer ")) {
            return token;
        }
        return null;
    }

    public boolean validateToken(String token) {
        try {
            DecodedJWT decodedJWT = decodedToken(token);
            if (decodedJWT.getExpiresAt().before(new Date())) {
                return false;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getSubject(String token) {
        if (!validateToken(token)) {
            return "";
        }
        return decodedToken(token).getSubject();
    }

    private DecodedJWT decodedToken(String token) {
        Algorithm alg = Algorithm.HMAC512(secretKey.getBytes());
        JWTVerifier verifier = JWT.require(alg).build();
        return verifier.verify(token);
    }

    public Authentication getAuthentication(String token) {
        DecodedJWT decodedJWT = decodedToken(token);
        UserDetails userDetails = this.userDetailsService
                .loadUserByUsername(decodedJWT.getSubject());
        return new UsernamePasswordAuthenticationToken(
                userDetails.getUsername(),
                "",
                userDetails.getAuthorities());
    }
}
