package com.nilsson.entity.rentable;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "BajaMajas")
public class BajaMaja {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 30)
    private String name;

    @Column(name = "description", length = 250)
    private String description;

    @Column(name = "rental_rate", nullable = false)
    private double rentalRate;

    @Column(name = "number_of_stalls", nullable = false)
    private int numberOfStalls;

    @Column(name = "handicap")
    private boolean handicap;

    /*@ManyToMany
    @JoinTable(name = "join_platforms_bajamajas",
            joinColumns = {@JoinColumn(name = "bajamaja_id")},
            inverseJoinColumns = {@JoinColumn(name = "platform_id")})
    private List<Platform> platforms;*/

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, updatable = false)
    private RentalObject type = RentalObject.BAJAMAJA;

    public BajaMaja(String name, String description, double rentalRate, int numberOfStalls, boolean handicap) {
        this.name = name;
        this.description = description;
        this.rentalRate = rentalRate;
        this.numberOfStalls = numberOfStalls;
        this.handicap = handicap;
    }

    public BajaMaja(String name, double rentalRate, int numberOfStalls) {
        this.name = name;
        this.rentalRate = rentalRate;
        this.numberOfStalls = numberOfStalls;
    }

    protected BajaMaja() {
    }

    public void setId(Long id) {
        this.id = id;
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

    public int getNumberOfStalls() {
        return numberOfStalls;
    }

    public void setNumberOfStalls(int numberOfStalls) {
        this.numberOfStalls = numberOfStalls;
    }

    public boolean isHandicap() {
        return handicap;
    }

    public void setHandicap(boolean handicap) {
        this.handicap = handicap;
    }

    public RentalObject getType() {
        return type;
    }

    public void setType(RentalObject rentalObjectType) {
        this.type = rentalObjectType;
    }

    @Override
    public String toString() {
        return type + "{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", rentalRate=" + rentalRate +
                ", numberOfStalls=" + numberOfStalls +
                ", handicap=" + handicap +
                '}';
    }
}
