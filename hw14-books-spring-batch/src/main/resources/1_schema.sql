CREATE SEQUENCE IF NOT EXISTS SQ_GENRES START WITH 1;
CREATE SEQUENCE IF NOT EXISTS SQ_AUTHORS START WITH 1;
CREATE SEQUENCE IF NOT EXISTS SQ_BOOKS START WITH 1;


CREATE TABLE IF NOT EXISTS GENRES
(
    ID   bigint not null default nextval('SQ_GENRES') not null primary key,
    NAME VARCHAR(255)                     NOT NULL
);

CREATE TABLE IF NOT EXISTS AUTHORS
(
    ID   bigint not null default nextval('SQ_AUTHORS') not null primary key,
    NAME VARCHAR(255)                      NOT NULL
);

CREATE TABLE IF NOT EXISTS BOOKS
(
    ID        bigint not null default nextval('SQ_BOOKS') not null primary key,
    NAME      VARCHAR(255)                    NOT NULL,
    GENRE_ID  bigint                          NOT NULL,
    AUTHOR_ID bigint                          NOT NULL,
    CONSTRAINT FK_BOOKS_GENRE FOREIGN KEY (GENRE_ID) REFERENCES GENRES,
    CONSTRAINT FK_BOOKS_AUTHOR FOREIGN KEY (AUTHOR_ID) REFERENCES AUTHORS
);