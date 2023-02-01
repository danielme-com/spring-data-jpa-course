package com.danielme.springdatajpa.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NamedQuery(name = "Country.findByCapital",
        query = """
                select c from Country c
                where c.capital like concat('%', :capital, '%')
                order by c.capital""")
@Entity
@Table(name = "countries")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Country {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private Integer population;

    @Column(nullable = false, unique = true)
    private String capital;

    private Boolean ocde;

    @Column(name = "united_nations_admission")
    private LocalDate unitedNationsAdmission;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "confederation_id")
    private Confederation confederation;

}