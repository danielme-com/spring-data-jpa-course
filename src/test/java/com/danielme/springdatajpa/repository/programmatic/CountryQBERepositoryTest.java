package com.danielme.springdatajpa.repository.programmatic;

import com.danielme.springdatajpa.model.dto.IdName;
import com.danielme.springdatajpa.model.entity.Country;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Sort;

import java.util.List;

import static com.danielme.springdatajpa.DatasetConstants.*;
import static com.danielme.springdatajpa.TestConstants.CITY_STRING;
import static com.danielme.springdatajpa.TestConstants.REPUBLIC_STRING;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CountryQBERepositoryTest {

    @Autowired
    private CountryQBERepository countryRepository;

    @Test
    void testFindAllByName() {
        Country country = new Country();
        country.setName(REPUBLIC_STRING);
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase();
        Example<Country> countryExample = Example.of(country, matcher);

        List<Country> republics = countryRepository.findAll(countryExample, Sort.by("name"));

        assertThat(republics)
                .extracting(Country::getId)
                .containsExactly(KOREA_ID, DOMINICAN_ID);
    }

    @Test
    void testFindAllByNameOrCapital() {
        Country country = new Country();
        country.setName(REPUBLIC_STRING);
        country.setCapital(CITY_STRING);
        ExampleMatcher matcher = ExampleMatcher.matchingAny()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase();
        Example<Country> countryExample = Example.of(country, matcher);

        List<Country> republicsCountriesOrCapital = countryRepository.findAll(countryExample, Sort.by("name"));

        assertThat(republicsCountriesOrCapital)
                .extracting(Country::getId)
                .containsExactly(GUATEMALA_ID, MEXICO_ID, KOREA_ID, DOMINICAN_ID, VATICAN_ID);
    }

    @Test
    void testFindAllByNameOrCapitalWithCustomMatcher() {
        Country country = new Country();
        country.setName("republic");
        country.setCapital("city");
        ExampleMatcher matcher = ExampleMatcher.matchingAny()
                .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.startsWith())
                .withMatcher("capital", ExampleMatcher.GenericPropertyMatchers.contains())
                .withIgnoreCase();
        Example<Country> countryExample = Example.of(country, matcher);

        List<Country> republicsCountriesOrCapital = countryRepository.findAll(countryExample, Sort.by("name"));

        assertThat(republicsCountriesOrCapital)
                .extracting(Country::getId)
                .containsExactly(GUATEMALA_ID, MEXICO_ID, KOREA_ID, VATICAN_ID);
    }


    @Test
    void testFindByAsIdName() {
        Example<Country> countryExample = buildRepublicExample();

        List<IdName> republics = countryRepository.findBy(countryExample, q -> q.sortBy(Sort.by("name"))
                .as(IdName.class)
                .all());

        assertThat(republics)
                .extracting(IdName::getId)
                .containsExactly(KOREA_ID, DOMINICAN_ID);
    }

    @Test
    void testFindByFirstWithConfederation() {
        Example<Country> countryExample = buildRepublicExample();

        Country firstRepublic = countryRepository.findBy(countryExample,
                q -> q.sortBy(Sort.by("name"))
                        .project("confederation")
                        .firstValue());

        assertThat(firstRepublic.getConfederation().getName()).isEqualTo(AFC_NAME);
    }

    @Test
    void testFindByStream() {
        Example<Country> countryExample = buildRepublicExample();

        List<Long> republics = countryRepository.findBy(countryExample,
                q -> q.sortBy(Sort.by("name"))
                        .stream()
                        .map(Country::getId)
                        .toList());

        assertThat(republics)
                .containsExactly(KOREA_ID, DOMINICAN_ID);
    }

    private Example<Country> buildRepublicExample() {
        Country country = new Country();
        country.setName(REPUBLIC_STRING);
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase();
        return Example.of(country, matcher);
    }


}
