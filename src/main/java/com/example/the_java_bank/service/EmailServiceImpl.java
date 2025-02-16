package com.example.the_java_bank.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.example.the_java_bank.dto.EmailDetail;
import com.example.the_java_bank.service.impl.EmailService;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String emailAddress;

    @Override
    public void sendEmail(EmailDetail emailDetail) {

        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom(emailAddress);
            simpleMailMessage.setTo(emailDetail.getRecipient());
            simpleMailMessage.setText(emailDetail.getMessageBody());
            simpleMailMessage.setSubject(emailDetail.getSubject());

            javaMailSender.send(simpleMailMessage);

            System.out.println("Sent Mail Succesfully");

        } catch (MailException e) {
            throw new RuntimeException(e);
        }

    }

}
