package com.example.demo.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "bills")
public class Bill {

    private static final double UNIT_PRICE = 2000; // Constant price per unit

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long billId;

    @Column(name = "bill_name", nullable = false)
    private String billName;

    private String description;

    private Double amount;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties("bills")
    private User user;

    @Column(name = "unit_number", nullable = false)
    private Integer unit;

    private double totalAmount;

    // Constructors
    public Bill() {}

    public Bill(String billName, String description, User user) {
        this.billName = billName;
        this.description = description;
        this.user = user;
    }

    // Getters & Setters
    public Long getBillId() {
        return billId;
    }

    public void setBillId(Long billId) {
        this.billId = billId;
    }

    public String getBillName() {
        return billName;
    }

    public void setBillName(String billName) {
        this.billName = billName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public static double getUnitPrice() {
        return UNIT_PRICE;
    }

    public Integer getUnit() {
        return unit != null ? unit : 0;
    }

    public void setUnit(Integer unit) {
        this.unit = unit;
        recalculateAmounts();
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    // This method recalculates totalAmount and amount from unit * UNIT_PRICE
    private void recalculateAmounts() {
        if (this.unit != null) {
            this.totalAmount = this.unit * UNIT_PRICE;
            this.amount = this.totalAmount;
        } else {
            this.totalAmount = 0;
            this.amount = 0.0;
        }
    }

    // Call recalc before save or update
    @PrePersist
    @PreUpdate
    private void prePersistUpdate() {
        recalculateAmounts();
    }
}
