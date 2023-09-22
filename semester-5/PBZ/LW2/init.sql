DROP SCHEMA public CASCADE;
CREATE SCHEMA public;

CREATE TABLE grades(
                       id  INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                       name VARCHAR(255) NOT NULL,
                       coefficient DOUBLE PRECISION
);

CREATE TABLE workers(
                        id  INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                        surname     VARCHAR(255) NOT NULL,
                        name        VARCHAR(255) NOT NULL,
                        patronim    VARCHAR(255) NOT NULL,
                        pos         VARCHAR(255) NOT NULL,
                        trade_union BOOLEAN      NOT NULL,

                        grade_id INT REFERENCES grades(id) ON DELETE CASCADE
);

CREATE TABLE payments(
                         id  INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                         datetime     VARCHAR(255)     NOT NULL,
                         salary       DOUBLE PRECISION NOT NULL,
                         surcharge    DOUBLE PRECISION NOT NULL,
                         income_tax   DOUBLE PRECISION NOT NULL,
                         pension_fund DOUBLE PRECISION NOT NULL,
                         trade_union  DOUBLE PRECISION NOT NULL,
                         payoff       DOUBLE PRECISION
                             GENERATED ALWAYS AS
                                 (salary + surcharge - income_tax - pension_fund - trade_union)
                                 STORED,

                         worker_id INT REFERENCES workers(id)
);

CREATE TABLE settings(
                         name  VARCHAR(255) PRIMARY KEY,
                         value DOUBLE PRECISION NOT NULL
);

INSERT INTO settings(name, value)
VALUES ('minimal_payment', 350  ),
       ('income_tax',      0.13 ),
       ('surcharge',       0.15 ),
       ('pension_fund',    0.01 ),
       ('trade_union',     0.01 );

INSERT INTO grades(name, coefficient)
VALUES ('I',   1),
       ('II',  1.15),
       ('III', 1.3),
       ('IV',  1.45),
       ('V',   1.6);

INSERT INTO workers(surname, name, patronim, pos, trade_union, grade_id)
VALUES ('Smith',    'John',      'Michael',   'Manager',       true,  3),
       ('Johnson',  'Emma',      'Grace',     'Engineer',      false, 2),
       ('Williams', 'Daniel',    'Robert',    'Analyst',       true,  4),
       ('Brown',    'Olivia',    'Sophia',    'Developer',     false, 1),
       ('Jones',    'James',     'Andrew',    'Designer',      true,  5),
       ('Miller',   'Ava',       'Elizabeth', 'Administrator', false, 3),
       ('Davis',    'William',   'David',     'Technician',    true,  2),
       ('Garcia',   'Sophia',    'Emily',     'Assistant',     false, 4),
       ('Wilson',   'Oliver',    'Joseph',    'Coordinator',   true,  1),
       ('Taylor',   'Charlotte', 'Grace',     'Manager',       false, 5);
