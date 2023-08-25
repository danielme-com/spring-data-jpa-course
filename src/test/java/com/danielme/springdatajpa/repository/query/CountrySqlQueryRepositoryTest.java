package com.danielme.springdatajpa.repository.query;

import com.danielme.springdatajpa.model.dto.IdName;
import com.danielme.springdatajpa.model.dto.IdNameDTO;
import com.danielme.springdatajpa.model.dto.IdNameRecord;
import com.danielme.springdatajpa.model.entity.Country;
import com.danielme.springdatajpa.model.entity.Country_;
import com.danielme.springdatajpa.repository.basic.CountryCrudRepository;
import com.danielme.springdatajpa.repository.basic.CountryRepository;
import jakarta.persistence.Tuple;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;

import static com.danielme.springdatajpa.DatasetConstants.*;
import static com.danielme.springdatajpa.TestConstants.CITY_STRING;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CountrySqlQueryRepositoryTest {

    @Autowired
    private CountrySqlQueryRepository countryRepository;

    @Test
    void testFindByNameOrCapital() {
        List<Country> countries = countryRepository.findByNameOrCapital('%' + CITY_STRING + '%');

        assertThat(countries)
                .extracting(Country::getId)
                .containsExactly(GUATEMALA_ID, MEXICO_ID, VATICAN_ID);
    }

    @Test
    void testCountByMinPopulation() {
        int over100millionCount = countryRepository.countByMinPopulation(100_000_000);

        assertThat(over100millionCount).isEqualTo(2);
    }

    @Test
    void testFindByName() {
        Optional<Country> spain = countryRepository.findByName(SPAIN_NAME);

        assertThat(spain).isPresent();
    }

    @Test
    void testFindByMinPopulationAsRaw() {
        List<Object[]> over100millionRaw = countryRepository.findByMinPopulationAsRaw(100_000_000);
        List<IdNameDTO> over100millionDTO = over100millionRaw
                .stream()
                .map(c -> new IdNameDTO((Long) c[0], (String) c[1]))
                .toList();

        assertOver100Million(over100millionDTO);
    }

    @Test
    void testFindByMinPopulationAsTuple() {
        List<Tuple> over100millionTuple = countryRepository.findByMinPopulationAsTuple(100_000_000);
        List<IdNameDTO> over100millionDTO = over100millionTuple
                .stream()
                .map(c -> new IdNameDTO((Long) c.get("id"), (String) c.get("name")))
                .toList();

        assertOver100Million(over100millionDTO);
    }

    private static void assertOver100Million(List<IdNameDTO> over100millionDTO) {
        assertThat(over100millionDTO)
                .extracting(IdNameDTO::getId)
                .containsExactly(MEXICO_ID, USA_ID);
    }

    @Test
    void testFindByMinPopulationAsEntities() {
        List<Country> over100millionRecord = countryRepository.findByMinPopulation(100_000_000);

        assertThat(over100millionRecord)
                .extracting(Country::getId)
                .containsExactly(MEXICO_ID, USA_ID);
    }

    @Test
    void testFindByMinPopulationAsInterface() {
        List<IdName> over100millionTuple = countryRepository.findByMinPopulationAsInterface(100_000_000);

        assertThat(over100millionTuple)
                .extracting(IdName::getId)
                .containsExactly(MEXICO_ID, USA_ID);
    }

    @Test
    void testFindByMinPopulationAsRecord() {
        List<IdNameRecord> over100millionRecord = countryRepository.findByMinPopulationAsRecord(100_000_000);

        assertThat(over100millionRecord)
                .extracting(IdNameRecord::id)
                .containsExactly(MEXICO_ID, USA_ID);
    }

    @Test
    void testFindByMinAdmissionDate() {
        testFindByMindAdmissionDate((d, p) -> countryRepository.findByMinAdmissionDate(d, p));
    }

    @Test
    void testFindByMinAdmissionDateNamed() {
        testFindByMindAdmissionDate((d, p) -> countryRepository.findByMinAdmissionDateAsNamed(d, p));
    }

    private void testFindByMindAdmissionDate(BiFunction<LocalDate, Pageable, Page<Country>> repoCall) {
        Sort sort = Sort.by(Country_.NAME);
        LocalDate minDate = LocalDate.of(1955, 1, 1);
        Pageable page1 = PageRequest.of(0, 1, sort);

        Page<Country> countriesAfterDatePage1 = repoCall.apply(minDate, page1);

        assertThat(countriesAfterDatePage1.getTotalElements()).isEqualTo(2);
        assertThat(countriesAfterDatePage1)
                .extracting(Country::getId)
                .containsExactly(KOREA_ID);
    }

    @Sql(value = {"/reset.sql", "/data.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    void testAppendConfederationToName(@Autowired CountryCrudRepository countryCrudRepository) {
        int updated = countryRepository.appendConfederationToName(AFC_NAME);

        assertThat(updated).isEqualTo(1);
        String koreNewName = countryCrudRepository.findById(KOREA_ID).get().getName();
        assertThat(koreNewName).isEqualTo(KOREA_NAME + " (" + AFC_NAME + ")");
    }

}
