package com.example.rosaceae.repository;
import com.example.rosaceae.model.UserReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserReportRepo extends JpaRepository<UserReport, Integer> {
}
