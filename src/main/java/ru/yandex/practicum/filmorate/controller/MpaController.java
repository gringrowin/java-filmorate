package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.dao.MpaDbStorage;


import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/mpa")
public class MpaController {

    private final MpaDbStorage mpaDbStorage;

    @Autowired
    public MpaController(MpaDbStorage mpaDbStorage) {
        this.mpaDbStorage = mpaDbStorage;
    }

    @GetMapping
    public Collection<Mpa> getAll() {
        log.info("findAll - Started");
        Collection<Mpa> allMpa = mpaDbStorage.getAll();
        log.info("findAll: {} - Finished", allMpa);
        return allMpa;
    }

    @GetMapping("/{mpaId}")
    public Mpa getMpa(@PathVariable("mpaId") Integer mpaId) {
        log.info("getMpa - Started");
        Mpa mpa = mpaDbStorage.getMpa(mpaId);
        log.info("getMpa: {} - Finished", mpa);
        return mpa;
    }

}
