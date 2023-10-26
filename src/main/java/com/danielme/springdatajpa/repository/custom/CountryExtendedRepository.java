package com.danielme.springdatajpa.repository.custom;

import com.danielme.springdatajpa.model.entity.Country;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface CountryExtendedRepository extends Repository<Country,Long>, CountryCustomRepository {

    Optional<Country> findById(Long id);

}
