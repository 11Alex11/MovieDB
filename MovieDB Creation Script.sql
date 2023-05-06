DROP DATABASE IF EXISTS MovieDB;
CREATE DATABASE MovieDB;
Use MovieDB;

CREATE TABLE Movie(
id INT PRIMARY KEY AUTO_INCREMENT,
imdbId VARCHAR(9),
title VARCHAR(50) NOT NULL,
description VARCHAR(1000),
plot VARCHAR(200),
releaseDate DATE,
contentRating VARCHAR(6),
length INT,
userRating FLOAT,
popularity INT,
imageUrl VARCHAR(200),
videoUrl VARCHAR(100)
);

CREATE TABLE Genre(
id INT PRIMARY KEY AUTO_INCREMENT,
name VARCHAR(20)
);

CREATE TABLE MovieGenre(
movieId INT NOT NULL,
genreId INT NOT NULL,
FOREIGN KEY(movieId) REFERENCES Movie(id),
FOREIGN KEY(genreId) REFERENCES Genre(id)
);

CREATE TABLE  CastMember(
id INT PRIMARY KEY AUTO_INCREMENT,
imdbId VARCHAR(9),
name VARCHAR(50) NOT NULL,
imageUrl VARCHAR(200),
birthDate DATE,
birthPlace VARCHAR(50),
bio VARCHAR(1000),
bioFull VARCHAR(10000)
);

CREATE TABLE MovieCastMember(
movieId int NOT NULL,
castMemberId int NOT NULL,
role VARCHAR(100) NOT NULL,
roleRelevance int DEFAULT 0,
PRIMARY KEY (movieId, castMemberId,role),
FOREIGN KEY(movieId) REFERENCES Movie(id),
FOREIGN KEY(castMemberId) REFERENCES CastMember(id)
);





