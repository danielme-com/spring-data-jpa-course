DROP PROCEDURE IF EXISTS count_countries_by_confederation_id^;
DROP PROCEDURE IF EXISTS count_countries_and_population_by_confederation_id^;
DROP PROCEDURE IF EXISTS countries_by_confederation_id^;
DROP FUNCTION IF EXISTS COUNT_COUNTRIES^;
DROP FUNCTION IF EXISTS COUNTRIES_BY_CONFEDERATION^;

CREATE PROCEDURE count_countries_by_confederation_id (IN param_conf_id int, OUT countries_count int)
READS SQL DATA
BEGIN ATOMIC
  SELECT COUNT(*) INTO countries_count FROM countries WHERE confederation_id = param_conf_id;
END^;


CREATE PROCEDURE count_countries_and_population_by_confederation_id (IN param_conf_id int,
            OUT countries_count int, OUT countries_population int)
READS SQL DATA
BEGIN ATOMIC
  SELECT COUNT(*), SUM(population) INTO countries_count, countries_population
  FROM countries WHERE confederation_id = param_conf_id;
END^;


CREATE FUNCTION COUNT_COUNTRIES(param_conf_id int) RETURNS INT
  READS SQL DATA
  RETURN (SELECT COUNT(*) FROM countries WHERE confederation_id = param_conf_id)
^;

CREATE FUNCTION COUNTRIES_BY_CONFEDERATION(param_conf_id int)
  RETURNS TABLE(id BIGINT, name VARCHAR(50))
  READS SQL DATA
  RETURN TABLE (SELECT id, name FROM countries WHERE confederation_id = param_conf_id)
^;

