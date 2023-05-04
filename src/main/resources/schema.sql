CREATE TABLE IF NOT EXISTS Films
(
    film_id      int generated by default as identity NOT NULL,
    film_name    varchar(40)                          NOT NULL,
    description  varchar(200),
    release_date DATE                                 NOT NULL,
    duration     int                                  NULL,
    rate         int                                  NULL,
    mpa_id       int                                  NULL,
    CONSTRAINT pk_Films PRIMARY KEY (
                                     film_id
        )
);

CREATE TABLE IF NOT EXISTS Users
(
    user_id   int generated by default as identity NOT NULL,
    email     varchar(40)                          NOT NULL,
    login     varchar(40)                          NOT NULL,
    user_name varchar(40)                          NULL,
    birthday  date                                 NULL,
    CONSTRAINT pk_Users PRIMARY KEY (
                                     user_id
        ),
    CONSTRAINT uc_Users_email UNIQUE (
                                      email
        ),
    CONSTRAINT uc_Users_login UNIQUE (
                                      login
        )
);

CREATE TABLE IF NOT EXISTS Genres
(
    genre_id   int,
    genre_name varchar(40),
    CONSTRAINT pk_Genres PRIMARY KEY (
                                      genre_id
        ),
    CONSTRAINT uc_Genres_name_id UNIQUE (
                                         genre_name
        )
);

CREATE TABLE IF NOT EXISTS FilmGenres
(
    film_id  int,
    genre_id int,
    CONSTRAINT unique_pair UNIQUE (
                                   film_id, genre_id
        )
);

CREATE TABLE IF NOT EXISTS Mpa
(
    mpa_id   int         NOT NULL,
    mpa_name varchar(10) NOT NULL,
    CONSTRAINT pk_Mpa PRIMARY KEY (
                                   mpa_id
        ),
    CONSTRAINT uc_Mpa_rating UNIQUE (
                                     mpa_name
        )
);

CREATE TABLE IF NOT EXISTS Friends
(
    user_id   int,
    friend_id int
);

CREATE TABLE IF NOT EXISTS Likes
(
    film_id int,
    user_id int
);

CREATE TABLE IF NOT EXISTS REVIEWS (
    REVIEW_ID INT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    CONTENT VARCHAR NOT NULL,
    IS_POSITIVE BOOLEAN NOT NULL,
    USER_ID INT NOT NULL,
    FILM_ID INT NOT NULL,
    CONSTRAINT PK_REVIEWS PRIMARY KEY (REVIEW_ID),
    CONSTRAINT FK_REVIEWS_USER_ID FOREIGN KEY (USER_ID) REFERENCES USERS(USER_ID) ON DELETE CASCADE,
    CONSTRAINT FK_REVIEWS_FILM_ID FOREIGN KEY (FILM_ID) REFERENCES FILMS(FILM_ID) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS REVIEWS_LIKES (
    REVIEW_ID INT,
    USER_ID INT,
    IS_LIKE BOOLEAN NOT NULL,
    CONSTRAINT REVIEWS_LIKES_FK FOREIGN KEY (REVIEW_ID) REFERENCES REVIEWS(REVIEW_ID),
  	CONSTRAINT REVIEWS_LIKES_FK_1 FOREIGN KEY (USER_ID) REFERENCES USERS(USER_ID)
);

--Добавление функционала "режиссёры"
CREATE TABLE IF NOT EXISTS Directors
(
    director_id   INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
    director_name VARCHAR(100) UNIQUE                              NOT NULL
);

CREATE TABLE IF NOT EXISTS Film_directors
(
    film_id INT REFERENCES Films (film_id) ON DELETE CASCADE NOT NULL,
    director_id INT REFERENCES Directors (director_id) ON DELETE CASCADE NOT NULL,
    PRIMARY KEY (film_id, director_id)
);
--Добавление функционала "режиссёры"

ALTER TABLE Films
    ADD CONSTRAINT IF NOT EXISTS fk_Films_mpa_id FOREIGN KEY (mpa_id)
        REFERENCES Mpa (mpa_id);

ALTER TABLE FilmGenres
    ADD CONSTRAINT IF NOT EXISTS fk_FilmGenres_film_id FOREIGN KEY (film_id)
        REFERENCES Films (film_id);


ALTER TABLE FilmGenres
    ADD CONSTRAINT IF NOT EXISTS fk_FilmGenres_genre_id FOREIGN KEY (genre_id)
        REFERENCES Genres (genre_id);


ALTER TABLE Friends
    ADD CONSTRAINT IF NOT EXISTS fk_Friends_user_id FOREIGN KEY (user_id)
        REFERENCES Users (user_id);


ALTER TABLE Friends
    ADD CONSTRAINT IF NOT EXISTS fk_Friends_friends_id FOREIGN KEY (friend_id)
        REFERENCES Users (user_id);


ALTER TABLE Likes
    ADD CONSTRAINT IF NOT EXISTS fk_Likes_film_id FOREIGN KEY (film_id)
        REFERENCES Films (film_id);


ALTER TABLE Likes
    ADD CONSTRAINT IF NOT EXISTS fk_Likes_user_id FOREIGN KEY (user_id)
        REFERENCES Users (user_id);

ALTER TABLE FILMS
    ALTER COLUMN FILM_ID RESTART WITH 1;
ALTER TABLE USERS
    ALTER COLUMN USER_ID RESTART WITH 1;