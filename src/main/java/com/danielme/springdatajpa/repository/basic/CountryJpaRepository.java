package com.danielme.springdatajpa.repository.basic;

import com.danielme.springdatajpa.model.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryJpaRepository extends JpaRepository<Country, Long> {

}
