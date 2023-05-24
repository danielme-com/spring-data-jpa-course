package com.danielme.springdatajpa.repository.query;

import com.danielme.springdatajpa.DatasetConstants;
import com.danielme.springdatajpa.model.entity.Confederation;
import com.danielme.springdatajpa.model.entity.Country;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ConfederationPagingRepositoryTest {

    @Autowired
    ConfederationPagingRepository confederationPagingRepository;

    @Test
    void testFindAllWithCountryJoinFetch() {
        assertFirstPageJoinFetch((pageable) -> confederationPagingRepository.findAllWithCountryPagingInMemory(pageable));
    }

    @Test
    void testFindAllWithCountryWith2Queries() {
        assertFirstPageJoinFetch((pageable) -> confederationPagingRepository.findAllWithCountryPagingTwoQueries(pageable));
    }

    private void assertFirstPageJoinFetch(Function<Pageable, Page<Confederation>> pageSupplier) {
        Pageable pageable = PageRequest.of(0, 2, Sort.Direction.ASC, "name");

        Page<Confederation> page1 = pageSupplier.apply(pageable);
        assertThat(page1.getTotalPages()).isEqualTo(2);
        assertThat(page1.getTotalElements()).isEqualTo(4);
        assertThat(page1.getContent())
                .extracting(Confederation::getId)
                .containsExactly(DatasetConstants.AFC_ID, DatasetConstants.CONCACAF_ID);
        assertThat(page1.getContent().get(0).getCountries()) //no LazyInitializationException thanks to JOIN FETCH
                .extracting(Country::getId)
                .containsExactly(DatasetConstants.KOREA_ID);
    }

}
