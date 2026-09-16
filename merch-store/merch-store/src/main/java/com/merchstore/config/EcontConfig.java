package com.merchstore.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EcontConfig {

    @Value("${econt.base-url}")
    private String baseUrl;

    @Value("${econt.username}")
    private String username;

    @Value("${econt.password}")
    private String password;

    @Value("${econt.sender-office-code}")
    private String senderOfficeCode;

    @Value("${econt.sender-name}")
    private String senderName;

    @Value("${econt.sender-phone}")
    private String senderPhone;

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getSenderOfficeCode() {
        return senderOfficeCode;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getSenderPhone() {
        return senderPhone;
    }
}