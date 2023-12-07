package com.sq018.monieflex.services.implementation;

import com.sq018.monieflex.payloads.ApiResponse;
import com.sq018.monieflex.services.EmailService;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Date;
@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImplementation implements EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String sender;

    @SneakyThrows
    @Override
    public void sendEmail(String message, String subject, String recipient) {
        log.info("mail setup");

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
        messageHelper.setSubject(subject);
        messageHelper.setFrom(sender);
        messageHelper.setTo(recipient);
        messageHelper.setText(message,true);
        messageHelper.setSentDate(new Date(System.currentTimeMillis()));
        mailSender.send(mimeMessage);

        log.info("mail sent");
    }
}