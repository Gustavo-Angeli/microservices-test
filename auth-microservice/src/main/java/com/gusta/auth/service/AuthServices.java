package com.gusta.auth.service;

import com.gusta.auth.jwt.*;
import com.gusta.auth.model.entity.*;
import com.gusta.auth.model.entity.User;
import com.gusta.auth.model.vo.*;
import com.gusta.auth.repository.*;
import lombok.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AuthServices {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UserRepository repository;

    public String create(UserCredentialsVO data) {
        repository.save(
            User.builder()
                .username(data.getUsername())
                .password(new BCryptPasswordEncoder().encode(data.getPassword()))
                .permissions(List.of(Permission.builder().id(2L).build()))
                .build());
        return "a";
    }

    public TokenVO login(UserCredentialsVO data) {
        try {
            String username = data.getUsername();
            String password = data.getPassword();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

            User user = repository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Username " + username + " not found!"));

            return tokenProvider.createTokenVO(username, user.getRoles());
        } catch (Exception e) {
            throw new BadCredentialsException("Invalid username/password supplied!");
        }
    }

    public TokenVO refreshToken(String refreshJwt) {
        if (refreshJwt.startsWith("Bearer ")) {
            refreshJwt = refreshJwt.substring("Bearer ".length());
        }
        try {
            return tokenProvider.refreshTokenVO(refreshJwt);
        } catch (Exception e) {
            throw new BadCredentialsException("Invalid token supplied!");
        }
    }

    public boolean validateToken(String token) {
        return tokenProvider.validateToken(token);
    }

    public String getSubject(String token) {
        return tokenProvider.getSubject(token);
    }
}