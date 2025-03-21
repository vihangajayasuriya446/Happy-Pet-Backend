package com.example.happypet.util;

import lk.pubudu.app.service.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EMailSender {

    private final Environment env;
    private final JavaMailSender javaMailSender;

    @Autowired
    public EMailSender(Environment env, JavaMailSender javaMailSender) {
        this.env = env;
        this.javaMailSender = javaMailSender;
    }

    public void sendMail(String email, String subject, String message) {
        try {
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setTo(email);
            mail.setFrom(env.getProperty("spring.mail.username"));
            mail.setSubject(subject);
            mail.setText(message);
            javaMailSender.send(mail);
        } catch (Exception e) {
            e.printStackTrace();
            throw new NotFoundException("Invalid email id");
        }
    }
}
