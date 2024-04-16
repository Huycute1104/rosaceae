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
    @JoinColumn(name = "CustomerID")
    private User customer;

    @ManyToOne
    @JoinColumn(name = "ShopID")
    private User shop;
}
