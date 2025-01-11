package com.talkmaster.talkmaster.controller;

import com.talkmaster.talkmaster.model.InstructorTimeSlot;
import com.talkmaster.talkmaster.model.Users;
import com.talkmaster.talkmaster.service.InstructorTimeSlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/instructor-time-slots")
public class InstructorTimeSlotController {

    @Autowired
    private InstructorTimeSlotService instructorTimeSlotService;

    // Create a new instructor time slot
    @PostMapping
    public InstructorTimeSlot createInstructorTimeSlot(@RequestBody InstructorTimeSlot instructorTimeSlot) {
        return instructorTimeSlotService.createInstructorTimeSlot(instructorTimeSlot);
    }

    // Get all instructor time slots for a specific instructor and date
    @GetMapping("/instructor/{instructorId}/date/{date}")
    public List<InstructorTimeSlot> getInstructorTimeSlotsByInstructorAndDate(@PathVariable String instructorId, @PathVariable String date) {
        LocalDate localDate = LocalDate.parse(date);
        return instructorTimeSlotService.getInstructorTimeSlotsByInstructorAndDate(instructorId, localDate);
    }

    // Get all available instructors for a specific date and time slot
    @GetMapping("/available-instructors/date/{date}/time-slot/{timeSlot}")
    public List<Users> getAvailableInstructors(@PathVariable String date, @PathVariable String timeSlot) {
        LocalDate localDate = LocalDate.parse(date);
        return instructorTimeSlotService.getAvailableInstructors(localDate, timeSlot);
    }

    // Update an existing instructor time slot
    @PutMapping("/{id}")
    public InstructorTimeSlot updateInstructorTimeSlot(@PathVariable String id, @RequestBody InstructorTimeSlot instructorTimeSlotDetails) {
       return instructorTimeSlotService.updateInstructorTimeSlot(id, instructorTimeSlotDetails);
       
    }

    // Delete an instructor time slot by ID
    @DeleteMapping("/{id}")
    public String deleteInstructorTimeSlot(@PathVariable String id) {
        return instructorTimeSlotService.deleteInstructorTimeSlot(id);
    }
}