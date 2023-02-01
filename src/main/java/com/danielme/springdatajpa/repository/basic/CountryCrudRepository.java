package com.danielme.springdatajpa.repository.basic;

import com.danielme.springdatajpa.model.entity.Country;
import org.springframework.data.repository.CrudRepository;

public interface CountryCrudRepository extends CrudRepository<Country, Long> {

}
