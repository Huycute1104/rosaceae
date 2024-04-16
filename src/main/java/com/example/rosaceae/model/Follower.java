package com.example.rosaceae.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Category")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Follower {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FollowerID")
    private Integer followerId;

    @ManyToOne
    @JoinColumn(name = "CustomerID")  // Reference to the User acting as a customer
    private User customer;

    @ManyToOne
    @JoinColumn(name = "ShopID")  // Reference to the User acting as a shop
    private User shop;
}
