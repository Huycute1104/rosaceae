package com.example.rosaceae.config;

import com.lib.payos.PayOS;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class PayOSConfig {

    @Value("${PAYOS_CLIENT_ID}")
    private String clientID;

    @Value("${PAYOS_API_KEY}")
    private String apiKey;

    @Value("${PAYOS_CHECKSUM_KEY}")
    private String checkSumKey;

    @Bean
    public PayOS payOSClient() {
        return new PayOS(clientID, apiKey, checkSumKey);
    }
}
