package com.danielme.springdatajpa.repository.query;

import com.danielme.springdatajpa.model.dto.IdName;
import com.danielme.springdatajpa.model.dto.IdNameRecord;
import com.danielme.springdatajpa.model.entity.Country;
import jakarta.persistence.Tuple;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface CountrySqlQueryRepository extends Repository<Country, Long> {

    @Query(value = """
            SELECT c.* FROM countries c
            WHERE UPPER(c.name) LIKE UPPER(:text) OR UPPER(c.capital) LIKE UPPER(:text)
            ORDER BY c.name ASC""",
            nativeQuery = true)
    List<Country> findByNameOrCapital(String text);

    @Query(value = "SELECT COUNT(*) FROM countries WHERE population >:minPopulation",
            nativeQuery = true)
    int countByMinPopulation(int minPopulation);

    @Query(value = "SELECT * FROM countries WHERE UPPER(name) = UPPER(:name)",
            nativeQuery = true)
    Optional<Country> findByName(String name);

    @Query(value = """
            SELECT id, name FROM countries
            WHERE population >:minPopulation ORDER BY population""",
            nativeQuery = true)
    List<Object[]> findByMinPopulationAsRaw(int minPopulation);

    @Query(value = """
            SELECT id, name FROM countries
            WHERE population >:minPopulation ORDER BY population""",
            nativeQuery = true)
    List<Tuple> findByMinPopulationAsTuple(int minPopulation);

    @Query(value = """
            SELECT id, name FROM countries
            WHERE population >:minPopulation ORDER BY population""",
            nativeQuery = true)
    List<IdName> findByMinPopulationAsInterface(int minPopulation);

    @Query(nativeQuery = true)
    List<IdNameRecord> findByMinPopulationAsRecord(int minPopulation);

    @Query(nativeQuery = true)
    List<Country> findByMinPopulation(int minPopulation);

    @Query(value = """
        SELECT * FROM countries
        WHERE united_nations_admission > :minDateAdmission""",
            //countQuery is unnecessary for this method
            /*countQuery = """
                SELECT COUNT(id) FROM countries
                WHERE united_nations_admission > :minDateAdmission""",*/
            nativeQuery = true)
    Page<Country> findByMinAdmissionDate(LocalDate minDateAdmission, Pageable page);

    @Query(nativeQuery = true)
    Page<Country> findByMinAdmissionDateAsNamed(LocalDate minDateAdmission, Pageable page);


    @Transactional
    @Modifying
    @Query(value ="""
            UPDATE countries SET name = CONCAT(name, ' (', :confederationName, ')')
            WHERE confederation_id IN
                (SELECT id FROM  confederations WHERE name LIKE :confederationName)""",
            nativeQuery = true)
    int appendConfederationToName(String confederationName);

    @Transactional
    @Modifying
    @Query(value = "ALTER TABLE countries ADD area INTEGER",
            nativeQuery = true)
    void addColumn();


}
