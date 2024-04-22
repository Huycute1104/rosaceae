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
@Table(name = "Reports")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ReportId")
    private int reportId;

    @Column(name = "ReportName", length = 20)
    private String reportName;

    @OneToMany(mappedBy = "report", cascade = CascadeType.ALL)
    @JsonIgnore
    @JsonManagedReference
    private List<UserReport> userReports;
}
