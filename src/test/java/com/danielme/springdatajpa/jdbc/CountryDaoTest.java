package com.danielme.springdatajpa.jdbc;

import com.danielme.springdatajpa.model.entity.Country;
import com.danielme.springdatajpa.repository.jdbc.CountryDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import static com.danielme.springdatajpa.DatasetConstants.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CountryDaoTest {

    @Autowired
    CountryDao countryDao;

    @Test
    void testFindByPopulationGreaterThanJdbc() {
        testFindByPopulationGreaterThan((population) -> countryDao.findByPopulationLessThanWithJdbc(population));
    }

    @Test
    void testFindByPopulationGreaterThanJpa() {
        testFindByPopulationGreaterThan((population) -> countryDao.findByPopulationLessThanWithJpa(population));
    }

    void testFindByPopulationGreaterThan(Function<Integer, List<Country>> daoFunction) {
        List<Country> countriesUnder5million = daoFunction.apply(5_000_000);

        assertThat(countriesUnder5million)
                .extracting(Country::getId)
                .containsExactly(COSTA_RICA_ID, VATICAN_ID);
    }

    @Test
    void testFindAllWithJpql() {
        testFindAllRange(countryDao::findAllWithJpql);
    }

    @Test
    void testFindAllByWithCriteria() {
        testFindAllRange(countryDao::findAllWithCriteria);
    }

    private void testFindAllRange(BiFunction<LocalDate, LocalDate, List<Country>> biFunction) {
        List<Country> countriesInRange = biFunction.apply(LocalDate.of(1950, 1, 1),
                null);

        assertThat(countriesInRange)
                .extracting(Country::getId)
                .containsExactly(KOREA_ID, SPAIN_ID);
    }

}
