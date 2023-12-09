package com.sq018.monieflex.services;

import com.sq018.monieflex.payloads.ApiResponse;
public interface EmailService {
     void sendEmail(String message, String subject, String recipient);
}
