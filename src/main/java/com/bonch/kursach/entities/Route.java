package com.bonch.kursach.entities;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "routes")
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long routeId;

    @Column
    private String source;

    @Column
    private String destination;

}
