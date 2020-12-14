package com.bonch.kursach.services;

import com.bonch.kursach.entities.Country;
import com.bonch.kursach.repositories.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class CountryService {

    @Autowired
    private CountryRepository countryRepository;

    public List<Country> getAll() {
        return StreamSupport
                .stream(countryRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    public void save(Country ship) {
        countryRepository.save(ship);
    }

    public void delete(Country ship) {
        countryRepository.delete(ship);
    }

}
