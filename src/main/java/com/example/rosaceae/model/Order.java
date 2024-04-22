package com.example.rosaceae.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "[Order]")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "OrderID")
    private int orderId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "OrderDate", nullable = false)
    private Date orderDate;

    @Column(name = "Total")
    private float total;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @JsonManagedReference
    @JsonIgnore
    private Set<OrderDetail> orderDetails;

    @ManyToOne(cascade = CascadeType.MERGE, optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "VoucherID", nullable = true)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonBackReference
    private Voucher voucher;

    @ManyToOne(cascade = CascadeType.MERGE, optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "CustomerID", nullable = true)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonBackReference
    private User customer;

}
