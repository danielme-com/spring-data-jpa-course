package com.danielme.springdatajpa.service;

import com.danielme.springdatajpa.model.entity.Country;
import com.danielme.springdatajpa.repository.basic.CountryCrudRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
public class CountryService {

    private final CountryCrudRepository countryRepository;

    @Transactional
    public void updateCountryNameTransactional(Long id, String newName) {
        countryRepository.findById(id)
                .ifPresent(spain -> spain.setName(newName));
    }

    public void updateCountryNameNoTransaction(Long id, String newName) {
        Optional<Country> countryOpt = countryRepository.findById(id);
        if (countryOpt.isPresent()) {
            countryOpt.get().setName(newName);
            countryRepository.save(countryOpt.get());
        }
    }

}
