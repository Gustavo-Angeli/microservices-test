package com.gusta.bank.proxy;

import com.gusta.bank.model.vo.*;
import org.springframework.cloud.openfeign.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "auth-microservice")
public interface AuthProxy {
    @RequestMapping(value = "/auth/create", method = RequestMethod.POST)
    public ResponseEntity create(@RequestBody UserCredentialsVO data);

    @RequestMapping(value = "/auth/validate-token", method = RequestMethod.GET)
    public boolean validateToken(@RequestHeader("Authorization") String token);

    @RequestMapping(value = "/auth/get-subject", method = RequestMethod.GET)
    public String getSubject(@RequestHeader("Authorization") String token);
}
