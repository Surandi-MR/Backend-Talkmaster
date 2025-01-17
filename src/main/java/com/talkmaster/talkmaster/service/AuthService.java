package com.talkmaster.talkmaster.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import com.talkmaster.talkmaster.model.Users;
import com.talkmaster.talkmaster.repository.UserRepository;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;

@Service
public class AuthService {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private UserRepository userRepository;

    public Map<String, Object> verify(Users credentials) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(credentials.getEmail(), credentials.getPassword()));

        if (authentication.isAuthenticated()) {
            Users user = userRepository.findByEmail(credentials.getEmail());
            String token = jwtService.generateToken(credentials.getEmail());

            Map<String, Object> response = new HashMap<>();
            response.put("user", user);
            response.put("token", token);
            return response;
        }
        return Collections.singletonMap("status", "fail");
    }
}