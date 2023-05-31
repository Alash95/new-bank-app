package com.alash.bankapp.sms.controller;

import com.alash.bankapp.sms.dto.SmsRequest;
import com.alash.bankapp.sms.service.SmsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sms/v1")
@Slf4j
public class SmsController {

    SmsService smsService;

    public SmsController(SmsService smsService) {
        this.smsService = smsService;
    }
    @PostMapping("/processSms")
    public String processSMS(@RequestBody SmsRequest request){
        return smsService.sendSMS(request);
    }


}
