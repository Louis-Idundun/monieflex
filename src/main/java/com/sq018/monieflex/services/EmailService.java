package com.sq018.monieflex.services;

import com.sq018.monieflex.payloads.ApiResponse;
public interface EmailService {
     ApiResponse<String> sendEmail(String message, String subject, String recipient);
}
