CREATE SEQUENCE confederations_seq INCREMENT BY 50;
CREATE TABLE confederations
(
    id   BIGINT GENERATED BY DEFAULT AS SEQUENCE confederations_seq,
    name VARCHAR(255) NOT NULL UNIQUE,
    PRIMARY KEY (id)
);

CREATE SEQUENCE countries_seq INCREMENT BY 50;
CREATE TABLE countries
(
    id                       BIGINT GENERATED BY DEFAULT AS SEQUENCE countries_seq,
    name                     VARCHAR(50) NOT NULL UNIQUE,
    population               INTEGER     NOT NULL,
    oecd                     BOOLEAN     NOT NULL,
    capital                  VARCHAR(50) NOT NULL UNIQUE,
    united_nations_admission DATE,
    confederation_id         BIGINT,
    PRIMARY KEY (id)
);

ALTER TABLE countries ADD FOREIGN KEY (confederation_id) REFERENCES confederations (id);

CREATE SEQUENCE users_seq INCREMENT BY 50;
CREATE TABLE users
(
    id                        BIGINT GENERATED BY DEFAULT AS SEQUENCE users_seq,
    name                      VARCHAR(50) NOT NULL UNIQUE,
    created_by                VARCHAR(50) NOT NULL,
    created_date              TIMESTAMP not null,
    last_modified_by          VARCHAR(50),
    last_modified_date          DATETIME,
    PRIMARY KEY (id)
);

