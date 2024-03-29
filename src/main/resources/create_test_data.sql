DELETE from FILM_GENRES;

DELETE from FRIENDS;

DELETE from GENRES;

DELETE from LIKES;

DELETE from FILMS;

DELETE from MPA;

DELETE from USERS;


insert into GENRES (GENRE_ID, GENRE_NAME) VALUES (1, 'Комедия');
insert into GENRES (GENRE_ID, GENRE_NAME) VALUES (2, 'Драма');
insert into GENRES (GENRE_ID, GENRE_NAME) VALUES (3, 'Мультфильм');
insert into GENRES (GENRE_ID, GENRE_NAME) VALUES (4, 'Триллер');
insert into GENRES (GENRE_ID, GENRE_NAME) VALUES (5, 'Документальный');
insert into GENRES (GENRE_ID, GENRE_NAME) VALUES (6, 'Боевик');

INSERT INTO MPA (MPA_ID, MPA_NAME) VALUES (1, 'G');
INSERT INTO MPA (MPA_ID, MPA_NAME) VALUES (2, 'PG');
INSERT INTO MPA (MPA_ID, MPA_NAME) VALUES (3, 'PG-13');
INSERT INTO MPA (MPA_ID, MPA_NAME) VALUES (4, 'R');
INSERT INTO MPA (MPA_ID, MPA_NAME) VALUES (5, 'NC-17');

INSERT INTO FILMS (FILM_NAME, DESCRIPTION, RELEASE_DATE, DURATION, RATE, MPA_ID)
VALUES ( 'Robocopy1', 'RobCop1', '1989-04-17', 120, 4, 1);
INSERT INTO FILMS (FILM_NAME, DESCRIPTION, RELEASE_DATE, DURATION, RATE, MPA_ID)
VALUES ( 'Robocopy', 'RobCop2', '1989-04-17', 125, 4, 2);
INSERT INTO FILMS (FILM_NAME, DESCRIPTION, RELEASE_DATE, DURATION, RATE, MPA_ID)
VALUES ( 'Robocopy', 'RobCop3', '1989-04-17', 130, 4, 3);

INSERT INTO USERS (EMAIL, LOGIN, USER_NAME, BIRTHDAY) VALUES ( 'mail@mail.ru', 'login', 'Max', '1987-10-14');
INSERT INTO USERS (EMAIL, LOGIN, USER_NAME, BIRTHDAY) VALUES ( 'mail1@mail.ru', 'login1', 'Max1', '1987-10-14');
INSERT INTO USERS (EMAIL, LOGIN, USER_NAME, BIRTHDAY) VALUES ( 'mail2@mail.ru', 'login2', 'Max2', '1987-10-14');
INSERT INTO USERS (EMAIL, LOGIN, USER_NAME, BIRTHDAY) VALUES ( 'mail3@mail.ru', 'login3', 'Max3', '1987-10-14');

INSERT INTO FILM_GENRES (FILM_ID, GENRE_ID) VALUES ( 1, 2);
INSERT INTO FILM_GENRES (FILM_ID, GENRE_ID) VALUES ( 1, 3);
INSERT INTO FILM_GENRES (FILM_ID, GENRE_ID) VALUES ( 1, 5);
INSERT INTO FILM_GENRES (FILM_ID, GENRE_ID) VALUES ( 2, 1);

INSERT INTO LIKES (FILM_ID, USER_ID) VALUES ( 1, 1 );
INSERT INTO LIKES (FILM_ID, USER_ID) VALUES ( 1, 2 );
INSERT INTO LIKES (FILM_ID, USER_ID) VALUES ( 1, 3 );
INSERT INTO LIKES (FILM_ID, USER_ID) VALUES ( 1, 4 );
INSERT INTO LIKES (FILM_ID, USER_ID) VALUES ( 2, 1 );
INSERT INTO LIKES (FILM_ID, USER_ID) VALUES ( 2, 3 );
INSERT INTO LIKES (FILM_ID, USER_ID) VALUES ( 2, 4 );

INSERT INTO FRIENDS (USER_ID, FRIEND_ID) VALUES ( 1, 2 );
INSERT INTO FRIENDS (USER_ID, FRIEND_ID) VALUES ( 2, 1 );
INSERT INTO FRIENDS (USER_ID, FRIEND_ID) VALUES ( 3, 1 );
INSERT INTO FRIENDS (USER_ID, FRIEND_ID) VALUES ( 3, 2 );

