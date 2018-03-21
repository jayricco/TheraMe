package com.therame.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.Map;

@RequestMapping("/api/auth")
@RestController
public class AuthenticationEndpoint {
    @GetMapping
    Map<String, Object> getToken(HttpSession session) {
        return Collections.singletonMap("session", session.getId());
    }
}
