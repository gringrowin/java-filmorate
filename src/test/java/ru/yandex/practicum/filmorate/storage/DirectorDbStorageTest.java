package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.dao.DirectorDbStorage;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql(statements = "DELETE FROM directors")
@Sql(statements = "ALTER TABLE directors ALTER COLUMN director_id RESTART WITH 1")
@Sql(statements = "DELETE FROM film_directors")
@Sql(statements = "DELETE FROM films")
@Sql(statements = "ALTER TABLE films ALTER COLUMN film_id RESTART WITH 1")
public class DirectorDbStorageTest {
    private final DirectorDbStorage directorDbStorage;
    private final FilmService filmService;

    @DisplayName("Проверка метода создания режиссёра")
    @Test
    void shouldCreateDirector() {
        Director director1 = createTestDirector(1);
        directorDbStorage.create(director1);
        Assertions.assertEquals(director1, directorDbStorage.get(1));

        Director director2 = createTestDirector(2);
        directorDbStorage.create(director2);
        Assertions.assertEquals(director2, directorDbStorage.get(2));

        assertThat(directorDbStorage.getAll().size()).isEqualTo(2);
    }

    @DisplayName("Проверка метода обновления режиссёра")
    @Test
    void shouldUpdateDirector() {
        Director director = createTestDirector(1);
        Director directorAsUpdate = createTestDirector(1).withName("Stanley Kubrick");

        directorDbStorage.create(director);
        directorDbStorage.update(directorAsUpdate);

        assertThat(directorDbStorage.get(1)).isEqualTo(directorAsUpdate);
    }

    @DisplayName("Проверка метода обновления режиссёра при передаче несуществующего id")
    @Test
    void shouldNotUpdateDirectorIfIdDoesNotExist() {
        Director director = createTestDirector(1);
        Director directorAsUpdate = createTestDirector(12).withName("Stanley Kubrick");
        directorDbStorage.create(director);

        assertThat(directorDbStorage.update(directorAsUpdate)).isNull();
        assertThat(directorDbStorage.get(1)).isEqualTo(director);
        assertThat(directorDbStorage.get(1)).isNotEqualTo(directorAsUpdate);
    }

    @DisplayName("Проверка метода получения режиссёра по id")
    @Test
    void shouldReturnDirectorById() {
        Director director1 = createTestDirector(1);
        Director director2 = createTestDirector(2).withName("Klim Shipenko");
        Director director3 = createTestDirector(3).withName("Stanley Kubrick");

        directorDbStorage.create(director1);
        directorDbStorage.create(director2);
        directorDbStorage.create(director3);

        assertThat(directorDbStorage.get(1)).isEqualTo(director1);
        assertThat(directorDbStorage.get(2)).isEqualTo(director2);
        assertThat(directorDbStorage.get(3)).isEqualTo(director3);
    }

    @DisplayName("Проверка метода получения режиссёра по несуществующему id")
    @Test
    void shouldReturnDirectorByIdIfIdDoesNotExist() {
        Director director1 = createTestDirector(1);
        Director director2 = createTestDirector(2).withName("Klim Shipenko");

        directorDbStorage.create(director1);
        directorDbStorage.create(director2);

        assertThat(directorDbStorage.getAll().size()).isEqualTo(2);
        assertThat(directorDbStorage.get(3000)).isNull();
    }

    @DisplayName("Проверка метода получения всех режиссёров")
    @Test
    void shouldReturnAllDirectors() {
        Director director1 = createTestDirector(1);
        Director director2 = createTestDirector(2).withName("Klim Shipenko");
        Director director3 = createTestDirector(3).withName("Stanley Kubrick");

        directorDbStorage.create(director1);
        directorDbStorage.create(director2);
        directorDbStorage.create(director3);

        Set<Director> directorCheckList = new HashSet<>();
        directorCheckList.add(director1);
        directorCheckList.add(director2);
        directorCheckList.add(director3);
        Assertions.assertEquals((directorDbStorage.getAll()), directorCheckList);
    }

    @DisplayName("Проверка метода получения всех режиссёров при пустом списке")
    @Test
    void shouldNotReturnAllDirectorsBecauseOfTableDirectorsIsEmpty() {
        assertThat(directorDbStorage.getAll().size()).isEqualTo(0);
    }

    @DisplayName("Проверка метода удаления режиссёра по id")
    @Test
    void shouldDeleteDirectorById() {
        Director director1 = createTestDirector(1);
        Director director2 = createTestDirector(2).withName("Klim Shipenko");
        Director director3 = createTestDirector(3).withName("Stanley Kubrick");

        directorDbStorage.create(director1);
        directorDbStorage.create(director2);
        directorDbStorage.create(director3);

        assertThat(directorDbStorage.getAll().size()).isEqualTo(3);
        assertThat(directorDbStorage.delete(1)).isEqualTo(true);
        assertThat(directorDbStorage.getAll().size()).isEqualTo(2);
    }

    @DisplayName("Проверка метода удаления фильма по несуществующему id")
    @Test
    void shouldNotDeleteAnyFilmByNonExistentId() {
        Director director1 = createTestDirector(1);
        Director director2 = createTestDirector(2).withName("Klim Shipenko");

        directorDbStorage.create(director1);
        directorDbStorage.create(director2);

        assertThat(directorDbStorage.getAll().size()).isEqualTo(2);
        assertThat(directorDbStorage.delete(213)).isEqualTo(false);
        assertThat(directorDbStorage.getAll().size()).isEqualTo(2);
    }

    @DisplayName("Проверка обновления режиссёров по фильму")
    @Test
    void shouldUpdateDirectorsSetByFilm() {
        Film film1 = createTestFilm(1);
        Film film2 = createTestFilm(1);
        Director director1 = createTestDirector(1);
        Director director2 = createTestDirector(2).withName("Klim Shipenko");

        directorDbStorage.create(director1);
        directorDbStorage.create(director2);

        Set<Director> testDirectorSet1 = new HashSet<>();
        testDirectorSet1.add(director1);
        Set<Director> testDirectorSet2 = new HashSet<>();
        testDirectorSet2.add(director2);

        film1.setDirectors(testDirectorSet1);
        film2.setDirectors(testDirectorSet2);
        filmService.create(film1);

        directorDbStorage.updateDirectorsByFilmToStorage(film2);
        Assertions.assertEquals(directorDbStorage.getDirectorsByFilmFromStorage(film1.getId()), testDirectorSet2);
    }

    @DisplayName("Проверка получения режиссёров по фильму")
    @Test
    void shouldReturnDirectorsSetByFilm() {
        Film film = createTestFilm(1);
        Director director1 = createTestDirector(1);
        Director director2 = createTestDirector(2).withName("Klim Shipenko");

        directorDbStorage.create(director1);
        directorDbStorage.create(director2);

        Set<Director> testDirectorSet = new HashSet<>();
        testDirectorSet.add(director1);
        testDirectorSet.add(director2);

        film.setDirectors(testDirectorSet);
        filmService.create(film);

        Assertions.assertEquals(directorDbStorage.getDirectorsByFilmFromStorage(film.getId()), testDirectorSet);
    }

    private Director createTestDirector(int id) {
        return new Director(
                id,
                "Quentin Tarantino"
        );
    }

    private Film createTestFilm(int id) {
        Mpa mpa = new Mpa();
        mpa.setId(3);
        mpa.setName("PG-13");
        Film film = new Film();
        film.setId(id);
        film.setName("Fast and Furious");
        film.setDescription("The Fast and the Furious");
        film.setReleaseDate(LocalDate.of(2000, 6, 18));
        film.setDuration(106);
        film.setMpa(mpa);
        film.setGenres(new HashSet<>());
        film.setDirectors(new HashSet<>());
        return film;
    }
}