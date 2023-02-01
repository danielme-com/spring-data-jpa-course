package com.danielme.springdatajpa.repository.query;

import com.danielme.springdatajpa.model.dto.IdName;
import com.danielme.springdatajpa.model.dto.IdNameRecord;
import com.danielme.springdatajpa.model.dto.StringCode;
import com.danielme.springdatajpa.model.entity.Country;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.Repository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

@Transactional(readOnly = true)
public interface CountryDerivedQueryRepository extends Repository<Country, Long> {

    List<Country> findByNameContainingOrderByNameAsc(String name);
    Stream<Country> findAsStreamByNameContainingOrderByNameAsc(String name);
    List<Country> findByNameContainingAndConfederationId(String name, Long id);
    List<Country> findByUnitedNationsAdmissionAfter(LocalDate date);
    List<Country> findByUnitedNationsAdmissionBefore(LocalDate date);
    List<Country> findByUnitedNationsAdmissionBetween(LocalDate dateMin, LocalDate dateMax);
    List<Country> findByPopulationLessThan(int population);
    List<Country> findByPopulationGreaterThan(int population);
    List<Country> findByUnitedNationsAdmissionIsNull();
    List<Country> findByUnitedNationsAdmissionIsNotNull();
    List<Country> findByUnitedNationsAdmissionEquals(LocalDate date);
    List<Country> findByConfederationIdNot(Long id);
    List<Country> findByCapitalLike(String capital);
    List<Country> findByCapitalContaining(String capital);
    List<Country> findByNameStartingWith(String name);
    List<Country> findByCapitalEndingWith(String capital);
    List<Country> findByConfederationIdIn(Collection<Long> ids);
    List<Country> findByOcdeFalse();
    Optional<Country> findByCapitalIgnoreCase(String capital);
    List<Country> findByNameContainingIgnoreCase(String name);
    List<Country> findByNameContainingOrCapitalContaining(String name, String capital);
    List<Country> findByOrderByUnitedNationsAdmissionAscNameAsc();
    List<Country> findTop3ByOrderByPopulationAsc();
    boolean existsByCapitalIgnoringCase(String capital);
    long countByConfederationId(Long id);
    List<Country> findTop3By(Sort sort);

    @Transactional
    long deleteByConfederationId(Long id);

    @Async
    CompletableFuture<List<Country>> findAsyncCountriesByNameContaining(String name);

    List<IdName> findIdNameAsInterfaceByConfederationId(Long id);
    List<StringCode> findCountryCodeByConfederationId(Long id);
    List<IdNameRecord> findIdNameAsRecordByConfederationId(Long id);
    <T> List<T> findDynamicProjectionByConfederationId(Long id, Class<T> type);

    //DONT DO THIS!! Use @Query
    List<Country> findByNameContainingIgnoringCaseOrCapitalIgnoringCaseContainingOrderByName(String name, String capital);

    List<Country> readByNameContainingOrderByNameAsc(String name);
    List<Country> getByNameContainingOrderByNameAsc(String name);
    List<Country> queryByNameContainingOrderByNameAsc(String name);
    List<Country> streamByNameContainingOrderByNameAsc(String name);
    List<Country> searchByNameContainingOrderByNameAsc(String name);

}
