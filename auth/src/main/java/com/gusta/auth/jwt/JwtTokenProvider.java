package com.gusta.auth.jwt;

import com.auth0.jwt.*;
import com.auth0.jwt.algorithms.*;
import com.auth0.jwt.interfaces.*;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.gusta.auth.model.vo.*;
import com.gusta.auth.service.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.*;

import javax.annotation.*;
import javax.servlet.http.*;
import java.util.*;

@Service
public class JwtTokenProvider {
    @Value("${security.jwt.token.secretKey}")
    private String secretKey;
    @Value("${security.jwt.token.validityInMilliseconds}")
    private Long validityInMilliseconds;
    @Autowired
    private CustomUserDetailsService userDetailsService;
    private Algorithm algorithm = null;

    @PostConstruct
    private void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        algorithm = Algorithm.HMAC256(secretKey.getBytes());
    }

    private String generateJwt(String username, List<String> roles) {
        Date issuedAt = new Date();
        Date expiresAt = new Date(issuedAt.getTime() + validityInMilliseconds);

        return JWT.create()
                .withClaim("roles", roles)
                .withIssuedAt(issuedAt)
                .withExpiresAt(expiresAt)
                .withSubject(username)
                .sign(algorithm)
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
                .sign(algorithm)
                .strip();
    }

    public TokenVO createTokenVO(String username, List<String> roles) {
        return TokenVO.builder()
                .jwtToken(generateJwt(username, roles))
                .jwtRefreshToken(generateRefreshJwt(username, roles))
                .build();
    }

    public TokenVO refreshTokenVO(String refreshJwt) {
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(refreshJwt);
        String username = decodedJWT.getSubject();
        List<String> roles = decodedJWT.getClaim("roles").asList(String.class);
        return createTokenVO(username, roles);
    }

    private DecodedJWT decodedToken(String jwtToken) {
        Algorithm alg = Algorithm.HMAC256(secretKey.getBytes());
        JWTVerifier verifier = JWT.require(alg).build();
        return verifier.verify(jwtToken);
    }

    public Authentication getAuthentication(String jwtToken) {
        DecodedJWT decodedJWT = decodedToken(jwtToken);
        UserDetails userDetails = this.userDetailsService
                .loadUserByUsername(decodedJWT.getSubject());
        return new UsernamePasswordAuthenticationToken(
                userDetails.getUsername(),
                null,
                userDetails.getAuthorities());
    }

    public String resolveToken(HttpServletRequest req) {
        String token = req.getHeader("Authorization");

        if (token == null) return null;

        return token;
    }

    public boolean validateToken(String token) {
        try {
            DecodedJWT decodedJWT = decodedToken(token);
            return !decodedJWT.getExpiresAt().before(new Date());
        } catch (Exception e) {
            //ToDo exception
            throw new IllegalArgumentException("AAA");
        }
    }

}
