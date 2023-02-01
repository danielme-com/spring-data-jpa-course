package com.danielme.springdatajpa.repository.programmatic;

import com.danielme.springdatajpa.model.entity.Country;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.ListQueryByExampleExecutor;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface CountryQBERepository extends Repository<Country, Long>,
        ListQueryByExampleExecutor<Country> {

}
