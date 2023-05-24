package com.danielme.springdatajpa.repository.query;

import com.danielme.springdatajpa.model.entity.Confederation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface ConfederationPagingRepository extends Repository<Confederation, Long> {

    @Query(value="SELECT c FROM Confederation c JOIN FETCH c.countries",
            countQuery = "SELECT COUNT(c) FROM Confederation c")
    Page<Confederation> findAllWithCountryPagingInMemory(Pageable pageable);

    @Query("select c.id from Confederation c")
    Page<Long> findConfederationsWithCountry(Pageable page);

    @Query(value = "SELECT c FROM Confederation c JOIN FETCH c.countries where c.id IN (:ids)")
    List<Confederation> findAllConfederation(List<Long> ids, Sort sort);

    default Page<Confederation> findAllWithCountryPagingTwoQueries(Pageable pageable) {
        Page<Long> idsPage = findConfederationsWithCountry(pageable);
        List<Confederation> confederationsPage1 = findAllConfederation(idsPage.getContent(), idsPage.getSort());
        return new PageImpl<>(confederationsPage1, pageable, idsPage.getTotalElements());
    }


}
