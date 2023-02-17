package com.gusta.test.proxy;

import com.gusta.test.response.*;
import org.springframework.cloud.openfeign.*;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "auth-microservice", url = "http://localhost:8000")
public interface UserProxy {

    @RequestMapping(value = "/auth/login", method = RequestMethod.GET)
    public String login(@RequestBody User user);

}