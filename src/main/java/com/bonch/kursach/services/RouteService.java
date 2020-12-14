package com.bonch.kursach.services;

import com.bonch.kursach.entities.Route;
import com.bonch.kursach.repositories.RouteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class RouteService {

    @Autowired
    private RouteRepository routeRepository;

    public List<Route> getAll() {
        return StreamSupport
                .stream(routeRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    public void save(Route route) {
        routeRepository.save(route);
    }

    public void delete(Route route) {
        routeRepository.delete(route);
    }

}
