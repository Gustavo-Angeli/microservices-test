package com.gusta.test.service;

import com.gusta.test.exceptions.*;
import com.gusta.test.proxy.*;
import com.gusta.test.response.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

@Service
public class TestService {

    @Autowired
    private TokenVOProxy proxy;

    public TokenVO login(UserCredentialsVO user) {
        return proxy.login(user);
    }

    public boolean validate(String jwtToken) {
        if (proxy.validateToken(jwtToken) == false) throw new InvalidJWTException("Expired or invalid JWT token!");
        return true;
    }

    public Double sum(Double n1, Double n2) {
        return n1+n2;
    }
}
