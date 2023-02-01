package com.danielme.springdatajpa.model.dto;

import com.danielme.springdatajpa.model.entity.Confederation;

public interface CountryIdNameConfederationProjection {

    Long getId();

    String getName();

    Confederation getConfederation();

}