package com.talkmaster.talkmaster.dto;

import com.talkmaster.talkmaster.model.Session;
import com.talkmaster.talkmaster.model.UserPackage;
import com.talkmaster.talkmaster.model.Users;


public class GetUserWithDetails {
    private Users user;
    private Session[] sessions;
    private UserPackage[] packages;

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public Session[] getSessions() {
        return sessions;
    }

    public void setSessions(Session[] sessions) {
        this.sessions = sessions;
    }

    public UserPackage[] getPackages() {
        return packages;
    }

    public void setPackages(UserPackage[] packages) {
        this.packages = packages;
    }
    
}
