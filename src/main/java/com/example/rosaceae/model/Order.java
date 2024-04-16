package com.example.rosaceae.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "Order")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "OrderID")
    private Integer orderId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "OrderDate", nullable = false)
    private Date orderDate;

    @Column(name = "Total")
    private Float total;

    @ManyToOne
    @JoinColumn(name = "CustomerID")
    private User customer;

    @JsonIgnore
    @OneToMany(mappedBy = "order")
    private Set<OrderDetail> orderDetails;

    @ManyToOne
    @JoinColumn(name = "VoucherID", nullable = true)
    private Voucher voucher;

}
