package com.danielme.springdatajpa.repository.query;

import com.danielme.springdatajpa.model.entity.Confederation;
import com.danielme.springdatajpa.model.entity.Country;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface CountryPagingRepository extends Repository<Country, Long> {

    List<Country> findAllByOrderByName(Pageable pageable);

    List<Country> findAll(Pageable pageable);

    Page<Country> findPageBy(Pageable pageable);

    @Query(value = "SELECT c FROM Country c",
            countQuery = "SELECT count(c) FROM Country c")
    Page<Country> findPageWithJpql(Pageable pageable);

    Slice<Country> findSliceBy(Pageable pageable);

    @Query(value="SELECT c FROM Country c JOIN FETCH c.confederation",
            countQuery = "SELECT COUNT(c) FROM Country c")
    Page<Country> findAllWithConfederation(Pageable pageable);

}
