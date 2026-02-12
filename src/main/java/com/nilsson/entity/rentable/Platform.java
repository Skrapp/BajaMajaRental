package com.nilsson.entity.rentable;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "join_platforms_bajamajas",
            joinColumns = {@JoinColumn(name = "platform_id")},
            inverseJoinColumns = {@JoinColumn(name = "bajamaja_id")})
    private Set<BajaMaja> bajamajas = new HashSet<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, updatable = false)
    private final RentalObject type = RentalObject.PLATFORM;

    protected Platform() {
    }

    public Platform(String name, String description, double rentalRate) {
        this.name = name;
        this.description = description;
        this.rentalRate = rentalRate;
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

    public Set<BajaMaja> getBajamajas() {
        return bajamajas;
    }

    public void setBajamajas(Set<BajaMaja> bajamajas) {
        this.bajamajas = bajamajas;
    }

    public void addBajaMaja(BajaMaja bajaMaja){
        this.bajamajas.add(bajaMaja);
    }

    public RentalObject getType() {
        return type;
    }

    @Override
    public String toString() {
        return type +"{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", rentalRate=" + rentalRate +
                '}';
    }
}
