package com.danielme.springdatajpa.repository.procedure;

import com.danielme.springdatajpa.SharedCountryAssertions;
import com.danielme.springdatajpa.model.dto.IdName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;

import java.util.List;
import java.util.Map;

import static com.danielme.springdatajpa.DatasetConstants.UEFA_ID;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Sql(value = "/procedures.sql", config = @SqlConfig(separator = "^;"))
class CountryStoredProceduresRepositoryTest {

    private static final int UEFA_COUNTRIES = 3;

    @Autowired
    private CountryStoredProceduresRepository countryRepository;

    @Test
    void testCountCountriesByConfIdNamedProc() {
        Integer uefaCountries = countryRepository.countCountriesByConfederationId(UEFA_ID);

        assertThat(uefaCountries).isEqualTo(UEFA_COUNTRIES);
    }

    @Test
    void testCountCountriesByConfIdDbName() {
        Integer uefaCountries = countryRepository.count_countries_by_confederation_id(UEFA_ID);

        assertThat(uefaCountries).isEqualTo(UEFA_COUNTRIES);
    }

    @Test
    void testCountCountriesByConfIdNameAnnotation() {
        Integer uefaCountries = countryRepository.countCountries(UEFA_ID);

        assertThat(uefaCountries).isEqualTo(UEFA_COUNTRIES);
    }

    @Test
    void testCountCountriesAndPopulation() {
        Map<String, Integer> uefaStats = countryRepository.countCountriesAndPopulationByConfId(UEFA_ID);

        assertThat(uefaStats)
                .containsEntry("countries_count", UEFA_COUNTRIES)
                .containsKey("population");
    }

    @Test
    void testCountCountriesWithFunction() {
        Integer uefaCountries = countryRepository.countCountriesWithFunction(UEFA_ID);

        assertThat(uefaCountries).isEqualTo(UEFA_COUNTRIES);
    }

    @Test
    void testFindCountriesWithFunction() {
        List<IdName> uefaCountries = countryRepository.findCountriesWithFunction(UEFA_ID);

        SharedCountryAssertions.assertUefaIdName(uefaCountries);
    }

}