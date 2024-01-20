package com.danielme.springdatajpa.repository.query;

import com.danielme.springdatajpa.model.dto.IdName;
import com.danielme.springdatajpa.model.dto.IdNameDTO;
import com.danielme.springdatajpa.model.dto.IdNameRecord;
import com.danielme.springdatajpa.model.dto.StringCode;
import com.danielme.springdatajpa.model.entity.Country;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.danielme.springdatajpa.DatasetConstants.*;
import static com.danielme.springdatajpa.SharedCountryAssertions.assertUefaCode;
import static com.danielme.springdatajpa.SharedCountryAssertions.assertUefaIdName;
import static com.danielme.springdatajpa.TestConstants.CITY_STRING;
import static com.danielme.springdatajpa.TestConstants.REPUBLIC_STRING;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CountryDerivedQueryRepositoryTest {

    @Autowired
    private CountryDerivedQueryRepository countryRepository;

    @Test
    @Transactional(readOnly = true)
    void testFindByNameContainingAsStream() {
        List<Country> republics;

        try (Stream<Country> stream = countryRepository.findAsStreamByNameContainingOrderByNameAsc("Republic")) {
            republics = stream.toList();
        }

        assertThat(republics)
                .extracting(Country::getId)
                .containsExactly(KOREA_ID, DOMINICAN_ID);
    }

    @Test
    void testFindByNameContainingAndCapitalContaining() {
        List<Country> republicsConcacaf = countryRepository.findByNameContainingAndConfederationId(REPUBLIC_STRING, CONCACAF_ID);

        assertThat(republicsConcacaf)
                .extracting(Country::getId)
                .containsExactly(DOMINICAN_ID);
    }

    @Test
    void testFindByUnitedNationsAdmissionAfter() {
        List<Country> countriesUnAfter90 = countryRepository.findByUnitedNationsAdmissionAfter(LocalDate.of(1990, 1, 1));

        assertThat(countriesUnAfter90)
                .extracting(Country::getId)
                .containsExactly(KOREA_ID);
    }

    @Test
    void testFindByUnitedNationsAdmissionBefore() {
        List<Country> countriesUnBeforeNov45 = countryRepository.findByUnitedNationsAdmissionBefore(LocalDate.of(1945, 11, 1));

        assertThat(countriesUnBeforeNov45)
                .extracting(Country::getId)
                .containsExactlyInAnyOrder(USA_ID, DOMINICAN_ID, PERU_ID);
    }

    @Test
    void testFindByUnitedNationsAdmissionBetween() {
        List<Country> countriesUn50 = countryRepository.findByUnitedNationsAdmissionBetween(LocalDate.of(1950, 1, 1),
                LocalDate.of(1959, 12, 31));

        assertThat(countriesUn50)
                .extracting(Country::getId)
                .containsExactly(SPAIN_ID);
    }

    @Test
    void testFindByPopulationLessThan() {
        List<Country> countriesUnder10million = countryRepository.findByPopulationLessThan(10_000_000);

        assertThat(countriesUnder10million)
                .extracting(Country::getId)
                .containsExactlyInAnyOrder(NORWAY_ID, COSTA_RICA_ID, VATICAN_ID);
    }

    @Test
    void testFindByPopulationGreaterThan() {
        List<Country> countriesUnder100million = countryRepository.findByPopulationGreaterThan(100_000_000);

        assertThat(countriesUnder100million)
                .extracting(Country::getId)
                .containsExactlyInAnyOrder(MEXICO_ID, USA_ID);
    }

    @Test
    void testFindByUnitedNationsAdmissionIsNull() {
        List<Country> countriesNotUn = countryRepository.findByUnitedNationsAdmissionIsNull();

        assertThat(countriesNotUn)
                .extracting(Country::getId)
                .containsExactly(VATICAN_ID);
    }

    @Test
    void testFindByUnitedNationsAdmissionIsNotNull() {
        List<Country> countriesUn = countryRepository.findByUnitedNationsAdmissionIsNotNull();

        assertThat(countriesUn)
                .extracting(Country::getId)
                .doesNotContain(VATICAN_ID);
    }

    @Test
    void testFindByUnitedNationsAdmissionEquals() {
        List<Country> countriesUAdmitted = countryRepository.findByUnitedNationsAdmissionEquals(LocalDate.of(1945, 11, 7));

        assertThat(countriesUAdmitted)
                .extracting(Country::getId)
                .containsExactly(MEXICO_ID);
    }

    @Test
    void testFindByConfederationIdNot() {
        List<Country> countriesNotConmebol = countryRepository.findByConfederationIdNot(CONMEBOL_ID);

        assertThat(countriesNotConmebol)
                .extracting(Country::getId)
                .doesNotContain(COLOMBIA_ID, PERU_ID);
    }

    @Test
    void testFindByCapitalLike() {
        List<Country> countriesCapitalCity = countryRepository.findByCapitalLike("%City%");

        assertThat(countriesCapitalCity)
                .extracting(Country::getId)
                .containsExactlyInAnyOrder(MEXICO_ID, GUATEMALA_ID, VATICAN_ID);
    }

    @Test
    void testFindByCapitalContaining() {
        List<Country> countriesCapitalCity = countryRepository.findByCapitalContaining(CITY_STRING);

        assertThat(countriesCapitalCity)
                .extracting(Country::getId)
                .containsExactlyInAnyOrder(MEXICO_ID, GUATEMALA_ID, VATICAN_ID);
    }

    @Test
    void testFindByNameStartingWith() {
        List<Country> countriesStartsThe = countryRepository.findByNameStartingWith("The");

        assertThat(countriesStartsThe)
                .extracting(Country::getId)
                .containsExactlyInAnyOrder(NETHERLANDS_ID, DOMINICAN_ID);
    }

    @Test
    void testFindByNameEndingWith() {
        List<Country> countriesCapitalCity = countryRepository.findByCapitalEndingWith(CITY_STRING);

        assertThat(countriesCapitalCity)
                .extracting(Country::getId)
                .containsExactlyInAnyOrder(MEXICO_ID, GUATEMALA_ID, VATICAN_ID);
    }

    @Test
    void testFindByConfederationIn() {
        List<Country> countriesInConfs = countryRepository.findByConfederationIdIn(List.of(CONMEBOL_ID, AFC_ID));

        assertThat(countriesInConfs)
                .extracting(Country::getId)
                .containsExactlyInAnyOrder(COLOMBIA_ID, PERU_ID, KOREA_ID);
    }

    @Test
    void testFindByOecdFalse() {
        List<Country> countriesNotOecd = countryRepository.findByOecdFalse();

        assertThat(countriesNotOecd)
                .extracting(Country::getId)
                .containsExactlyInAnyOrder(DOMINICAN_ID, PERU_ID, GUATEMALA_ID, VATICAN_ID);
    }

    @Test
    void testFindByCapitalIgnoreCase() {
        Optional<Country> spain = countryRepository.findByCapitalIgnoreCase("madrid");

        assertThat(spain).isNotEmpty();
        assertThat(spain.get().getId()).isEqualTo(SPAIN_ID);
    }

    @Test
    void testFindByNameContainingOrCapitalContaining() {
        List<Country> countriesByString = countryRepository.findByNameContainingOrCapitalContaining(REPUBLIC_STRING, CITY_STRING);

        assertThat(countriesByString)
                .extracting(Country::getId)
                .containsExactlyInAnyOrder(MEXICO_ID, GUATEMALA_ID, VATICAN_ID, KOREA_ID, DOMINICAN_ID);
    }

    @Test
    void testMethodNameTooLong() {
        List<Country> countriesByString = countryRepository.findByNameContainingIgnoringCaseOrCapitalIgnoringCaseContainingOrderByName(REPUBLIC_STRING.toLowerCase(), CITY_STRING.toUpperCase());

        assertThat(countriesByString)
                .extracting(Country::getId)
                .containsExactly(GUATEMALA_ID, MEXICO_ID, KOREA_ID, DOMINICAN_ID, VATICAN_ID);
    }

    @Test
    void testFindByNameContainingIgnoreCase() {
        List<Country> republics = countryRepository.findByNameContainingIgnoreCase(REPUBLIC_STRING.toLowerCase());

        assertThat(republics)
                .extracting(Country::getId)
                .containsExactly(KOREA_ID, DOMINICAN_ID);
    }

    @Test
    void testFindByOrderByUnitedNationsAdmissionAscNameAsc() {
        List<Country> republics = countryRepository.findByOrderByUnitedNationsAdmissionAscNameAsc();

        assertThat(republics.subList(0, 3))
                .extracting(Country::getId)
                .containsExactly(VATICAN_ID, DOMINICAN_ID, USA_ID);
    }

    @Test
    void testFindTopOrderByPopulationAsc() {
        List<Country> less3PopulatedCountries = countryRepository.findTop3ByOrderByPopulationAsc();

        assertThat(less3PopulatedCountries)
                .extracting(Country::getId)
                .containsExactly(VATICAN_ID, COSTA_RICA_ID, NORWAY_ID);
    }

    @Test
    void testFindLimitOrderByPopulationAsc() {
        List<Country> less3PopulatedCountries = countryRepository.findByOrderByPopulationAsc(Limit.of(3));

        assertThat(less3PopulatedCountries)
                .extracting(Country::getId)
                .containsExactly(VATICAN_ID, COSTA_RICA_ID, NORWAY_ID);
    }

    @Test
    void testCountByConfederationId() {
        long uefaMembers = countryRepository.countByConfederationId(UEFA_ID);

        assertThat(uefaMembers).isEqualTo(3);
    }

    @Test
    void testExistsByCapitalIgnoringCase() {
        assertThat(countryRepository.existsByCapitalIgnoringCase("lima")).isTrue();
    }

    @Test
    @Sql(value = {"/reset.sql", "/data.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void testDeleteByConfederationId() {
        long removed = countryRepository.deleteByConfederationId(UEFA_ID);

        assertThat(removed).isEqualTo(3);
        assertThat(countryRepository.countByConfederationId(UEFA_ID)).isZero();
    }

    @Test
    void testFindAsyncCountriesByNameContaining() throws ExecutionException, InterruptedException {
        Future<List<Country>> republicsAsyncList = countryRepository.findAsyncCountriesByNameContaining(REPUBLIC_STRING);

        assertThat(republicsAsyncList.get())
                .extracting(Country::getId)
                .containsExactlyInAnyOrder(KOREA_ID, DOMINICAN_ID);
    }

    @Test
    void testFindIdNameAsInterfaceByConfederationId() {
        List<IdName> uefaCountries
                = countryRepository.findIdNameAsInterfaceByConfederationId(UEFA_ID);

        assertThat(uefaCountries)
                .extracting(IdName::getId)
                .containsExactlyInAnyOrder(SPAIN_ID, NORWAY_ID, NETHERLANDS_ID);
    }

    @Test
    void testFindIdNameAsRecordByConfederationId() {
        List<IdNameRecord> uefaCountries
                = countryRepository.findIdNameAsRecordByConfederationId(UEFA_ID);

        assertUefaIdName(uefaCountries);
    }

    @ParameterizedTest
    @ValueSource(classes = {IdName.class, IdNameDTO.class, IdNameRecord.class, Country.class})
    void testFindDynamicProjectionByConfederationId(Class projectionClass) {
        List uefaCountries
                = countryRepository.findDynamicProjectionByConfederationId(UEFA_ID, projectionClass);

        assertUefaIdName(uefaCountries);
    }

    @Test
    void testFindCountryCodeByConfederationId() {
        List<StringCode> uefaCountries
                = countryRepository.findCountryCodeByConfederationId(UEFA_ID);

        assertUefaCode(uefaCountries);
    }

    @Test
    void testSearchByNameContainingOrderByNameAsc() {
        assertPrefixByNameContainingOrderByNameAsc((name) -> countryRepository.searchByNameContainingOrderByNameAsc(name));
    }

    @Test
    void testQueryByNameContainingOrderByNameAsc() {
        assertPrefixByNameContainingOrderByNameAsc((name) -> countryRepository.queryByNameContainingOrderByNameAsc(name));
    }

    @Test
    void testStreamByNameContainingOrderByNameAsc() {
        assertPrefixByNameContainingOrderByNameAsc((name) -> countryRepository.streamByNameContainingOrderByNameAsc(name));
    }

    @Test
    void testReadByNameContainingOrderByNameAsc() {
        assertPrefixByNameContainingOrderByNameAsc((name) -> countryRepository.readByNameContainingOrderByNameAsc(name));
    }

    @Test
    void testGetByNameContainingOrderByNameAsc() {
        assertPrefixByNameContainingOrderByNameAsc((name) -> countryRepository.getByNameContainingOrderByNameAsc(name));
    }

    @Test
    void testFindByNameContainingOrderByNameAsc() {
        assertPrefixByNameContainingOrderByNameAsc((name) -> countryRepository.findByNameContainingOrderByNameAsc(name));
    }

    private void assertPrefixByNameContainingOrderByNameAsc(Function<String, List<Country>> listSupplier) {
        assertThat(listSupplier.apply(REPUBLIC_STRING))
                .extracting(Country::getId)
                .containsExactly(KOREA_ID, DOMINICAN_ID);
    }

    @Test
    void testFindTop3SortByPopulationAsc() {
        Sort sortByPopulation = Sort.by("population");

        List<Country> lessPopulatedCountries = countryRepository.findTop3By(sortByPopulation);

        assertThat(lessPopulatedCountries)
                .extracting(Country::getId)
                .containsExactly(VATICAN_ID, COSTA_RICA_ID, NORWAY_ID);
    }

    @Test
    void testFindTop3SortByPopulationDesc() {
        Sort sortByPopulation = Sort.by(Sort.Direction.DESC, "population");

        List<Country> mostPopulatedCountries = countryRepository.findTop3By(sortByPopulation);

        assertThat(mostPopulatedCountries)
                .extracting(Country::getId)
                .containsExactly(USA_ID, MEXICO_ID, KOREA_ID);
    }


}
