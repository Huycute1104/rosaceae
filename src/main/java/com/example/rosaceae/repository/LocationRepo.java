package com.example.rosaceae.repository;

import com.example.rosaceae.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LocationRepo extends JpaRepository<Location, Integer> {
    Optional<Location> findByUserUsersID(int id);
    public Location findLocationsByUserUsersID(int userId);
}
