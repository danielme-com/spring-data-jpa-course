package com.danielme.springdatajpa.repository.programmatic;

import com.danielme.springdatajpa.model.entity.Country;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface CountrySpecificationRepository extends Repository<Country, Long>,
        JpaSpecificationExecutor<Country> {

}
