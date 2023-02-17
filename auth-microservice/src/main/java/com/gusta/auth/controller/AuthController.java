package com.gusta.auth.controller;

import com.gusta.auth.model.vo.*;
import com.gusta.auth.service.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import static com.gusta.auth.utils.ParamValidation.checkIfIsNullOrBlankThrowingEx;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    AuthServices authServices;

    @PostMapping(value = "/login")
    public ResponseEntity login(@RequestBody UserCredentialsVO data) {
        checkIfIsNullOrBlankThrowingEx(data.getUsername(), data.getPassword());
        return ResponseEntity.ok(authServices.login(data));
    }

    @PutMapping(value = "/refresh/{username}")
    public ResponseEntity refreshToken(
            @PathVariable("username") String username,
           @RequestHeader("Authorization") String refreshToken
    ) {
        checkIfIsNullOrBlankThrowingEx(username, refreshToken);
        return ResponseEntity.ok(authServices.refreshToken(username, refreshToken));
    }

}
