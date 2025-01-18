package com.talkmaster.talkmaster.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.talkmaster.talkmaster.enums.UserRole;

@Document(collection = "users")
public class Users {
    @Id
    private String id; 
    private String firstName;
    private String lastName;
    private String email;
    private String phone_no;
    private String gender;
    private String password;
    private UserRole role;
    private LocalDateTime created_at;

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() { return firstName; }

    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }

    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone_no() { return phone_no; }

    public void setPhone_no(String phone_no) { this.phone_no = phone_no; }

    public String getGender() { return gender; }

    public void setGender(String gender) { this.gender = gender; }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password;}

    public LocalDateTime getCreatedAt(){
        return created_at;
    }

    public void setCreatedAt(LocalDateTime created_at){
        this.created_at = created_at;
    }

}
