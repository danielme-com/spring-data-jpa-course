package com.danielme.springdatajpa.model.dto;

import java.util.List;

public record CountrySearch(Integer populationMin,
                            Integer populationMax,
                            List<Long> confederationsIds) {
}
