package com.danielme.springdatajpa.repository.basic;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
class CountryRepositoryTest {

    @Autowired
    private CountryRepository countryRepository;

    @Test
    void testRepository() {
        log.info(countryRepository.toString());
    }

}
