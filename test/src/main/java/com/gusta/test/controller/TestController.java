package com.gusta.test.controller;

import com.gusta.test.exceptions.*;
import com.gusta.test.response.*;
import com.gusta.test.service.*;
import com.gusta.test.utils.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.*;

import static com.gusta.test.utils.ParamValidation.*;


@RestController
@RequestMapping(value = "/test")
public class TestController {

    @Autowired
    private TestService service;
    @Autowired
    private HttpServletRequest request;

    @PostMapping(value = "/login")
    public TokenVO login(@RequestBody UserCredentialsVO user) {
        checkIfIsNullOrBlankThrowingEx(user.getUsername(), user.getPassword());
        return service.login(user);
    }

    @GetMapping("/sum/{n1}/{n2}")
    public ResponseEntity<Double> sum(
            @PathVariable(value = "n1") Double n1,
            @PathVariable(value = "n2") Double n2,
            @RequestHeader(value = "Authorization") String jwtToken
            ) {
        checkIfIsNullOrBlankThrowingEx(n1, n2, jwtToken);

        if (service.validate(jwtToken) == false) throw new InvalidJWTException("Expired or Invalid JWT token");

        return new ResponseEntity<>(service.sum(n1, n2), HttpStatus.OK);
    }

}
