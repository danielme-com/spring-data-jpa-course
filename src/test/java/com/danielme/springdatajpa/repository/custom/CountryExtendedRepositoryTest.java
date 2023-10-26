package com.danielme.springdatajpa.repository.custom;

import com.danielme.springdatajpa.model.entity.Country;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static com.danielme.springdatajpa.DatasetConstants.SPAIN_ID;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CountryExtendedRepositoryTest {

    @Autowired
    CountryExtendedRepository countryRepository;

    @Test
    void testUpdateCountryPopulation() {
        int newPopulation = 47_558_630;
        boolean wasUpdated = countryRepository.updateCountryPopulation(SPAIN_ID, newPopulation);

        assertThat(wasUpdated).isTrue();
        Optional<Country> spain = countryRepository.findById(SPAIN_ID);
        assertThat(spain)
                .isNotEmpty()
                .map(Country::getPopulation).get().isEqualTo(newPopulation);
    }

}
