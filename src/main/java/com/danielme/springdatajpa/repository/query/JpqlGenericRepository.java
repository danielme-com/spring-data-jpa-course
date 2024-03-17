package com.danielme.springdatajpa.repository.query;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

import java.util.List;

@NoRepositoryBean
public interface JpqlGenericRepository<T, ID> extends Repository<T, ID> {

    @Query("SELECT t FROM #{#entityName} t WHERE t.name LIKE %:name% ORDER BY t.name")
    List<T> findByNameLike(String name);

}
