package com.danielme.springdatajpa.repository.query;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.QueryRewriter;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ExampleQueryRewriter implements QueryRewriter {

    @Override
    public String rewrite(String query, Sort sort) {
        log.info("query before: " + query);
        return query.replace(" c ", " country ");
    }

}
