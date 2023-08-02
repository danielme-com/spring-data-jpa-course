package com.danielme.springdatajpa.repository.query;

import com.danielme.springdatajpa.model.dto.ConfederationSummaryDTO;
import com.danielme.springdatajpa.model.dto.ConfederationSummaryRecord;
import com.danielme.springdatajpa.model.entity.QConfederation;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.Expressions;
import jakarta.persistence.Tuple;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.QSort;

import java.util.List;
import java.util.stream.Collectors;

import static com.danielme.springdatajpa.DatasetConstants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@SpringBootTest
class ConfederationJpqlQueryRepositoryTest {

    @Autowired
    private ConfederationJpqlQueryRepository confederationRepository;

    @Test
    void testSummaryCountriesCountAsRaw() {
        List<Object[]> confederationsSummary = confederationRepository.getSummaryCountryCountAsRaw();

        List<ConfederationSummaryRecord> confSummaryMapped = map(confederationsSummary);
        assertConfederationsSummary(confSummaryMapped);
    }

    @Test
    void testSummaryCountriesCountAsTuple() {
        List<Tuple> confederationsSummary = confederationRepository.getSummaryCountryCountAsTuple();

        List<ConfederationSummaryRecord> confSummaryMapped = mapTuples(confederationsSummary);
        assertConfederationsSummary(confSummaryMapped);
    }

    @Test
    void testGetSummaryCountryCountWithSort() {
        Sort sort = Sort.by("countries")
                .descending()
                .and(Sort.by("name"));

        List<ConfederationSummaryRecord> confederationsSummary = confederationRepository.getSummaryCountryCount(sort);

        assertConfederationsSummarySorting(confederationsSummary);
    }

    @Test
    void testGetSummaryCountryCountWithQSort() {
        OrderSpecifier orderAlias = new OrderSpecifier<>(Order.DESC, Expressions.stringPath("countries"));
        Sort sort = QSort.by(orderAlias)
                .and(QSort.by(QConfederation.confederation.name.desc()));

        List<ConfederationSummaryRecord> confederationsSummary = confederationRepository.getSummaryCountryCount(sort);

        assertConfederationsSummarySorting(confederationsSummary);
    }

    private void assertConfederationsSummarySorting(List<ConfederationSummaryRecord> confederationsSummary) {
        assertThat(confederationsSummary)
                .extracting(ConfederationSummaryRecord::id)
                .containsExactly(CONCACAF_ID, UEFA_ID, CONMEBOL_ID, AFC_ID);
    }

    @Test
    void testGetSummaryCountryCountWithOrder1() {
        List<Sort.Order> orders = List.of(Sort.Order.desc("countries"), Sort.Order.asc("name"));

        List<ConfederationSummaryRecord> confederationsSummary = confederationRepository.getSummaryCountryCount(Sort.by(orders));

        assertConfederationsSummarySorting(confederationsSummary);
    }

    @Test
    void testGetSummaryCountryCountWithOrder2() {
        Sort.Order countryOrder = Sort.Order.by("countries")
                .with(Sort.Direction.DESC);
        Sort.Order nameOrder = Sort.Order.by("name");
        Sort sort = Sort.by(countryOrder, nameOrder);

        List<ConfederationSummaryRecord> confederationsSummary = confederationRepository.getSummaryCountryCount(sort);

        assertConfederationsSummarySorting(confederationsSummary);
    }

    @Test
    void testGetSummaryCountryCountWithTypedSort() {
        Sort.TypedSort<ConfederationSummaryDTO> typedSort = Sort.sort(ConfederationSummaryDTO.class);
        Sort sort = typedSort.by(ConfederationSummaryDTO::getCountries)
                .descending()
                .and(typedSort.by(ConfederationSummaryDTO::getName));

        List<ConfederationSummaryRecord> confederationsSummary = confederationRepository.getSummaryCountryCount(sort);

        assertConfederationsSummarySorting(confederationsSummary);
    }

    private List<ConfederationSummaryRecord> mapTuples(List<Tuple> confederationsSummary) {
        return confederationsSummary.stream()
                .map(tuple -> new ConfederationSummaryRecord(
                        tuple.get("id", Long.class),
                        tuple.get("name", String.class),
                        tuple.get("countries", Long.class)))
                .toList();
    }

    @Test
    void testSummaryCountriesCountAsRecord() {
        List<ConfederationSummaryRecord> confederationsSummary = confederationRepository.getSummaryCountryCountAsRecord();

        assertConfederationsSummary(confederationsSummary);
    }

    private void assertConfederationsSummary(List<ConfederationSummaryRecord> confederationsSummary) {
        assertThat(confederationsSummary)
                .extracting(ConfederationSummaryRecord::id, ConfederationSummaryRecord::countries)
                .containsExactly(
                        tuple(AFC_ID, 1L),
                        tuple(CONCACAF_ID, 5L),
                        tuple(CONMEBOL_ID, 2L),
                        tuple(UEFA_ID, 3L));
    }

    private List<ConfederationSummaryRecord> map(List<Object[]> confederationsSummary) {
        return confederationsSummary.stream()
                .map(object -> new ConfederationSummaryRecord((Long) object[0], (String) object[1], (Long) object[2]))
                .toList();
    }

}
