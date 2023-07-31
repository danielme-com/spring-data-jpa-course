package com.danielme.springdatajpa.repository.programmatic;


import com.danielme.springdatajpa.model.entity.Country;
import org.springframework.data.querydsl.ListQuerydslPredicateExecutor;
import org.springframework.data.repository.Repository;

public interface CountryQueryDSLRepository extends Repository<Country, Long>,
        ListQuerydslPredicateExecutor<Country> {

}
