package com.danielme.springdatajpa.model.specification;

import com.danielme.springdatajpa.model.entity.Country;
import org.springframework.lang.Nullable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public final class CountrySpecifications {

    private CountrySpecifications() {
    }

    public static Specification<Country> admissionDateRange(@Nullable LocalDate from, @Nullable LocalDate to) {
        return Specification.allOf(admissionDateFrom(from), admissionDateTo(to));
    }

    public static Specification<Country> admissionDateFrom(@Nullable LocalDate from) {
        if (from == null) {
            return Specification.where(null);
        }
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("unitedNationsAdmission"), from);
    }

    public static Specification<Country> admissionDateTo(@Nullable LocalDate to) {
        if (to == null) {
            return Specification.where(null);
        }
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("unitedNationsAdmission"), to);
    }

    public static Specification<Country> confederationId(@Nullable Long id) {
        if (id == null) {
            return Specification.where(null);
        }
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("confederation").get("id"), id);
    }

}
