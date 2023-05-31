package com.alash.bankapp.sms.service.serviceImpl;

import com.alash.bankapp.sms.dto.SmsRequest;
import com.alash.bankapp.sms.service.SmsService;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SmsServiceImpl implements SmsService {

    @Value("${TWILIO_ACCOUNT_SID}")
    String ACCOUNT_SID;

    @Value("${TWILIO_AUTH_TOKEN}")
    String AUTH_TOKEN;

    @Value("${TWILIO_OUTGOING_SMS_NUMBER}")
    String OUTGOING_SMS_NUMBER;

    @PostConstruct
    private void setup(){
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    }


    @Override
    public String sendSMS(SmsRequest smsRequest) {
        Message message = Message.creator(
                new PhoneNumber(smsRequest.getDestinationSmsNumber()),
                new PhoneNumber(OUTGOING_SMS_NUMBER),
                smsRequest.getSmsMessage()).create();
        return message.getStatus().toString();
    }
}
