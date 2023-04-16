package com.danielme.springdatajpa.repository.query;

import com.danielme.springdatajpa.model.dto.*;
import com.danielme.springdatajpa.model.entity.Country;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static com.danielme.springdatajpa.DatasetConstants.*;
import static com.danielme.springdatajpa.SharedCountryAssertions.assertUefaCode;
import static com.danielme.springdatajpa.SharedCountryAssertions.assertUefaIdName;
import static com.danielme.springdatajpa.TestConstants.CITY_STRING;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CountryJpqlQueryRepositoryTest {

    @Autowired
    private CountryJpqlQueryRepository countryRepository;

    @Test
    void testFindByNameOrCapitalContaining() {
        List<Country> countries = countryRepository.findByNameOrCapitalContaining(CITY_STRING);

        assertThat(countries)
                .extracting(Country::getId)
                .containsExactly(GUATEMALA_ID, MEXICO_ID, VATICAN_ID);
    }

    @Test
    void testFindByNameOrCapital() {
        List<Country> countries = countryRepository.findByNameOrCapital('%' + CITY_STRING + '%');

        assertThat(countries)
                .extracting(Country::getId)
                .containsExactly(GUATEMALA_ID, MEXICO_ID, VATICAN_ID);
    }

    @Test
    void testFindByName() {
        List<Country> countries = countryRepository.findByName(CITY_STRING);

        assertThat(countries)
                .extracting(Country::getId)
                .containsExactly(VATICAN_ID);
    }

    @Test
    void testFindLongestCapitalName() {
        String longestCapitalName = countryRepository.findLongestCapitalName();

        assertThat(longestCapitalName)
                .isEqualTo(WASHINGTON);
    }

    @Test
    void testFindByCapitalNamedQuery() {
        List<Country> countries = countryRepository.findByCapitalUsingNameQuery(CITY_STRING);

        assertThat(countries)
                .extracting(Country::getId)
                .containsExactly(GUATEMALA_ID, MEXICO_ID, VATICAN_ID);
    }

    @Sql(value = {"/reset.sql", "/data.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    void testAppendConfederationToName() {
        int updated = countryRepository.appendConfederationToName(UEFA_NAME);

        assertThat(updated).isEqualTo(3);
        String spainNewName = countryRepository.findById(SPAIN_ID).get().getName();
        assertThat(spainNewName).isEqualTo(SPAIN_NAME + " (" + UEFA_NAME + ")");
    }

    @Test
    @Sql(value = {"/reset.sql", "/data.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void testDeleteByConfederationId() {
        long removed = countryRepository.deleteByConfederationId(UEFA_ID);

        assertThat(removed).isEqualTo(3);
    }

    @Test
    void testSearch() {
        CountrySearch countrySearch = new CountrySearch(40_000_000, 60_000_000, List.of(UEFA_ID));
        List<Country> countries = countryRepository.search(countrySearch);

        assertThat(countries)
                .extracting(Country::getId)
                .containsExactly(SPAIN_ID);
    }

    @Test
    void testFindIdNameAsRecordByConfederationId() {
        List<IdNameRecord> uefaCountries
                = countryRepository.findIdNameAsRecordByConfederationId(UEFA_ID);

        assertUefaIdName(uefaCountries);
    }

    @Test
    void testFindIdNameAsInterfaceByConfederationId() {
        List<IdName> uefaCountries
                = countryRepository.findIdNameAsInterfaceByConfederationId(UEFA_ID);

        assertUefaIdName(uefaCountries);
    }

    @Test
    void testFindIdNameConfederationByConfederationId() {
        List<CountryIdNameConfederationProjection> uefaCountries
                = countryRepository.findIdNameConfederationByConfederationId(UEFA_ID);

        assertUefaIdName(uefaCountries);
        assertThat(uefaCountries)
                .extracting("confederation.name")
                .containsExactly(UEFA_NAME, UEFA_NAME, UEFA_NAME);
    }

    @Test
    void testFindCountryCodeByConfederationId() {
        List<StringCode> uefaCountries
                = countryRepository.findCountryCodeByConfederationId(UEFA_ID);

        assertUefaCode(uefaCountries);
    }

    @Test
    void testFindAllSortByNameLength() {
        JpaSort sort = JpaSort.unsafe(Sort.Direction.DESC, "LENGTH(name)");
        List<Country> countries = countryRepository.findAll(sort);

        assertThat(countries.get(0)).extracting("id").isEqualTo(USA_ID);
    }

}
