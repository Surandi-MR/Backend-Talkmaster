package com.talkmaster.talkmaster.repository;

import com.talkmaster.talkmaster.model.Session;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;

import java.util.List;

@Repository
public interface SessionRepository extends MongoRepository<Session, String> {
    // Find all sessions by studentId
    List<Session> findByStudentId(String studentId);

    // Find all sessions by instructorId
    List<Session> findByInstructorId(String instructorId);

    // Find all sessions by status
    List<Session> findByStatus(String status);

    // Find all sessions by studentId and instructorId
    List<Session> findByStudentIdAndInstructorId(String studentId, String instructorId);

    // Find all sessions by studentId and status
    List<Session> findByStudentIdAndStatus(String studentId, String status);

    // Find all sessions by instructorId and status
    List<Session> findByInstructorIdAndStatus(String instructorId, String status);

    // Find all sessions by studentId, instructorId, and status
    List<Session> findByStudentIdAndInstructorIdAndStatus(String studentId, String instructorId, String status);

    // Find all sessions by status and time
    List<Session> findByStatusAndTime(String status, LocalDateTime time);

    // Find all sessions by studentId,status, and time range
    List<Session> findByStudentIdAndStatusAndTimeBetween(String studentId, String status, LocalDateTime startTime, LocalDateTime endTime);

    // Find all sessions by instructorId, status, and time range
    List<Session> findByInstructorIdAndStatusAndTimeBetween(String instructorId, String status, LocalDateTime startTime, LocalDateTime endTime);

    // Find all sessions by studentId and time range
    List<Session> findByStudentIdAndTimeBetween(String studentId, LocalDateTime startTime, LocalDateTime endTime);

    // Find all sessions by instructorId and time range
    List<Session> findByInstructorIdAndTimeBetween(String instructorId, LocalDateTime startTime, LocalDateTime endTime);

    // Find all sessions by studentId, instructorId, status, and time range
    List<Session> findByStudentIdAndInstructorIdAndStatusAndTimeBetween(String studentId, String instructorId, String status, LocalDateTime startTime, LocalDateTime endTime);

}
