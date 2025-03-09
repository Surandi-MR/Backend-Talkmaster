package com.talkmaster.talkmaster.dto;

import com.talkmaster.talkmaster.model.Session;
import com.talkmaster.talkmaster.model.Users;

public class sessionDetails {
    private Session session;
    private Users student;
    private Users instructor;

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public Users getStudent() {
        return student;
    }

    public void setStudent(Users student) {
        this.student = student;
    }

    public Users getInstructor() {
        return instructor;
    }

    public void setInstructor(Users instructor) {
        this.instructor = instructor;
    }

}
