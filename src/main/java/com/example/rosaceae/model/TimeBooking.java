package com.example.rosaceae.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "[TimeBooking]")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TimeBooking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TimeID")
    private int timeID;

    private String time;

    @OneToMany(mappedBy = "timeBooking", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<Booking> bookings = new HashSet<>();


}
