package com.talkmaster.talkmaster.repository;

import com.talkmaster.talkmaster.model.Users;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserRepository extends MongoRepository<Users, String> {

    // Fetch users by name
    List<Users> findByFirstName(String firstName);

    // Fetch users by role
    List<Users> findByRole(String role);

    // Fetch users by both name and role
    List<Users> findByFirstNameAndRole(String firstName, String role);

    // Fetch a user by email
    Users findByEmail(String email);
}
