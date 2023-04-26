package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.jdbc.SqlGroup;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.dao.FilmDbStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SqlGroup({
		@Sql(scripts = "classpath:schema.sql",
				config = @SqlConfig(encoding = "UTF-8"),
				executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
		@Sql(scripts = "classpath:create_test_data.sql",
				config = @SqlConfig(encoding = "UTF-8"),
				executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
})
class FilmorateApplicationTests {

	private final FilmDbStorage filmDbStorage;

	@Test
	public void testAddFilmToStorageDb() {
		Film testFilm = new Film();
		testFilm.setName("Robocop4");
		testFilm.setDescription("RobCop4");
		testFilm.setDuration(120);
		testFilm.setReleaseDate(LocalDate.of(1988, 12, 14));
		Mpa mpa = new Mpa();
			mpa.setId(1);
		testFilm.setMpa(mpa);

		Film fromDbFilm = filmDbStorage.add(testFilm);

		assertEquals(4, fromDbFilm.getId());
		assertEquals(testFilm.getName(), fromDbFilm.getName());
		assertEquals(testFilm.getDuration(), fromDbFilm.getDuration());
	}

	@Test
	public void testGetAllFilmsFromStorageDb() {

		Collection<Film> films = filmDbStorage.getAll();

		assertEquals(3, films.size());
	}

	@Test
	public void testGetFilmFromStorageDb() {

		Film film = filmDbStorage.getFilm(1);

		assertEquals(1, film.getId());
	}

	@Test
	public void testUpdateFilmInStorageDb() {

		Film film = filmDbStorage.getFilm(1);
		assertEquals("Robocopy1", film.getName());

		film.setName("Transformer");

		Film film1 = filmDbStorage.update(film);

		assertEquals("Transformer", film1.getName());
	}

	@Test
	public void testAddLikeToFilmInStorageDb() {

		Film film = filmDbStorage.getFilm(3);
		assertEquals(0, film.getLikes());

		filmDbStorage.addLike(3, 1);
		film = filmDbStorage.addLike(3, 2);
		assertEquals(2, film.getLikes());
	}

	@Test
	public void testDeleteLikeToFilmInStorageDb() {

		Film film = filmDbStorage.getFilm(1);
		assertEquals(4, film.getLikes());

		filmDbStorage.deleteLike(1, 1);
		film = filmDbStorage.deleteLike(1, 2);
		assertEquals(2, film.getLikes());
	}

	@Test
	public void testGetPopularFilmsInStorageDb() {

		List<Film> popularFilms = filmDbStorage.getPopularFilms(5);
		assertEquals(3, popularFilms.size());
		assertEquals(1, popularFilms.get(0).getId());
	}



}
