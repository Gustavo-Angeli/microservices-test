package com.gusta.auth.controller;

import com.gusta.auth.model.vo.*;
import com.gusta.auth.service.*;
import lombok.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import static com.gusta.auth.utils.ParamValidation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthServices authServices;

    @PostMapping(value = "/login")
    public ResponseEntity login(@RequestBody UserCredentialsVO data) {
        checkIfIsNullOrBlankThrowingEx(data.getUsername(), data.getPassword());
        return ResponseEntity.ok(authServices.login(data));
    }

    @PutMapping(value = "/refresh")
    public ResponseEntity refreshToken(
            @RequestHeader("Authorization") String refreshToken
    ) {
        checkIfIsNullOrBlankThrowingEx(refreshToken);
        return ResponseEntity.ok(authServices.refreshToken(refreshToken));
    }

    @GetMapping(value = "/validate-token")
    public ResponseEntity<Boolean> validateToken(
            @RequestHeader("Authorization") String jwtToken
    ) {
        return ResponseEntity.ok(authServices.validateToken(jwtToken));
    }

}
