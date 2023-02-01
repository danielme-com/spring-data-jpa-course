package com.danielme.springdatajpa.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class IdNameDTO {

    private final Long id;

    private final String name;
}
