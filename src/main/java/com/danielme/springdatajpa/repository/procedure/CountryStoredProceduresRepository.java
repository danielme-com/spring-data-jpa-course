package com.danielme.springdatajpa.repository.procedure;

import com.danielme.springdatajpa.model.dto.IdName;
import com.danielme.springdatajpa.model.entity.Country;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Transactional(readOnly = true)
public interface CountryStoredProceduresRepository extends Repository<Country, Long> {

    @Procedure
    Integer countCountriesByConfederationId(Long param_conf_id);

    @Procedure
    Integer count_countries_by_confederation_id(Long param_conf_id);

    @Procedure(procedureName = "count_countries_by_confederation_id")
    Integer countCountries(Long param_conf_id);

    @Procedure
    Map<String, Integer> countCountriesAndPopulationByConfId(Long param_conf_id);

    @Query(value = "SELECT COUNT_COUNTRIES(:confId) FROM (VALUES(0))", nativeQuery = true)
    Integer countCountriesWithFunction(Long confId);

    @Query(value = "CALL COUNTRIES_BY_CONFEDERATION(:confId)", nativeQuery = true)
    List<IdName> findCountriesWithFunction(Long confId);

}
