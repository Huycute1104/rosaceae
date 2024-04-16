package com.example.rosaceae.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Item")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ItemId")
    private int itemId;

    @Column(name = "ItemName", length = 30)
    private String itemName;

    @Column(name = "ItemPrice")
    private Float itemPrice;

    @Column(name = "ItemDescription", length = 300)
    private String itemDescription;

    private Float itemRate;

    private Integer commentCount;

    private Integer countUsage;

    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "UsersID")
    private User user;

    @ManyToOne
    @JoinColumn(name = "ItemTypeId")
    private ItemType itemType;
}
