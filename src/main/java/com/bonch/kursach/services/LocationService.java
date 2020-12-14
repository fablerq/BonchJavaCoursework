package com.bonch.kursach.services;

import com.bonch.kursach.entities.Location;
import com.bonch.kursach.repositories.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class LocationService {

    @Autowired
    private LocationRepository locationRepository;

    public List<Location> getAll() {
        return StreamSupport
                .stream(locationRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    public void save(Location location) {
        locationRepository.save(location);
    }

    public void delete(Location location) {
        locationRepository.delete(location);
    }

}


