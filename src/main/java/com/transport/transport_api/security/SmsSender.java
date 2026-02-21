package com.transport.transport_api.security;

import org.springframework.context.annotation.Bean;

public interface SmsSender {
    void sendSms(String phoneNumber, String message);
}
