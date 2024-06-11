package com.example.rosaceae.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "OrderDetail")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "OrderDetailID")
    private int orderDetailId;

    @Column(name = "Quantity")
    private int quantity;

    @Column(name = "Price")
    private float price;

    @Column(name = "PriceforShop")
    private float priceForShop;

    @ManyToOne(cascade = CascadeType.MERGE, optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "ItemID")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonBackReference
    private Item item;

    @ManyToOne(cascade = CascadeType.MERGE, optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "OrderID")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonBackReference
    private Order order;
}
