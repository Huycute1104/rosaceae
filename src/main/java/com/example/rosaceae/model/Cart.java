package com.example.rosaceae.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Cart")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CartID")
    private int cartId;

    private int quantity;

    @ManyToOne(cascade = CascadeType.MERGE, optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "ItemID")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonBackReference
    private Item item;

    @ManyToOne(cascade = CascadeType.MERGE, optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "UsersID")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonBackReference
    private User user;

}
