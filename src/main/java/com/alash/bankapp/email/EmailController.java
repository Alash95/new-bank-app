package com.alash.bankapp.email;

import com.alash.bankapp.email.dto.EmailDetails;
import com.alash.bankapp.email.service.EmailService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/email")
@Tag(
        name = "Email account service REST APIs/Endpoint",
        description = "Endpoints for manipulating Email Account"
)
public class EmailController {

    @Autowired
    EmailService emailService;

    @PostMapping("/sendMail")
    public String sendEmail(@RequestBody EmailDetails emailDetails){
        return emailService.sendSimpleEmail(emailDetails);
    }

    @PostMapping("sendEmailWithAttachment")
    public String sendEmailWithAttachment(@RequestBody EmailDetails emailDetails){
        return emailService.sendEmailWithAttachment(emailDetails);
    }
}
