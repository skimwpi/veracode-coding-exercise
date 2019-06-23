-- H2 DDL

DROP TABLE IF EXISTS User;

CREATE TABLE User (
    user_name VARCHAR(255) NOT NULL PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    middle_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL
);

CREATE INDEX idx_last_name on User(last_name);
CREATE INDEX idx_first_name on User(first_name);

INSERT INTO User (user_name, first_name, middle_name, last_name) VALUES
 ('spiderman', 'peter', '', 'parker');
