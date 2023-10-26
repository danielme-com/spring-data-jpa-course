package com.danielme.springdatajpa.repository.custom;

public interface CountryCustomRepository {

    boolean updateCountryPopulation(Long countryId, Integer population);

}
