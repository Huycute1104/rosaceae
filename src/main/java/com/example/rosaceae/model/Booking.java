package com.example.rosaceae.model;

import com.example.rosaceae.enums.BookingStatus;
import com.example.rosaceae.enums.OrderStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;


@Entity
@Table(name = "[Booking]")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BookingID")
    private int BookingId;

    @ManyToOne(cascade = CascadeType.MERGE, optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "UsersID")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonBackReference
    private User customer;

    @ManyToOne(cascade = CascadeType.MERGE, optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "ItemID")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonBackReference
    private Item service;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "BookingDate")
    private Date bookingDate;

    @ManyToOne(cascade = CascadeType.MERGE, optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "TimeID")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonBackReference
    private TimeBooking timeBooking;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;


}
