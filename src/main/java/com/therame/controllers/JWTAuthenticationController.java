package com.therame.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JWTAuthenticationController {

    @RequestMapping(value = "/api/login", method = RequestMethod.POST)
    public @ResponseBody String getUsers() {
        return "Success";
    }

}
