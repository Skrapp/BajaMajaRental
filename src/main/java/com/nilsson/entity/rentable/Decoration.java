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

    @Column(name = "rental_rate", nullable = false)
    private double rentalRate;

    @Enumerated(EnumType.STRING)
    @Column(name = "color")
    private Color color;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, updatable = false)
    private final RentalObject type = RentalObject.DECORATION;

    protected Decoration() {
    }

    public Decoration(String name, double rentalRate, Color color) {
        this.name = name;
        this.rentalRate = rentalRate;
        this.color = color;
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

    public double getRentalRate() {
        return rentalRate;
    }

    public void setRentalRate(double rentalRate) {
        this.rentalRate = rentalRate;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public RentalObject getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Decoration{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", rentalRate=" + rentalRate +
                ", color=" + color +
                ", type=" + type +
                '}';
    }
}
