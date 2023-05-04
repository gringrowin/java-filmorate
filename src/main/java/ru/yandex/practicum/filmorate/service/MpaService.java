package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.List;

@Service
@Slf4j
public class MpaService {
    private final MpaStorage mpaStorage;

    @Autowired
    public MpaService(MpaStorage mpaStorage) {
        this.mpaStorage = mpaStorage;
    }

    public List<Mpa> getAll() {
        List<Mpa> mpa = mpaStorage.getAll();
        log.info("findAll: {}", mpa);
        return mpa;
    }

    public Mpa getMpa(Integer mpaId) {
        Mpa mpa = mpaStorage.getMpa(mpaId);
        log.info("getMpa: {} - ", mpa);
        return mpa;
    }
}
