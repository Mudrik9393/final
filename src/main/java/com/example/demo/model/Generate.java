package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "generate")
public class Generate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "control_number", nullable = false, unique = true)
    private String controlNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Constructors
    public Generate() {
    }

    public Generate(String controlNumber, User user) {
        this.controlNumber = controlNumber;
        this.user = user;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public String getControlNumber() {
        return controlNumber;
    }

    public void setControlNumber(String controlNumber) {
        this.controlNumber = controlNumber;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
