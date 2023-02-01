package com.danielme.springdatajpa.repository.basic;

import com.danielme.springdatajpa.model.entity.Confederation;
import com.danielme.springdatajpa.model.entity.Country;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;

import static com.danielme.springdatajpa.DatasetConstants.CONCACAF_ID;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ConfederationJpaRepositoryTest {

    @Autowired
    private CountryCrudRepository countryRepository;

    @Autowired
    private ConfederationJpaRepository confederationRepository;

    @Test
    @Sql(value = {"/reset.sql", "/data.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void testCreate() {
        Country country = new Country();
        country.setName("Cuba");
        country.setPopulation(11113215);
        country.setOcde(false);
        country.setCapital("Havana City");
        country.setUnitedNationsAdmission(LocalDate.of(1945, 10, 24));
        Confederation concacaf = confederationRepository.getReferenceById(CONCACAF_ID);
        country.setConfederation(concacaf);

        countryRepository.save(country);

        assertThat(country.getId()).isNotNull();
    }


}
