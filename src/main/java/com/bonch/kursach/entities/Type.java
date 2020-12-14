package com.bonch.kursach.entities;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "types")
public class Type {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long typeId;

    @Column
    private String title;

}
