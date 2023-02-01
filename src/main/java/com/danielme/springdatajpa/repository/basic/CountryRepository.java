package com.danielme.springdatajpa.repository.basic;

import com.danielme.springdatajpa.model.entity.Country;
import org.springframework.data.repository.Repository;

public interface CountryRepository extends Repository<Country, Long> {

}
