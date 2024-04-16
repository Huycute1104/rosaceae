package com.example.rosaceae.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Rate")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int rateId;

    private int rateStar;

    @ManyToOne
    @JoinColumn(name = "ItemId")
    private Item item;

    @ManyToOne
    @JoinColumn(name = "UsersID")
    private User customer;
}
