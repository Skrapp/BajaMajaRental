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

    @Column(name = "rental_rate", nullable = false)
    private double rentalRate;

    /*@ManyToMany
    @JoinTable(name = "join_platforms_bajamajas",
            joinColumns = {@JoinColumn(name = "platform_id")},
            inverseJoinColumns = {@JoinColumn(name = "bajamaja_id")})
    private List<BajaMaja> bajamajas = new ArrayList<>();*/

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, updatable = false)
    private final RentalObject type = RentalObject.PLATFORM;

    public Platform() {
    }

    public Platform(String name, String description, double rentalRate, List<BajaMaja> bajamajas) {
        this.name = name;
        this.description = description;
        this.rentalRate = rentalRate;
        //this.bajamajas = bajamajas;
    }

    public Platform(String name, double rentalRate) {
        this.name = name;
        this.rentalRate = rentalRate;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getRentalRate() {
        return rentalRate;
    }

    public void setRentalRate(double rentalRate) {
        this.rentalRate = rentalRate;
    }

    /*public List<BajaMaja> getBajamajas() {
        return bajamajas;
    }

    public void setBajamajas(List<BajaMaja> bajamajas) {
        this.bajamajas = bajamajas;
    }*/

    public RentalObject getType() {
        return type;
    }
}
