package com.example.demo.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "bills")
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long billId;

    private String description;

    private Double amount;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties("bills") // Hii inazuia nested cycle kwenye serialization
    private User user;

    // Constructors
    public Bill() {}

    public Bill(String description, Double amount, User user) {
        this.description = description;
        this.amount = amount;
        this.user = user;
    }

    // Getters & Setters
    public Long getBillId() {
        return billId;
    }

    public void setBillId(Long billId) {
        this.billId = billId;
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

    public void setUnitprice(float f) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setUnitprice'");
    }
}