package com.bonch.kursach.repositories;

import com.bonch.kursach.entities.Ship;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShipRepository extends CrudRepository<Ship, Long> { }
