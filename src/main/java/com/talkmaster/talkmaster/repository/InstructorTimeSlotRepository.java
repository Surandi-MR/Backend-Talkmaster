package com.talkmaster.talkmaster.repository;

import com.talkmaster.talkmaster.model.InstructorTimeSlot;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;

public interface InstructorTimeSlotRepository extends MongoRepository<InstructorTimeSlot, String> {
    // find all available instructor by date and timeslot
    List<InstructorTimeSlot> findByDateAndTimeSlotAndStatus(LocalDate date, String timeSlot, String status);

    // find all instructor time slots by instructorId and date
    List<InstructorTimeSlot> findByInstructorIdAndDate(String instructorId, LocalDate date);

}
