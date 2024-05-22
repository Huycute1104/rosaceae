package com.example.rosaceae.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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

    private int commentCount;

    private int countUsage;

    private int quantity;

    private int discount;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    @JsonManagedReference
    @JsonIgnore
    private List<Cart> cart;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    @JsonManagedReference
    @JsonIgnore
    private List<OrderDetail> orderDetails;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    @JsonManagedReference
    @JsonIgnore
    private List<ItemImages> itemImages;

    @ManyToOne(cascade = CascadeType.MERGE, optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "UsersID")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonBackReference
    private User user;

    @ManyToOne(cascade = CascadeType.MERGE, optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "ItemTypeId")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonBackReference
    private ItemType itemType;

    @ManyToOne(cascade = CascadeType.MERGE, optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "CategoryId")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonBackReference
    private Category category;
}
