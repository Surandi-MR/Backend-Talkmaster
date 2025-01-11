package com.talkmaster.talkmaster.service;

import com.talkmaster.talkmaster.model.InstructorTimeSlot;
import com.talkmaster.talkmaster.model.Users;
import com.talkmaster.talkmaster.repository.InstructorTimeSlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class InstructorTimeSlotService {

    @Autowired
    private InstructorTimeSlotRepository instructorTimeSlotRepository;

    @Autowired
    private UserService instructorService;

    // Create a new instructor time slot
    public InstructorTimeSlot createInstructorTimeSlot(InstructorTimeSlot instructorTimeSlot) {
        return instructorTimeSlotRepository.save(instructorTimeSlot);
    }

    // Get all instructor time slots for a specific instructor and date
    public List<InstructorTimeSlot> getInstructorTimeSlotsByInstructorAndDate(String instructorId, LocalDate date) {
        return instructorTimeSlotRepository.findByInstructorIdAndDate(instructorId, date);
    }

    // Get all available instructors for a specific date and time slot
    public List<Users> getAvailableInstructors(LocalDate date, String timeSlot) {
        List<InstructorTimeSlot> avaInstructorTimeSlots = instructorTimeSlotRepository.findByDateAndTimeSlotAndStatus(date, timeSlot, "available");
        List<Users> availableInstructors = avaInstructorTimeSlots.stream()
                .map(instructorTimeSlot -> instructorService.getUserById(instructorTimeSlot.getInstructorId()).get())
                .toList();
        return availableInstructors;
    }

    // Update an existing instructor time slot
    public InstructorTimeSlot updateInstructorTimeSlot(String id, InstructorTimeSlot instructorTimeSlotDetails) {
        return instructorTimeSlotRepository.findById(id).map(existingInstructorTimeSlot -> {
            if (instructorTimeSlotDetails.getInstructorId() != null) {
                existingInstructorTimeSlot.setInstructorId(instructorTimeSlotDetails.getInstructorId());
            }
            if (instructorTimeSlotDetails.getDate() != null) {
                existingInstructorTimeSlot.setDate(instructorTimeSlotDetails.getDate());
            }
            if (instructorTimeSlotDetails.getTimeSlot() != null) {
                existingInstructorTimeSlot.setTimeSlot(instructorTimeSlotDetails.getTimeSlot());
            }
            if (instructorTimeSlotDetails.getStatus() != null) {
                existingInstructorTimeSlot.setStatus(instructorTimeSlotDetails.getStatus());
            }
            if (instructorTimeSlotDetails.getScheduleId() != null) {
                existingInstructorTimeSlot.setScheduleId(instructorTimeSlotDetails.getScheduleId());
            }
            return instructorTimeSlotRepository.save(existingInstructorTimeSlot);
        }).orElseThrow(() -> new RuntimeException("Instructor time slot not found with id " + id));
    }

    // Delete an instructor time slot by ID
    public String deleteInstructorTimeSlot(String id) {
        return instructorTimeSlotRepository.findById(id).map(instructorTimeSlot -> {
            instructorTimeSlotRepository.deleteById(id);
            return "Delete success";
        }).orElse("Time slot not found");
    }
}
