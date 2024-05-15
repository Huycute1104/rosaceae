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
@Table(name = "RankMember")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RankMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RankMemberID")
    private int rankMemberID;

    @Column(name = "RankName",length = 20,unique = true)
    private String rankName;
    @Column(name = "RankPoint")
    private int rankPoint = 0;



//    @OneToMany(mappedBy = "rankMember", cascade = CascadeType.ALL)
//    @JsonIgnore
//    @JsonManagedReference
//    private List<User> users;
}
