package com.talkmaster.talkmaster.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }

    public void sendPasswordEmail(String to, String password) {
        String subject = "Talk Master Account Password";
        String body = "Hello,\n\nA new Talk Master account has been created for you. Here is your generated password: " + password +
                    "\n\nPlease change your password after logging in.\n\nThank you!";
        sendEmail(to, subject, body);
    }
}
