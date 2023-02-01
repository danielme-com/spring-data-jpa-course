package com.danielme.springdatajpa.repository.programmatic;

import com.danielme.springdatajpa.SharedCountryAssertions;
import com.danielme.springdatajpa.model.dto.IdName;
import com.danielme.springdatajpa.model.entity.Country;
import com.danielme.springdatajpa.model.specification.CountrySpecifications;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.List;

import static com.danielme.springdatajpa.DatasetConstants.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CountrySpecificationRepositoryTest {

    @Autowired
    private CountrySpecificationRepository countryRepository;


    @Test
    void testFindAllNoRangeSpecification() {
        Specification<Country> dateRangeSpecification = CountrySpecifications.admissionDateRange(null, null);

        List<Country> countriesInRange = countryRepository.findAll(dateRangeSpecification, Sort.by("name"));

        assertThat(countriesInRange).hasSize(TOTAL_COUNTRIES);
    }

    @Test
    void testFindAllRangeDateToSpecification() {
        Specification<Country> dateRangeSpecification = CountrySpecifications
                .admissionDateRange(null, LocalDate.of(1945, 10, 24));

        List<Country> countriesInRange = countryRepository.findAll(dateRangeSpecification, Sort.by("name"));

        assertThat(countriesInRange)
                .extracting(Country::getId)
                .containsExactly(DOMINICAN_ID, USA_ID);
    }

    @Test
    void testFindAllUefaSpecification() {
        Specification<Country> uefaSpecification = CountrySpecifications.confederationId(UEFA_ID);

        List<Country> countriesUefa = countryRepository.findAll(uefaSpecification, Sort.by("name"));

        SharedCountryAssertions.assertUefaIdName(countriesUefa);
    }

    @Test
    void testFindAllNoConfederationSpecification() {
        Specification<Country> specification = CountrySpecifications.confederationId(null);

        List<Country> countries = countryRepository.findAll(specification, Sort.by("name"));

        assertThat(countries).hasSize(TOTAL_COUNTRIES);
    }

    @Test
    void testFindAllUefaSpecificationAsIdName() {
        Specification<Country> uefaSpecification = CountrySpecifications.confederationId(UEFA_ID);

        List<IdName> countriesUefa = countryRepository.findBy(uefaSpecification, q -> q.sortBy(Sort.by("name"))
                .as(IdName.class)
                .all());

        SharedCountryAssertions.assertUefaIdName(countriesUefa);
    }

    @Test
    void testFindAllUefaBefore1946() {
        Specification<Country> uefefaBefore46Spec = CountrySpecifications.confederationId(UEFA_ID)
                .and(CountrySpecifications.admissionDateRange(null, LocalDate.of(1945, 12, 31)));

        List<Country> countriesUefa = countryRepository.findAll(uefefaBefore46Spec, Sort.by("name"));

        assertThat(countriesUefa)
                .extracting(Country::getId)
                .containsExactly(NORWAY_ID, NETHERLANDS_ID);
    }

    @Test
    void testFindAllNotUefa() {
        Specification<Country> noUefaSpec = Specification.not(CountrySpecifications.confederationId(UEFA_ID));

        List<Country> countriesNotUefa = countryRepository.findAll(noUefaSpec);

        assertThat(countriesNotUefa)
                .hasSize(8);
    }

    @Test
    void testFindAllUefaOrConmembol() {
        Specification<Country> uefaOrConmmebolSpec = CountrySpecifications.confederationId(UEFA_ID).or(CountrySpecifications.confederationId(CONMEBOL_ID));

        List<Country> uefaOrConmmebolCountries = countryRepository.findAll(uefaOrConmmebolSpec);

        assertThat(uefaOrConmmebolCountries)
                .hasSize(5);
    }


}
