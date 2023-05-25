package com.alash.bankapp.email.service;

import com.alash.bankapp.email.dto.EmailDetails;

public interface EmailService {

    String sendSimpleEmail(EmailDetails emailDetails);
    String sendEmailWithAttachment(EmailDetails emailDetails);
}
