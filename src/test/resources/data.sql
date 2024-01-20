INSERT INTO confederations (id, name)
VALUES (1, 'UEFA');
INSERT INTO confederations (id, name)
VALUES (2, 'CONCACAF');
INSERT INTO confederations (id, name)
VALUES (3, 'CONMEBOL');
INSERT INTO confederations (id, name)
VALUES (4, 'AFC');

INSERT INTO countries (id, name, capital, population, oecd, united_nations_admission, confederation_id)
VALUES (1, 'Norway', 'Oslo', 5136700, true, '1945-11-27', 1),
       (2, 'Spain', 'Madrid', 47265321, true, '1955-12-14', 1),
       (3, 'Mexico', 'Mexico City', 115296767, true, '1945-11-7', 2),
       (4, 'Colombia', 'Bogotá', 47846160, true, '1945-11-5', 3),
       (5, 'Costa Rica', 'San José', 4586353, true, '1945-11-2', 2),
       (6, 'The Netherlands', 'Amsterdam', 17734100, true, '1945-12-10', 1),
       (7, 'Republic of Korea', 'Seoul', 51744876, true, '1991-12-17', 4),
       (8, 'The Dominican Republic', 'Santo Domingo', 10694700, false, '1945-10-24', 2),
       (9, 'Peru', 'Lima', 34294231, false, '1945-10-31', 3),
       (10, 'Guatemala', 'Guatemala City', 17263239, false, '1945-11-21', 2),
       (11, 'United States of America', 'Washington, D.C.', 331893745, true, '1945-10-24', 2),
       (12, 'Vatican City State', 'Vatican City', 453, false, null, null);
