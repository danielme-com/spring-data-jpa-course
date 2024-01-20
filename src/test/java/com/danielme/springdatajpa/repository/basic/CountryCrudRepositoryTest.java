package com.danielme.springdatajpa.repository.basic;

import com.danielme.springdatajpa.model.entity.Confederation;
import com.danielme.springdatajpa.model.entity.Country;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;

import static com.danielme.springdatajpa.DatasetConstants.CONCACAF_ID;
import static com.danielme.springdatajpa.DatasetConstants.SPAIN_ID;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Sql(value = {"/reset.sql", "/data.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class CountryCrudRepositoryTest {

    @Autowired
    private CountryCrudRepository countryRepository;

    @Test
    void testCreate(@Autowired ConfederationCustomCrudRepository confederationRepository) {
        Country country = new Country();
        country.setName("Cuba");
        country.setPopulation(11113215);
        country.setOecd(false);
        country.setCapital("Havana City");
        country.setUnitedNationsAdmission(LocalDate.of(1945, 10, 24));
        Confederation concacaf = confederationRepository.findById(CONCACAF_ID).get();
        country.setConfederation(concacaf);

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
