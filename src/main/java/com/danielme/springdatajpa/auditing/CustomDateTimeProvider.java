package com.danielme.springdatajpa.auditing;

import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAccessor;
import java.util.Optional;

@Component
public class CustomDateTimeProvider implements DateTimeProvider {
    private static final ZoneId zoneId = ZoneId.of("America/Mexico_City");

    @Override
    public Optional<TemporalAccessor> getNow() {
        return Optional.of(LocalDateTime.now(zoneId));
    }

}
