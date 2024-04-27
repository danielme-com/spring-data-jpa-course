package com.danielme.springdatajpa.repository.query;

import com.danielme.springdatajpa.model.dto.ConfederationSummaryRecord;
import com.danielme.springdatajpa.model.entity.Confederation;
import jakarta.persistence.Tuple;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface ConfederationJpqlQueryRepository extends Repository<Confederation, Long> {

    @Query("""
            SELECT c.id, c.name, COUNT(ct.id) FROM Country ct INNER JOIN ct.confederation c
            GROUP BY ct.confederation.id, c.name
            ORDER BY c.name ASC""")
    List<Object[]> getSummaryCountryCountAsRaw();

    @Query("""
            SELECT new com.danielme.springdatajpa.model.dto.ConfederationSummaryRecord(c.id, c.name, COUNT(ct.id))
            FROM Country ct INNER JOIN ct.confederation c
            GROUP BY ct.confederation.id, c.name
            ORDER BY c.name ASC""")
    List<ConfederationSummaryRecord> getSummaryCountryCountAsRecord();

    @Query("""
            SELECT c.id as id, c.name as name, COUNT(ct.id) as countries FROM Country ct INNER JOIN ct.confederation c
            GROUP BY ct.confederation.id, c.name
            ORDER BY c.name ASC""")
    List<Tuple> getSummaryCountryCountAsTuple();

    @Query("""
            SELECT new com.danielme.springdatajpa.model.dto.ConfederationSummaryRecord(
                c.id, c.name as name, COUNT(ct.id) as countries)
            FROM Country ct INNER JOIN ct.confederation c
            GROUP BY ct.confederation.id, c.name""")
    List<ConfederationSummaryRecord> getSummaryCountryCount(Sort sort);

    List<Confederation> findByCountriesIsEmpty();

}
