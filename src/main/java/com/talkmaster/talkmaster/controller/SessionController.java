package com.talkmaster.talkmaster.controller;

import com.talkmaster.talkmaster.model.Session;
import com.talkmaster.talkmaster.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/sessions")
public class SessionController {

    @Autowired
    private SessionService sessionService;

    // Create a new session
    @PostMapping
    public Session createSession(@RequestBody Session session) {
        return sessionService.createSession(session);
    }

    // Get session by ID
    @GetMapping("/{id}")
    public Session getSessionById(@PathVariable String id) {
        return sessionService.getSessionById(id);
    }

    // Get sessions within a date range
    @GetMapping
    public List<Session> getSessions(
            @RequestParam(value = "studentId", required = false) String studentId, 
            @RequestParam(value = "instructorId", required = false) String instructorId, 
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "startTime", required = false) LocalDateTime startTime,
            @RequestParam(value = "endTime", required = false) LocalDateTime endTime) {
        return sessionService.getSessions(studentId, instructorId, status, startTime, endTime);
    }
    

    // Update a session
    @PutMapping("/{id}")
    public Session updateSession(
            @PathVariable String id, 
            @RequestBody Session sessionDetails) {
        return sessionService.updateSession(id, sessionDetails);
    }

    // Schedule a session
    @PutMapping("/schedule/{id}")
    public Session bookSession(
        @PathVariable String id,
        @RequestBody Session sessionDetails) {
        return sessionService.scheduleSession(id, sessionDetails);
    }

    // Delete a session by ID
    @DeleteMapping("/{id}")
    public void deleteSession(@PathVariable String id) {
        sessionService.deleteSession(id);
    }
}
