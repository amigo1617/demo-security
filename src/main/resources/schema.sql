DROP TABLE user_master IF EXISTS;
DROP TABLE user_auth IF EXISTS;

CREATE TABLE user_master  ( id BIGINT IDENTITY NOT NULL PRIMARY KEY, username VARCHAR(20), password VARCHAR(20), address VARCHAR(50));

CREATE TABLE user_auth (id BIGINT IDENTITY NOT NULL PRIMARY KEY, auth VARCHAR(20));
