package com.danielme.springdatajpa.repository.jdbc;

import com.danielme.springdatajpa.model.entity.Country;
import com.danielme.springdatajpa.model.entity.Country_;
import com.danielme.springdatajpa.model.entity.QCountry;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CountryDao {

    @Autowired
    private DataSource dataSource;
    @PersistenceContext
    private EntityManager entityManager;

    @SneakyThrows
    public List<Country> findByPopulationLessThanWithJdbc(int maxPopulation) {
        String sql = "SELECT * FROM countries WHERE population < ? ORDER BY name";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);) {
            ps.setInt(1, maxPopulation);
            ResultSet resultSet = ps.executeQuery();
            return mapResults(resultSet);
        }
    }

    public List<Country> findByPopulationLessThanWithJpa(int maxPopulation) {
        return entityManager.createQuery("SELECT c FROM Country c WHERE c.population < :maxPopulation ORDER BY c.name", Country.class)
                .setParameter("maxPopulation", maxPopulation)
                .getResultList();
    }

    private List<Country> mapResults(ResultSet resultSet) throws SQLException {
        List<Country> results = new ArrayList<>();
        while (resultSet.next()) {
            Country country = mapToCountry(resultSet);
            results.add(country);
        }
        return results;
    }

    private Country mapToCountry(ResultSet resultSet) throws SQLException {
        return new Country(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getInt("population"),
                resultSet.getString("capital"),
                resultSet.getBoolean("oecd"),
                mapToLocalDate(resultSet.getDate("united_nations_admission")),
                null);
    }

    private LocalDate mapToLocalDate(Date date) {
        if (date == null) {
            return null;
        }
        return date.toLocalDate();
    }

    public List<Country> findAllWithJpql(LocalDate from, LocalDate to) {
        StringBuilder jpql = new StringBuilder("SELECT c FROM Country c");
        if (from != null || to != null) {
            jpql.append(" WHERE ");
            if (from != null) {
                jpql.append("unitedNationsAdmission >= :from");
            }
            if (to != null) {
                if (from != null) {
                    jpql.append(" AND ");
                }
                jpql.append("unitedNationsAdmission <= :to");
            }
        }
        jpql.append(" ORDER BY unitedNationsAdmission DESC");
        TypedQuery<Country> query = entityManager.createQuery(jpql.toString(), Country.class);
        if (from != null) {
            query.setParameter("from", from);
        }
        if (to != null) {
            query.setParameter("to", to);
        }
        return query.getResultList();
    }

    public List<Country> findAllWithCriteria(LocalDate from, LocalDate to) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Country> cq = cb.createQuery(Country.class);
        Root<Country> countryRoot = cq.from(Country.class);

        Path<LocalDate> unitedNationsAdmission = countryRoot.get(Country_.unitedNationsAdmission);

        List<Predicate> predicates = new ArrayList<>();
        if (from != null) {
            predicates.add(cb.greaterThanOrEqualTo(unitedNationsAdmission,
                    from));
        }
        if (to != null) {
            predicates.add(cb.lessThanOrEqualTo(unitedNationsAdmission,
                    to));
        }
        cq.select(countryRoot)
                .where(predicates.toArray(new Predicate[0]))
                .orderBy(cb.desc(unitedNationsAdmission));

        return entityManager.createQuery(cq).getResultList();
    }

    public List<Country> findAllWithQueryDSL(LocalDate from, LocalDate to) {
        JPAQuery<Country> query = new JPAQuery<Country>(entityManager)
                .select(QCountry.country)
                .from(QCountry.country)
                .orderBy(QCountry.country.unitedNationsAdmission.desc());

        if (from != null) {
            query.where(QCountry.country.unitedNationsAdmission.goe(from));
        }
        if (to != null) {
            query.where(QCountry.country.unitedNationsAdmission.loe(to));
        }

        return query.fetch();
    }

}
