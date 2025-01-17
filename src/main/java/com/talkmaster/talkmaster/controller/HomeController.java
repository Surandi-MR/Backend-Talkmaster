package com.talkmaster.talkmaster.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.csrf.CsrfToken;
@RestController
public class HomeController {

    @GetMapping("/")
    public String home( HttpServletRequest request ) {
        return "Welcome to TalkMaster!" + request.getSession().getId() ;
    }

    @GetMapping("/token")
    public CsrfToken getCsrfToken (HttpServletRequest request){
        return (CsrfToken) request.getAttribute("_csrf");
    }
}
