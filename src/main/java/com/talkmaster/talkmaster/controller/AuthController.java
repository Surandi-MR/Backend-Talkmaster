package com.talkmaster.talkmaster.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.talkmaster.talkmaster.model.Users;
import com.talkmaster.talkmaster.service.AuthService;
import com.talkmaster.talkmaster.service.UserService;

@RestController
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public Map<String, Object>  login(@RequestBody Users user){

        System.out.println(user.getEmail());
        return authService.verify(user);

    }

    @PostMapping("/register")
    public Users register(@RequestBody Users user){
        user.setRole("student");
        try {
            return userService.createUser(user);
        } catch (Exception e) {
            if (e.getMessage().contains("already exists")) {
                throw new IllegalArgumentException("User with this email already exists");
            }
            throw new RuntimeException("An error occurred during registration");
        }
    }

}
