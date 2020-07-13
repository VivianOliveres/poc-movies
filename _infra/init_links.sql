CREATE DATABASE IF NOT EXISTS links;
USE links;

CREATE TABLE IF NOT EXISTS Imdb (
    movie_id BIGINT PRIMARY KEY,
    imdb_id varchar(20) NOT NULL
);
ALTER TABLE Imdb ADD UNIQUE INDEX imdb_id_unique (imdb_id ASC) ;

CREATE TABLE IF NOT EXISTS Tmdb (
    movie_id BIGINT PRIMARY KEY,
    tmdb_id BIGINT NOT NULL
);
ALTER TABLE Tmdb ADD UNIQUE INDEX tmdb_id_unique (tmdb_id ASC) ;
