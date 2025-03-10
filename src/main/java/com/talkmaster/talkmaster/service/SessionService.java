package com.talkmaster.talkmaster.service;

import com.talkmaster.talkmaster.model.Session;
import com.talkmaster.talkmaster.model.UserPackage;
import com.talkmaster.talkmaster.model.Users; // Ensure this import is present
import com.talkmaster.talkmaster.repository.SessionRepository;
import com.talkmaster.talkmaster.repository.UserPackageRepository;
import com.talkmaster.talkmaster.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SessionService {

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private UserPackageRepository userPackageRepository;

    @Autowired
    private UserRepository userRepository;

    // Instructor Create a new session and make it available for students to book
    public Session createSession(Session session) {
        Session createdSession = sessionRepository.save(session);
        return createdSession;
    }

    // Get session by ID
    public Session getSessionById(String sessionId) {
        Session session = sessionRepository.findById(sessionId)
            .orElseThrow(() -> new RuntimeException("Session not found with id " + sessionId));
        // Populate student and instructor if they exist
        return populateSessionUsers(session);
    }

    public List<Session> getSessions(String studentId, String instructorId, String status, LocalDateTime startTime, LocalDateTime endTime) {
        List<Session> sessions;

        if (studentId != null && instructorId != null && status != null && startTime != null && endTime != null) {
            sessions = sessionRepository.findByStudentIdAndInstructorIdAndStatusAndTimeBetween(studentId, instructorId, status, startTime, endTime);
        } else if (studentId != null && status != null && startTime != null && endTime != null) {
            sessions = sessionRepository.findByStudentIdAndStatusAndTimeBetween(studentId, status, startTime, endTime);
        } else if (instructorId != null && status != null && startTime != null && endTime != null) {
            sessions = sessionRepository.findByInstructorIdAndStatusAndTimeBetween(instructorId, status, startTime, endTime);
        } else if (studentId != null && startTime != null && endTime != null) {
            sessions = sessionRepository.findByStudentIdAndTimeBetween(studentId, startTime, endTime);
        } else if (instructorId != null && startTime != null && endTime != null) {
            sessions = sessionRepository.findByInstructorIdAndTimeBetween(instructorId, startTime, endTime);
        } else if (studentId != null && instructorId != null && status != null) {
            sessions = sessionRepository.findByStudentIdAndInstructorIdAndStatus(studentId, instructorId, status);
        } else if (studentId != null && instructorId != null) {
            sessions = sessionRepository.findByStudentIdAndInstructorId(studentId, instructorId);
        } else if (studentId != null && status != null) {
            sessions = sessionRepository.findByStudentIdAndStatus(studentId, status);
        } else if (instructorId != null && status != null) {
            sessions = sessionRepository.findByInstructorIdAndStatus(instructorId, status);
        } else if (studentId != null) {
            sessions = sessionRepository.findByStudentId(studentId);
        } else if (instructorId != null) {
            sessions = sessionRepository.findByInstructorId(instructorId);
        } else if (status != null && startTime != null) {
            sessions = sessionRepository.findByStatusAndTime(status, startTime);
        } else {
            return List.of();
        }

        // Populate student and instructor details for each session
        return sessions.stream()
            .map(this::populateSessionUsers)
            .collect(Collectors.toList());
    }

    // Helper method to populate student and instructor details
    private Session populateSessionUsers(Session session) {
        // Populate student if studentId exists and student is not already set
        if (session.getStudentId() != null && session.getStuddent() == null) {
            userRepository.findById(session.getStudentId())
                .ifPresent(student -> session.setStuddent(student));
        }

        // Populate instructor if instructorId exists and instructor is not already set
        if (session.getInstructorId() != null && session.getInstructor() == null) {
            userRepository.findById(session.getInstructorId())
                .ifPresent(instructor -> session.setInstructor(instructor));
        }

        return session;
    }

    // Update a session
    public Session updateSession(String sessionId, Session sessionDetails) {
        return sessionRepository.findById(sessionId).map(session -> {
            if (sessionDetails.getTopic() != null) {
                session.setTopic(sessionDetails.getTopic());
            }
            if (sessionDetails.getStudentId() != null) {
                session.setStudentId(sessionDetails.getStudentId());
            }
            if (sessionDetails.getInstructorId() != null) {
                session.setInstructorId(sessionDetails.getInstructorId());
            }
            if (sessionDetails.getTime() != null) {
                session.setTime(sessionDetails.getTime());
            }
            if (sessionDetails.getStatus() != null) {
                session.setStatus(sessionDetails.getStatus());
            }
            if (sessionDetails.getMeetingLink() != null) {
                session.setMeetingLink(sessionDetails.getMeetingLink());
            }
            // Populate student and instructor after updating
            Session updatedSession = sessionRepository.save(session);
            return populateSessionUsers(updatedSession);
        }).orElseThrow(() -> new RuntimeException("Session not found with id: " + sessionId));
    }

    // Student Schedule a session
    public Session scheduleSession(String sessionId, Session sessionDetails) {
        UserPackage activePackage = userPackageRepository.findByUserIdAndRemainingSessionsGreaterThan(sessionDetails.getStudentId(), 0)
            .stream().findFirst().orElseThrow(() -> new RuntimeException("No active package found for user"));

        activePackage.setRemainingSessions(activePackage.getRemainingSessions() - 1);
        userPackageRepository.save(activePackage);

        return this.updateSession(sessionId, sessionDetails);
    }

    // Delete a session by ID
    public void deleteSession(String sessionId) {
        sessionRepository.deleteById(sessionId);
    }
}