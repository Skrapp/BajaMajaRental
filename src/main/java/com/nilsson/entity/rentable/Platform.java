package com.nilsson.entity.rentable;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "platforms")
public class Platform {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true, length = 30)
    private String name;

    @Column(name = "description", length = 250)
    private String description;

    @Column(name = "rental_rate", nullable = false, precision = 10, scale = 2)
    private double rentalRate;

    @ManyToMany
    @JoinTable(name = "join_platforms_bajamajas",
            joinColumns = {@JoinColumn(name = "platform_id")},
            inverseJoinColumns = {@JoinColumn(name = "bajamaja_id")})
    private List<BajaMaja> bajamajas = new ArrayList<>();


}
