package com.gusta.test.controller;

import com.gusta.test.proxy.*;
import com.gusta.test.response.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "test/")
public class SayHelloController {

    @Autowired
    private UserProxy proxy;

    @GetMapping(value = "hello")
    public String hello(@RequestBody User user) {
        System.out.println(user.getUsername());

        if (proxy.login(user) == null) System.out.println("nao foi");
        System.out.println("foi");
        return "Hello " + user.getUsername();
    }

}
