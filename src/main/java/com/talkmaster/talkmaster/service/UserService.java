package com.talkmaster.talkmaster.service;

import com.talkmaster.talkmaster.model.Users;
import com.talkmaster.talkmaster.repository.PackageRepository;
import com.talkmaster.talkmaster.repository.SessionRepository;
import com.talkmaster.talkmaster.repository.UserPackageRepository;
import com.talkmaster.talkmaster.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.apache.commons.lang3.RandomStringUtils;

import com.talkmaster.talkmaster.dto.GetUserWithDetails;
import com.talkmaster.talkmaster.dto.PasswordUpdateRequest;
import com.talkmaster.talkmaster.model.PackageModel;
import com.talkmaster.talkmaster.model.Session;
import com.talkmaster.talkmaster.model.UserPackage;
import com.talkmaster.talkmaster.model.UserPrincipal;
import java.util.List;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

     @Autowired
    private UserPackageRepository userPackageRepository;

     @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private PackageRepository packageRepository;

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

    public GetUserWithDetails getUserById(String id) {
    GetUserWithDetails userWithDetails = new GetUserWithDetails();
    
    // Get the user
    Users user = userRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("User not found with id " + id));
    userWithDetails.setUser(user);
    
    // Get sessions where the user is either a student or instructor
    List<Session> userSessions = sessionRepository.findByStudentIdOrInstructorId(id, id);
    
    // Convert List to Array and ensure student and instructor are populated
    Session[] sessions = userSessions.stream()
        .map(session -> {
            // Ensure student is populated
            if (session.getStudentId() != null && session.getStuddent() == null) {
                Users student = userRepository.findById(session.getStudentId())
                    .orElseThrow(() -> new RuntimeException("Student not found with id " + session.getStudentId()));
                session.setStuddent(student);
            }
            
            // Ensure instructor is populated
            if (session.getInstructorId() != null && session.getInstructor() == null) {
                Users instructor = userRepository.findById(session.getInstructorId())
                    .orElseThrow(() -> new RuntimeException("Instructor not found with id " + session.getInstructorId()));
                session.setInstructor(instructor);
            }
            
            return session;
        })
        .toArray(Session[]::new);
    userWithDetails.setSessions(sessions);
    
    // Get user packages
    List<UserPackage> userPackages = userPackageRepository.findByUserId(id);
    
    // Convert List to Array and ensure package details are populated
    UserPackage[] packages = userPackages.stream()
        .map(userPackage -> {
            // Ensure package details are populated
            if (userPackage.getPackageId() != null && userPackage.getPackageModel() == null) {
                PackageModel packageModel = packageRepository.findById(userPackage.getPackageId())
                    .orElseThrow(() -> new RuntimeException("Package not found with id " + userPackage.getPackageId()));
                userPackage.setPackageModel(packageModel);
            }
            return userPackage;
        })
        .toArray(UserPackage[]::new);
    userWithDetails.setPackages(packages);
    
    return userWithDetails;
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
            if (userDetails.getBirthday() != null) {
                user.setBirthday(userDetails.getBirthday());
            }
            if (userDetails.getRole() != null) {
                user.setRole(userDetails.getRole());
            }
            Users updatedUser = userRepository.save(user);
            updatedUser.setPassword(null);
            return updatedUser;
        }).orElseThrow(() -> new RuntimeException("User not found with id " + id));
    }

    public Users createUser(Users user) {
        Users existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser != null) {
            throw new RuntimeException("User with email " + user.getEmail() + " already exists");
        }

        // Generate a random password if none is provided
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            String generatedPassword = RandomStringUtils.random(8, true, true); // Generate 8-character random password
            user.setPassword(generatedPassword);

            emailService.sendPasswordEmail(user.getEmail(), generatedPassword);
        }

        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setCreatedAt(java.time.LocalDateTime.now());

        Users savedUser = userRepository.save(user);

        // Remove password before returning the response
        savedUser.setPassword(null);
        return savedUser;
    }

    public String updatePassword(String id, PasswordUpdateRequest passwordUpdateRequest) {
        Users existingUser = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found with id " + id));
        if (!bCryptPasswordEncoder.matches(passwordUpdateRequest.getOldPassword(), existingUser.getPassword())) {
            throw new RuntimeException("Old password is incorrect");
        }
        existingUser.setPassword(bCryptPasswordEncoder.encode(passwordUpdateRequest.getNewPassword()));
        userRepository.save(existingUser);
        return "Password updated successfully";
    }

    public List<Users> getUsersByRole(String role) {
        return userRepository.findByRole(role);
    }
}
