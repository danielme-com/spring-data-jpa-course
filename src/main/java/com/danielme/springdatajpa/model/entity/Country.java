package com.danielme.springdatajpa.model.entity;

import com.danielme.springdatajpa.model.dto.IdNameRecord;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@SqlResultSetMapping(
        name = "IdNameRecordMapping",
        classes = @ConstructorResult(
                targetClass = IdNameRecord.class,
                columns = {
                        @ColumnResult(name = "id", type = Long.class),
                        @ColumnResult(name = "name")
                }))
@NamedNativeQuery(name = "Country.findByMinPopulationAsRecord",
        query =  """
            SELECT id, name FROM countries
            WHERE population >:minPopulation ORDER BY population""",
        resultSetMapping = "IdNameRecordMapping")
@NamedQuery(name = "Country.findByCapital",
        query = """
                select c from Country c
                where c.capital like concat('%', :capital, '%')
                order by c.capital""")
@NamedQuery(name = "Country.findByCapital.count",
        query = """
                select count(c) from Country c
                where c.capital like concat('%', :capital, '%') """)
@NamedNativeQuery(name = "Country.findByMinPopulation",
        resultClass = Country.class,
        query =  """
            SELECT * FROM countries
            WHERE population >:minPopulation ORDER BY population""")
@NamedNativeQuery(name = "Country.findByMinAdmissionDateAsNamed",
        resultClass = Country.class,
        query =  """
            SELECT * FROM countries
            WHERE united_nations_admission > :minDateAdmission
           ORDER BY name""")
@NamedNativeQuery(name = "Country.findByMinAdmissionDateAsNamed.count",
        resultClass = Long.class,
        query =  """
            SELECT COUNT(*) FROM countries
            WHERE united_nations_admission > :minDateAdmission""")
@NamedStoredProcedureQuery(name = "Country.countCountriesByConfederationId",
        procedureName = "count_countries_by_confederation_id",
        parameters = {
                @StoredProcedureParameter(mode = ParameterMode.IN,  name = "param_conf_id", type = Long.class),
                @StoredProcedureParameter(mode = ParameterMode.OUT, name = "countries_count", type = Integer.class) })
@NamedStoredProcedureQuery(name = "Country.countCountriesAndPopulationByConfId",
        procedureName = "count_countries_and_population_by_confederation_id",
        parameters = {
                @StoredProcedureParameter(mode = ParameterMode.IN,  name = "param_conf_id", type = Long.class),
                @StoredProcedureParameter(mode = ParameterMode.OUT, name = "countries_count", type = Integer.class),
                @StoredProcedureParameter(mode = ParameterMode.OUT, name = "population", type = Integer.class) })
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

    private Boolean oecd;

    @Column(name = "united_nations_admission")
    private LocalDate unitedNationsAdmission;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "confederation_id")
    private Confederation confederation;

}