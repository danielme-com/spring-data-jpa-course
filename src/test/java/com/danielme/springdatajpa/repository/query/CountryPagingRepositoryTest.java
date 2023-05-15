package com.danielme.springdatajpa.repository.query;

import com.danielme.springdatajpa.model.entity.Country;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.function.Function;

import static com.danielme.springdatajpa.DatasetConstants.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CountryPagingRepositoryTest {

    private static final int PAGE_SIZE = 4;

    @Autowired
    CountryPagingRepository countryRepository;

    @Test
    void testFindAllListPageOne_1() {
        Pageable pageable = PageRequest.ofSize(PAGE_SIZE);

        List<Country> countriesFirstPage = countryRepository.findAllByOrderByName(pageable);

        assertCountriesFirstPage(countriesFirstPage);
    }

    @Test
    void testFindAllListPageOne_2() {
        Pageable pageable = PageRequest.of(0, PAGE_SIZE);

        List<Country> countriesFirstPage = countryRepository.findAllByOrderByName(pageable);

        assertCountriesFirstPage(countriesFirstPage);
    }

    @Test
    void testFindAllListPageOne_3() {
        Pageable pageable = PageRequest.of(0, PAGE_SIZE, Sort.by("name"));

        List<Country> countriesFirstPage = countryRepository.findAll(pageable);

        assertCountriesFirstPage(countriesFirstPage);
    }

    @Test
    void testFindAllListPageOne_4() {
        Pageable pageable = PageRequest.of(0, PAGE_SIZE, Sort.Direction.ASC, "name");

        List<Country> countriesFirstPage = countryRepository.findAll(pageable);

        assertCountriesFirstPage(countriesFirstPage);
    }

    @Test
    void testFindAllUnpaged() {
        List<Country> allCountries = countryRepository.findAll(Pageable.unpaged());

        assertThat(allCountries).hasSize(TOTAL_COUNTRIES);
    }

    @Test
    void testFindAllListNavigation() {
        Pageable pageable = PageRequest.of(0, PAGE_SIZE, Sort.Direction.ASC, "name");

        List<Country> countriesFirstPage = countryRepository.findAll(pageable);
        assertCountriesFirstPage(countriesFirstPage);

        Pageable pageablePageNext = pageable.next();
        List<Country> countriesPageTwo = countryRepository.findAll(pageablePageNext);
        assertThat(countriesPageTwo)
                .extracting(Country::getId)
                .containsExactly(NORWAY_ID, PERU_ID, KOREA_ID, SPAIN_ID);

        Pageable pageablePage4 = pageable.withPage(4);
        List<Country> countriesPageFour = countryRepository.findAll(pageablePage4);
        assertThat(countriesPageFour).isEmpty();
    }

    @Test
    void testFindAllPageWithDerivedQuery() {
        testFindAllPage((pageable -> countryRepository.findPageBy(pageable)));
    }

    @Test
    void testFindAllPageWithQuery() {
        testFindAllPage((pageable -> countryRepository.findPageWithJpql(pageable)));
    }

    void testFindAllPage(Function<Pageable, Page<Country>> function) {
        Pageable pageable = PageRequest.of(0, PAGE_SIZE, Sort.Direction.ASC, "name");

        Page<Country> countriesFirstPage = function.apply(pageable);

        assertCountriesFirstPage(countriesFirstPage.getContent());
        assertThat(countriesFirstPage.getTotalElements()).isEqualTo(TOTAL_COUNTRIES);
        assertThat(countriesFirstPage.getNumber()).isZero();
        assertThat(countriesFirstPage.getTotalPages()).isEqualTo(3);
        assertThat(countriesFirstPage.getNumberOfElements()).isEqualTo(PAGE_SIZE);
        assertThat(countriesFirstPage.getSize()).isEqualTo(4);
    }

    @Test
    void testFindAllSlice() {
        Pageable pageable = PageRequest.of(0, PAGE_SIZE, Sort.Direction.ASC, "name");

        Slice<Country> countriesFirstPage = countryRepository.findSliceBy(pageable);

        assertCountriesFirstPage(countriesFirstPage.getContent());
        assertThat(countriesFirstPage.getNumber()).isZero();
        assertThat(countriesFirstPage.getNumberOfElements()).isEqualTo(PAGE_SIZE);
        assertThat(countriesFirstPage.getSize()).isEqualTo(4);
    }

    private void assertCountriesFirstPage(List<Country> countriesFirstPage) {
        assertThat(countriesFirstPage)
                .extracting(Country::getId)
                .containsExactly(COLOMBIA_ID, COSTA_RICA_ID, GUATEMALA_ID, MEXICO_ID);
    }

}
