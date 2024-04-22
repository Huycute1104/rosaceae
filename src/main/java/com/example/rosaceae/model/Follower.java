package com.example.rosaceae.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Follower")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Follower {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FollowerID")
    private int followerId;

    @ManyToOne(cascade = CascadeType.MERGE, optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "CustomerID")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonBackReference
    private User customer;

    @ManyToOne(cascade = CascadeType.MERGE, optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "ShopID")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonBackReference
    private User shop;
}
