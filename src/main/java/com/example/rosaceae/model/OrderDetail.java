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
    private Integer orderDetailId;

    @ManyToOne
    @JoinColumn(name = "ItemID")
    private Item item;

    @Column(name = "Quantity")
    private Integer quantity;

    @Column(name = "Price")
    private Float price;

    @ManyToOne
    @JoinColumn(name = "OrderID")
    @ToString.Exclude
    @JsonBackReference
    private Order order;
}
