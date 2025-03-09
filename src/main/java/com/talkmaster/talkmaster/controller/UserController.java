package com.talkmaster.talkmaster.controller;

import com.talkmaster.talkmaster.model.Users;
import com.talkmaster.talkmaster.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.talkmaster.talkmaster.dto.GetUserWithDetails;
import com.talkmaster.talkmaster.dto.PasswordUpdateRequest;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/all")
    public List<Users> getUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public GetUserWithDetails getUserById(@PathVariable String id) {
        return userService.getUserById(id);
    }

    @GetMapping("/role/{role}")
    public List<Users> getUsersByRole(@PathVariable String role) {
        return userService.getUsersByRole(role);
    }

    @PostMapping("/create")
    public Users createUser(@RequestBody Users user) {
        return userService.createUser(user);
    }

    @PutMapping("/{id}")
    public Users updateUser(@PathVariable String id, @RequestBody Users userDetails) {
        return userService.updateUserById(id, userDetails);
    }

    @PutMapping("/update-password/{id}")
    public String updatePassword(@PathVariable String id, @RequestBody PasswordUpdateRequest passwordUpdateRequest) {
        return userService.updatePassword(id, passwordUpdateRequest);
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable String id) {
        userService.deleteUserById(id);
        return "User deleted successfully!";
    }
}
