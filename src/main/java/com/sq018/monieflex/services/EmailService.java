package com.sq018.monieflex.services;

public interface EmailService {
    public void sendEmail(String message, String subject, String sender, String recipient);
}
