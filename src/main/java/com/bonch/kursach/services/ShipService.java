package com.bonch.kursach.services;

import com.bonch.kursach.entities.Ship;
import com.bonch.kursach.repositories.ShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ShipService {

    @Autowired
    private ShipRepository shipRepository;

    public List<Ship> getAll() {
        return StreamSupport
                .stream(shipRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    public void save(Ship ship) {
        shipRepository.save(ship);
    }

    public void delete(Ship ship) {
        shipRepository.delete(ship);
    }

}
