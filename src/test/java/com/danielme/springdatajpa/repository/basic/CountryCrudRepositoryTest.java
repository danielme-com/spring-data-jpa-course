package com.danielme.springdatajpa.repository.basic;

import com.danielme.springdatajpa.model.entity.Confederation;
import com.danielme.springdatajpa.model.entity.Country;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static com.danielme.springdatajpa.DatasetConstants.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Sql(value = {"/reset.sql", "/data.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class CountryCrudRepositoryTest {

    @Autowired
    private CountryCrudRepository countryRepository;

    @Test
    void testCreate(@Autowired ConfederationCustomCrudRepository confederationRepository) {
        Country country = new Country();
        country.setName("Republic of India");
        country.setPopulation(1_437_375_657);
        country.setOecd(false);
        country.setCapital("New Delhi");
        country.setUnitedNationsAdmission(LocalDate.of(1945, 10, 24));
        //Confederation afc = confederationRepository.findById(AFC_ID).get();
        //In this example, calling getReferenceById saves a SELECT
        Confederation afc = confederationRepository.getReferenceById(AFC_ID);
        country.setConfederation(afc);

        countryRepository.save(country);

        assertThat(country.getId()).isNotNull();
    }

    @Test
    void testUpdatePopulation() {
        Country country = countryRepository.findById(SPAIN_ID).get();
        int newPopulation = 47432805;
        country.setPopulation(newPopulation);

        countryRepository.save(country);

        Country countryAfterSave = countryRepository.findById(SPAIN_ID).get();
        assertThat(countryAfterSave.getPopulation()).isEqualTo(newPopulation);
    }

}
