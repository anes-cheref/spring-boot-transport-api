package com.transport.transport_api.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ConsoleSmsSender implements SmsSender {

    @Override
    public void sendSms(String phoneNumber, String message) {
        System.out.println("---------------------------");
        System.out.println("SMS ENVOYÉ À : " + phoneNumber);
        System.out.println("CONTENU : " + message);
        System.out.println("---------------------------");
    }
}
