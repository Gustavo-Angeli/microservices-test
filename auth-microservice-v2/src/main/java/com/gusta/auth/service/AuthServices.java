package com.gusta.auth.service;

import com.gusta.auth.jwt.*;
import com.gusta.auth.model.vo.*;
import com.gusta.auth.repository.*;
import lombok.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.*;

@Service
@RequiredArgsConstructor
public class AuthServices {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UserRepository repository;

    public TokenVO login(UserCredentialsVO data) {
        try {
            var username = data.getUsername();
            var password = data.getPassword();
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));

            var user = repository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Username " + username + " not found!"));

            return tokenProvider.createTokenVO(username, user.getRoles());
        } catch (Exception e) {
            throw new BadCredentialsException("Invalid username/password supplied!");
        }
    }

    public TokenVO refreshToken(String refreshJwt) {
        try {
            return tokenProvider.refreshTokenVO(refreshJwt);
        } catch (Exception e) {
            throw new BadCredentialsException("Invalid token supplied!");
        }
    }

    public boolean validateToken(String jwtToken) {
        return tokenProvider.validateToken(jwtToken);
    }
}