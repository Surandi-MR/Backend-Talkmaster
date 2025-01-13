package com.talkmaster.talkmaster.service;

import com.talkmaster.talkmaster.model.Session;
import com.talkmaster.talkmaster.model.UserPackage;
import com.talkmaster.talkmaster.repository.SessionRepository;
import com.talkmaster.talkmaster.repository.UserPackageRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SessionService {

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private UserPackageRepository userPackageRepository;

    // Instructor Create a new session and make it available for students to book
    public Session createSession(Session session) {
        Session createdSession = sessionRepository.save(session);
        return createdSession;
    }

    // Get session by ID
    public Session getSessionById(String sessionId) {
        return sessionRepository.findById(sessionId).orElseThrow(() -> new RuntimeException("Session not found with id " + sessionId));    
    }


    public List<Session> getSessions(String studentId, String instructorId, String status, LocalDateTime startTime, LocalDateTime endTime) {
        if (studentId != null && instructorId != null && status != null && startTime != null && endTime != null) {
            return sessionRepository.findByStudentIdAndInstructorIdAndStatusAndTimeBetween(studentId, instructorId, status, startTime, endTime);
        } else if(studentId != null && status != null && startTime != null && endTime != null){
            return sessionRepository.findByStudentIdAndStatusAndTimeBetween(studentId, status, startTime, endTime);
        }else if(instructorId != null && status != null && startTime != null && endTime != null){
            return sessionRepository.findByInstructorIdAndStatusAndTimeBetween(instructorId, status, startTime, endTime);
        } else if( studentId != null && startTime != null && endTime != null){
            return sessionRepository.findByStudentIdAndTimeBetween(studentId, startTime, endTime);
        } else if( instructorId != null && startTime != null && endTime != null){
            return sessionRepository.findByInstructorIdAndTimeBetween(instructorId, startTime, endTime);
        }else if(studentId != null && instructorId != null && status!= null){
            return sessionRepository.findByStudentIdAndInstructorIdAndStatus(studentId, instructorId, status);
        }else if (studentId != null && instructorId != null) {
            return sessionRepository.findByStudentIdAndInstructorId(studentId, instructorId);
        } else if (studentId != null && status != null) {
            return sessionRepository.findByStudentIdAndStatus(studentId, status);
        } else if (instructorId != null && status != null) {
            return sessionRepository.findByInstructorIdAndStatus(instructorId, status);
        } else if (studentId != null) {
            return sessionRepository.findByStudentId(studentId);
        } else if (instructorId != null) {
            return sessionRepository.findByInstructorId(instructorId);
        } else if (status != null && startTime != null){
            return sessionRepository.findByStatusAndTime(status, startTime);
        }else {
            return List.of();
        }
        
    }

    // Update a session
    public Session updateSession(String sessionId, Session sessionDetails) {
        return sessionRepository.findById(sessionId).map(session -> {
            if(sessionDetails.getTopic() != null) {
                session.setTopic(sessionDetails.getTopic());
            }
            if(sessionDetails.getStudentId() != null) {
                session.setStudentId(sessionDetails.getStudentId());
            }
            if(sessionDetails.getInstructorId() != null) {
                session.setInstructorId(sessionDetails.getInstructorId());
            }
            if(sessionDetails.getTime() != null) {
                session.setTime(sessionDetails.getTime());
            }
            if(sessionDetails.getStatus() != null) {
                session.setStatus(sessionDetails.getStatus());
            }
            if(sessionDetails.getMeetingLink() != null) {
                session.setMeetingLink(sessionDetails.getMeetingLink());
            }
            return sessionRepository.save(session);
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
