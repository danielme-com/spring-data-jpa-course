package com.danielme.springdatajpa.repository.basic;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

import java.util.Optional;

@NoRepositoryBean
public interface ReadCommonRepository<T, ID> extends Repository<T, ID> {

    Optional<T> findById(ID id);

    boolean existsById(ID id);

    long count();

}
