package com.carmanagement.rental.infrastructure.config;

import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.circuitbreaker.event.CircuitBreakerOnStateTransitionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;

@Configuration
public class CircuitBreakerConfig {

    private static final Logger logger = LoggerFactory.getLogger(CircuitBreakerConfig.class);
    private final CircuitBreakerRegistry circuitBreakerRegistry;

    public CircuitBreakerConfig(CircuitBreakerRegistry circuitBreakerRegistry) {
        this.circuitBreakerRegistry = circuitBreakerRegistry;
    }

    @PostConstruct
    public void registerCircuitBreakerListeners() {
        // Listen to all circuit breakers
        circuitBreakerRegistry.getAllCircuitBreakers().forEach(circuitBreaker -> {
            circuitBreaker.getEventPublisher()
                    .onStateTransition(event -> {
                        if (event.getStateTransition().getToState().name().equals("OPEN")) {
                            logger.warn("⚠️ Circuit Breaker '{}' is now OPEN! Service may be down.",
                                    circuitBreaker.getName());
                        } else if (event.getStateTransition().getToState().name().equals("CLOSED")) {
                            logger.info("✅ Circuit Breaker '{}' is now CLOSED. Service is back to normal.",
                                    circuitBreaker.getName());
                        } else if (event.getStateTransition().getToState().name().equals("HALF_OPEN")) {
                            logger.info("🔄 Circuit Breaker '{}' is HALF_OPEN. Testing if service recovered.",
                                    circuitBreaker.getName());
                        }
                    });
        });
    }
}