CREATE TABLE "Films" (
    "film_id" int   NOT NULL,
    "name" varchar(40)   NOT NULL,
    "description" varchar(200),
    "release_date" timestamp   NOT NULL,
    "duration" int   NOT NULL,
    "mpa_id" int   NOT NULL,
    CONSTRAINT "pk_Films" PRIMARY KEY (
        "film_id"
     )
);

CREATE TABLE "Users" (
    "user_id" int   NOT NULL,
    "email" varchar(40)   NOT NULL,
    "login" varchar(40)   NOT NULL,
    "name" varchar(40),
    "birthday" timestamp,
    CONSTRAINT "pk_Users" PRIMARY KEY (
        "user_id"
     ),
    CONSTRAINT "uc_Users_email" UNIQUE (
        "email"
    ),
    CONSTRAINT "uc_Users_login" UNIQUE (
        "login"
    )
);

CREATE TABLE "Genres" (
    "genre_id" int   NOT NULL,
    "name_id" varchar(40)   NOT NULL,
    CONSTRAINT "pk_Genres" PRIMARY KEY (
        "genre_id"
     ),
    CONSTRAINT "uc_Genres_name_id" UNIQUE (
        "name_id"
    )
);

CREATE TABLE "FilmGenres" (
    "film_id" int   NOT NULL,
    "genre_id" int   NOT NULL
);

CREATE TABLE "Mpa" (
    "rate_id" int   NOT NULL,
    "name" varchar(10)   NOT NULL,
    CONSTRAINT "pk_Mpa" PRIMARY KEY (
        "rate_id"
     ),
    CONSTRAINT "uc_Mpa_name" UNIQUE (
        "name"
    )
);

CREATE TABLE "Friends" (
    "user_id" int   NOT NULL,
    "friends_id" int   NOT NULL
);

CREATE TABLE "FriendReqestsTo" (
    "user_id" int   NOT NULL,
    "friends_id" int   NOT NULL
);

CREATE TABLE "FriendReqestsFrom" (
    "user_id" int   NOT NULL,
    "friends_id" int   NOT NULL
);

CREATE TABLE "Likes" (
    "films_id" int   NOT NULL,
    "user_id" int   NOT NULL
);

ALTER TABLE "Films" ADD CONSTRAINT "fk_Films_mpa_id" FOREIGN KEY("mpa_id")
REFERENCES "Mpa" ("rate_id");

ALTER TABLE "FilmGenres" ADD CONSTRAINT "fk_FilmGenres_film_id" FOREIGN KEY("film_id")
REFERENCES "Films" ("film_id");

ALTER TABLE "FilmGenres" ADD CONSTRAINT "fk_FilmGenres_genre_id" FOREIGN KEY("genre_id")
REFERENCES "Genres" ("genre_id");

ALTER TABLE "Friends" ADD CONSTRAINT "fk_Friends_user_id" FOREIGN KEY("user_id")
REFERENCES "Users" ("user_id");

ALTER TABLE "Friends" ADD CONSTRAINT "fk_Friends_friends_id" FOREIGN KEY("friends_id")
REFERENCES "Users" ("user_id");

ALTER TABLE "FriendReqestsTo" ADD CONSTRAINT "fk_FriendReqestsTo_user_id" FOREIGN KEY("user_id")
REFERENCES "Users" ("user_id");

ALTER TABLE "FriendReqestsTo" ADD CONSTRAINT "fk_FriendReqestsTo_friends_id" FOREIGN KEY("friends_id")
REFERENCES "Users" ("user_id");

ALTER TABLE "FriendReqestsFrom" ADD CONSTRAINT "fk_FriendReqestsFrom_user_id" FOREIGN KEY("user_id")
REFERENCES "Users" ("user_id");

ALTER TABLE "FriendReqestsFrom" ADD CONSTRAINT "fk_FriendReqestsFrom_friends_id" FOREIGN KEY("friends_id")
REFERENCES "Users" ("user_id");

ALTER TABLE "Likes" ADD CONSTRAINT "fk_Likes_films_id" FOREIGN KEY("films_id")
REFERENCES "Films" ("film_id");

ALTER TABLE "Likes" ADD CONSTRAINT "fk_Likes_user_id" FOREIGN KEY("user_id")
REFERENCES "Users" ("user_id");