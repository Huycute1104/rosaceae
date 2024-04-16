package com.example.rosaceae.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "Feedback")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FeedbackID")
    private Integer feedBackId;

    @Column(name = "Feedback", length = 500)
    private String feedback;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FeedbackAt")
    private Date feedBackAt;

    @ManyToOne
    @JoinColumn(name = "UserID")
    private User user;

    @ManyToOne
    @JoinColumn(name = "ItemID")
    private Item item;
}
