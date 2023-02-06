package com.danielme.springdatajpa.repository.basic;

import com.danielme.springdatajpa.model.entity.Confederation;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface ConfederationCustomCrudRepository extends Repository<Confederation, Long> {

    Optional<Confederation> findById(Long id);

    boolean existsById(Long id);

    long count();

}
