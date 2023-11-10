package com.danielme.springdatajpa.auditing;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CustomAuditorAware implements AuditorAware<String> {

    public static final String MOCK_USER = "John Doe";

    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of(MOCK_USER);
    }

}
