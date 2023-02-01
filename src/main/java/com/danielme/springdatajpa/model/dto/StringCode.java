package com.danielme.springdatajpa.model.dto;

import org.springframework.beans.factory.annotation.Value;

public interface StringCode {

    @Value("#{target.id + '-' + target.name}")
    String getCode();
}
