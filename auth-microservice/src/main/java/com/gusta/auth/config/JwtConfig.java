package com.gusta.auth.config;

import com.gusta.auth.jwt.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.security.config.annotation.*;
import org.springframework.security.config.annotation.web.builders.*;
import org.springframework.security.web.*;
import org.springframework.security.web.authentication.*;

public class JwtConfig extends
        SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    @Autowired
    private JwtTokenProvider tokenProvider;

    public JwtConfig(JwtTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        JwtTokenFilter customFilter = new JwtTokenFilter(tokenProvider);
        http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
    }
}