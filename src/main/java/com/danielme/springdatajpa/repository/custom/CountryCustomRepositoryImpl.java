package com.danielme.springdatajpa.repository.custom;

import com.danielme.springdatajpa.model.entity.Country;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

public class CountryCustomRepositoryImpl implements CountryCustomRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public boolean updateCountryPopulation(Long countryId, Integer population) {
        Country country = em.find(Country.class, countryId);
        if (country != null) {
            country.setPopulation(population);
            return true;
        }
        return false;
    }

}
