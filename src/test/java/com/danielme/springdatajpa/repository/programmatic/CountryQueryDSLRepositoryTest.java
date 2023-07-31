package com.danielme.springdatajpa.repository.programmatic;

import com.danielme.springdatajpa.DatasetConstants;
import com.danielme.springdatajpa.SharedCountryAssertions;
import com.danielme.springdatajpa.model.dto.IdName;
import com.danielme.springdatajpa.model.entity.Country;
import com.danielme.springdatajpa.model.entity.QCountry;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.QPageRequest;
import org.springframework.data.querydsl.QSort;

import java.time.LocalDate;
import java.util.List;

import static com.danielme.springdatajpa.DatasetConstants.UEFA_ID;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CountryQueryDSLRepositoryTest {

    @Autowired
    CountryQueryDSLRepository countryRepository;

    @Test
    void testFindAllCountries1945() {
        LocalDate startRange = LocalDate.of(1945, 1, 1);
        LocalDate endRange = LocalDate.of(1945, 12, 31);

        BooleanExpression predicateFullRange = QCountry.country.unitedNationsAdmission.goe(startRange)
                .and(QCountry.country.unitedNationsAdmission.loe(endRange));
        //alternative
        //BooleanExpression predicateFullRange = QCountry.country.unitedNationsAdmission.between(startRange, endRange);

        Sort order = QSort.by(QCountry.country.unitedNationsAdmission.desc())
                .and(QCountry.country.name.asc());
        //alternative
        //OrderSpecifier<?>[] orders = {QCountry.country.unitedNationsAdmission.desc(),
        //        QCountry.country.name.asc()};

        //alternative
        //Sort sort = Sort.by("unitedNationsAdmission").descending();

        List<Country> countries1945 = countryRepository.findAll(predicateFullRange, order);

        assertThat(countries1945)
                .extracting(Country::getId)
                .hasSize(9)
                .doesNotContain(DatasetConstants.SPAIN_ID,
                        DatasetConstants.KOREA_ID,
                        DatasetConstants.VATICAN_ID);
    }

    @Test
    void testFindAllUefaAsIdName() {
        BooleanExpression confFilter = QCountry.country.confederation.id.eq(UEFA_ID);

        List<IdName> countriesUefa = countryRepository.findBy(confFilter,
                q -> q.sortBy(Sort.by("name"))
                    .as(IdName.class)
                    .all());

        SharedCountryAssertions.assertUefaIdName(countriesUefa);
    }

    @Test
    void testFindAllUefaPagination() {
        BooleanExpression confFilter = QCountry.country.confederation.id.eq(UEFA_ID);
        Pageable page1 = QPageRequest.of(0, 2 , QCountry.country.name.asc());
        //Pageable page1 = PageRequest.of(0, 2 , QSort.by(QCountry.country.name.asc()));
        Page<Country> countriesUefa = countryRepository.findAll(confFilter, page1);

        assertThat(countriesUefa.getTotalPages()).isEqualTo(2);
    }

}
