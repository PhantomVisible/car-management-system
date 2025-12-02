package com.carmanagement.rental.infrastructure.config;

import com.carmanagement.rental.infrastructure.clients.FeignErrorDecoder;
import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {

    @Bean
    public FeignErrorDecoder errorDecoder() {
        return new FeignErrorDecoder();
    }

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL; // Logs request/response details
    }
}