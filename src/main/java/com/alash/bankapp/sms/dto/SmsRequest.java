package com.alash.bankapp.sms.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder

public class SmsRequest {

    private String destinationSmsNumber;
    private String smsMessage;
}
