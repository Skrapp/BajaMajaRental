package com.nilsson.entity.rentable;

import jakarta.persistence.*;

@Entity
@Table(name = "decorations")
public class Decoration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true, length = 30)
    private String name;

    @Column(name = "description", length = 250)
    private String description;

    @Column(name = "rental_rate", nullable = false, precision = 10, scale = 2)
    private double rentalRate;

}
