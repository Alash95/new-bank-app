package com.alash.bankapp.sms.service;

import com.alash.bankapp.sms.dto.SmsRequest;

public interface SmsService {

    String sendSMS(SmsRequest smsRequest);

}
