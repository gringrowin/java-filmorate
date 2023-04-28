package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;
import ru.yandex.practicum.filmorate.storage.dao.MpaDbStorage;


import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/mpa")
public class MpaController {

    private final MpaStorage mpaStorage;

    @Autowired
    public MpaController(MpaDbStorage mpaStorage) {
        this.mpaStorage = mpaStorage;
    }

    @GetMapping
    public Collection<Mpa> getAll() {
        log.info("findAll - Started");
        Collection<Mpa> allMpa = mpaStorage.getAll();
        log.info("findAll: {} - Finished", allMpa);
        return allMpa;
    }

    @GetMapping("/{mpaId}")
    public Mpa getMpa(@PathVariable("mpaId") Integer mpaId) {
        log.info("getMpa - Started");
        Mpa mpa = mpaStorage.getMpa(mpaId);
        log.info("getMpa: {} - Finished", mpa);
        return mpa;
    }

}
