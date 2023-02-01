package com.danielme.springdatajpa;

import com.danielme.springdatajpa.model.dto.StringCode;

import java.util.List;

import static com.danielme.springdatajpa.DatasetConstants.*;
import static com.danielme.springdatajpa.DatasetConstants.NETHERLANDS_NAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;

public final class SharedCountryAssertions {

    private SharedCountryAssertions() {
    }

    public static void assertUefaIdName(List uefaCountries) {
        assertThat(uefaCountries)
                .extracting("id", "name")
                .containsExactlyInAnyOrder(
                        tuple(SPAIN_ID, SPAIN_NAME),
                        tuple(NORWAY_ID, NORWAY_NAME),
                        tuple(NETHERLANDS_ID, NETHERLANDS_NAME));
    }

    public static void assertUefaCode(List<StringCode> uefaCountries) {
        assertThat(uefaCountries)
                .extracting(StringCode::getCode)
                .containsExactlyInAnyOrder(buildCode(SPAIN_ID, SPAIN_NAME),
                        buildCode(NORWAY_ID, NORWAY_NAME),
                        buildCode(NETHERLANDS_ID, NETHERLANDS_NAME));
    }

    private static String buildCode(long id, String name) {
        return id + "-" + name;
    }

}
