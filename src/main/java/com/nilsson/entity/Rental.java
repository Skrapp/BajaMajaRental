package com.nilsson.entity;

import com.nilsson.entity.rentable.RentalObject;
import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Entity
@Table(name = "rentals")
public class Rental {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Enumerated(EnumType.STRING)
    @Column(name = "rental_object_type", nullable = false)
    private RentalObject rentalObjectType;

    @Column(name = "rental_object_id", nullable = false)
    private Long rentalObjectId;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    @Column(name = "return_date")
    private LocalDateTime returnDate;

    //TODO använd dailyRate istället
    @Column(name = "dailyRate", nullable = false)
    private Double dailyRate;

    @Column(name = "finalPayment")
    private Double finalPayment;

    protected Rental() {
    }

    public Rental(Customer customer, RentalObject rentalObjectType, Long rentalObjectId, LocalDateTime startDate, LocalDateTime endDate, Double dailyRate) {
        this.customer = customer;
        this.rentalObjectType = rentalObjectType;
        this.rentalObjectId = rentalObjectId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.dailyRate = dailyRate;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public RentalObject getRentalObjectType() {
        return rentalObjectType;
    }

    public void setRentalObjectType(RentalObject rentalObjectType) {
        this.rentalObjectType = rentalObjectType;
    }

    public Long getRentalObjectId() {
        return rentalObjectId;
    }

    public void setRentalObjectId(Long rentalObjectId) {
        this.rentalObjectId = rentalObjectId;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public LocalDateTime getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDateTime returnDate) {
        this.returnDate = returnDate;
    }

    public Double getDailyRate() {
        return dailyRate;
    }

    public void setDailyRate(Double dailyRate) {
        this.dailyRate = dailyRate;
    }

    public Double getFinalPayment() {
        return finalPayment;
    }

    public void setFinalPayment(Double finalPayment) {
        this.finalPayment = finalPayment;
    }

    @Override
    public String toString() {
        return "Rental{" +
                "id=" + id +
                ", customer=" + customer +
                ", rentalObjectType=" + rentalObjectType +
                ", rentalObjectId=" + rentalObjectId +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", returnDate=" + returnDate +
                ", price=" + dailyRate +
                '}';
    }
}
