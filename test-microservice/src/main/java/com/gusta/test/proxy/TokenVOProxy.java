package com.gusta.test.proxy;

import com.gusta.test.response.*;
import org.springframework.cloud.openfeign.*;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "auth-microservice", url = "http://localhost:8000")
public interface TokenVOProxy {

    @RequestMapping(value = "/auth/login", method = RequestMethod.POST)
    public TokenVO login(@RequestBody UserCredentialsVO credentialsVO);

    @RequestMapping(value = "/auth/validate-token", method = RequestMethod.GET)
    public boolean validateToken(@RequestHeader("Authorization") String jwtToken);

}