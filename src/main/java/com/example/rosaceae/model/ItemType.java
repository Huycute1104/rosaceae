package com.example.rosaceae.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "ItemType")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ItemTypeId")
    private int itemTypeId;

    @Column(name = "ItemTypeName", length = 30)
    private String itemTypeName;

    @OneToMany(mappedBy = "itemType", cascade = CascadeType.ALL)
    @JsonIgnore
    @JsonManagedReference
    private List<Item> items;
}
