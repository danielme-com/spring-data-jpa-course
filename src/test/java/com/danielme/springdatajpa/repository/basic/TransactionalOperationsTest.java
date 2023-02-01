package com.danielme.springdatajpa.repository.basic;

import com.danielme.springdatajpa.model.entity.Country;
import com.danielme.springdatajpa.service.CountryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.BiConsumer;

import static com.danielme.springdatajpa.DatasetConstants.SPAIN_ID;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class TransactionalOperationsTest {

    @Autowired
    private CountryCrudRepository countryRepository;

    @Autowired
    private CountryService service;

    @Test
    @Transactional
    void testCountryIsAlwaysTheSameInTransaction() {
        Country spain1 = countryRepository.findById(SPAIN_ID).get();
        Country spain2 = countryRepository.findById(SPAIN_ID).get();

        assertThat(spain1).isEqualTo(spain2);
    }

    @Test
    void testCountryIsDifferentOutsideTransaction() {
        Country spain1 = countryRepository.findById(SPAIN_ID).get();
        Country spain2 = countryRepository.findById(SPAIN_ID).get();

        assertThat(spain1).isNotEqualTo(spain2);
    }

    @Test
    @Sql(value = {"/reset.sql", "/data.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void testUpdateInTransaction() {
        assertUpdateCountry(service::updateCountryNameTransactional);
    }

    @Test
    @Sql(value = {"/reset.sql", "/data.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void testUpdateNoTransaction() {
        assertUpdateCountry(service::updateCountryNameNoTransaction);
    }

    void assertUpdateCountry(BiConsumer<Long, String> consumer) {
        String newName = "test";

        consumer.accept(SPAIN_ID, newName);

        Country spain = countryRepository.findById(SPAIN_ID).get();
        assertThat(spain.getName()).isEqualTo(newName);
    }
}
