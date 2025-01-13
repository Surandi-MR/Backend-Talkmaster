package com.talkmaster.talkmaster.service;

import com.talkmaster.talkmaster.model.Users;
import com.talkmaster.talkmaster.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.talkmaster.talkmaster.model.UserPrincipal;
import java.util.List;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(10);

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = userRepository.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        return new UserPrincipal(user); 
    }

    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }

    public Users getUserById(String id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found with id " + id));
    }

    public Users getUserByEmail(String email) {
        return userRepository.findAll().stream()
                .filter(user -> email.equals(user.getEmail()))
                .findFirst()
                .orElse(null);
    }

    public void deleteUserById(String id) {
        userRepository.deleteById(id);
    }

    public Users updateUserById(String id, Users userDetails) {
        return userRepository.findById(id).map(user -> {
            if (userDetails.getFirstName() != null) {
                user.setFirstName(userDetails.getFirstName());
            }
            if (userDetails.getLastName() != null) {
                user.setLastName(userDetails.getLastName());
            }
            if (userDetails.getEmail() != null) {
                user.setEmail(userDetails.getEmail());
            }
            if (userDetails.getPhone_no() != null) {
                user.setPhone_no(userDetails.getPhone_no());
            }
            if (userDetails.getGender() != null) {
                user.setGender(userDetails.getGender());
            }
            if (userDetails.getPassword() != null) {
                user.setPassword(userDetails.getPassword());
            }
            if (userDetails.getRole() != null) {
                user.setRole(userDetails.getRole());
            }
            return userRepository.save(user);
        }).orElseThrow(() -> new RuntimeException("User not found with id " + id));
    }

    public Users createUser(Users user) {
        Users existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser != null) {
            throw new RuntimeException("User with email " + user.getEmail() + " already exists");
        }
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        Users savedUser = userRepository.save(user);
        savedUser.setPassword(null);
        return savedUser;
    }

    public String updatePassword(Users user){
         Users existingUser = userRepository.findByEmail(user.getEmail());
         existingUser.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
         userRepository.save(existingUser);
         return "password updated";
    }

    public List<Users> getUsersByRole(String role) {
        return userRepository.findByRole(role);
    }
}
