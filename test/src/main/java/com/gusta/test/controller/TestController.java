package com.gusta.test.controller;

import com.gusta.test.proxy.*;
import com.gusta.test.response.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "test/")
public class TestController {

    @Autowired
    private TokenVOProxy proxy;

    @GetMapping(value = "hello")
    public TokenVO hello(@RequestBody UserCredentialsVO user) {
        TokenVO tokenVO = proxy.login(user);

        if (proxy.login(user) == null) System.out.println("nao foi");
        System.out.println("foi");

        return tokenVO;
    }

}
