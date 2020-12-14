package com.bonch.kursach.services;

import com.bonch.kursach.entities.Type;
import com.bonch.kursach.repositories.TypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class TypeService {

    @Autowired
    private TypeRepository typeRepository;

    public List<Type> getAll() {
        return StreamSupport
                .stream(typeRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    public void save(Type ship) {
        typeRepository.save(ship);
    }

    public void delete(Type ship) {
        typeRepository.delete(ship);
    }
}
