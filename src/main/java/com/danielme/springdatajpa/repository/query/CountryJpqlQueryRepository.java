package com.danielme.springdatajpa.repository.query;

import com.danielme.springdatajpa.model.dto.*;
import com.danielme.springdatajpa.model.entity.Country;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface CountryJpqlQueryRepository extends Repository<Country, Long> {

    @Query("""
            SELECT c FROM Country c
            WHERE UPPER(c.name) LIKE UPPER(:text) OR UPPER(c.capital) LIKE UPPER(:text)
            ORDER BY c.name ASC""")
    List<Country> findByNameOrCapital(String text);

    @Query("""
            SELECT c FROM Country c
            WHERE UPPER(c.name) LIKE UPPER(CONCAT('%', :text, '%')) OR UPPER(c.capital) LIKE UPPER(CONCAT('%', :text, '%'))
            ORDER BY c.name ASC""")
    List<Country> findByNameOrCapitalContaining(String text);

    @Query("""
            SELECT c FROM Country c
            WHERE c.name LIKE %:name%
            ORDER BY c.name ASC""")
    List<Country> findByName(String name);

    @Query("SELECT MAX(c.capital) FROM Country c")
    String findLongestCapitalName();

    @Query(name = "Country.findByCapital")
    List<Country> findByCapitalUsingNameQuery(String capital);

    @Transactional
    @Modifying
    @Query("""
            UPDATE Country c SET c.name = CONCAT(c.name, ' (', :confederationName, ')')
            WHERE c.confederation.id IN
                (SELECT c.id FROM Confederation c WHERE c.name LIKE :confederationName)""")
    int appendConfederationToName(String confederationName);

    Optional<Country> findById(long id);

    @Transactional
    @Modifying
    @Query("DELETE FROM Country c WHERE c.confederation.id = :id")
    int deleteByConfederationId(Long id);

    @Query("""
            select c from Country c
            where c.population between :#{#countrySearch.populationMin} and :#{#countrySearch.populationMax}
            and c.confederation.id IN :#{#countrySearch.confederationsIds}
            order by c.name asc""")
    List<Country> search(CountrySearch countrySearch);

    @Query("""
            SELECT new com.danielme.springdatajpa.model.dto.IdNameRecord(c.id, c.name)
            FROM Country c WHERE c.confederation.id=:id""")
    List<IdNameRecord> findIdNameAsRecordByConfederationId(Long id);

    @Query("SELECT c.id as id, c.name as name FROM Country c WHERE c.confederation.id=:id")
    List<IdName> findIdNameAsInterfaceByConfederationId(Long id);


    @Query("""
            SELECT c.id as id, c.name as name, c.confederation as confederation
            FROM Country c WHERE c.confederation.id=:id""")
    List<CountryIdNameConfederationProjection> findIdNameConfederationByConfederationId(Long id);

    @Query("SELECT c.id as id, c.name as name FROM Country c WHERE c.confederation.id=:id")
    List<StringCode> findCountryCodeByConfederationId(Long id);

    @Query("SELECT c FROM Country c")
    List<Country> findAll(Sort sort);

}
