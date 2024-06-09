package com.example.rosaceae.service;

import com.example.rosaceae.dto.Data.LocationDTO;
import com.example.rosaceae.dto.Request.LocationRequest.LocationRequest;

import java.util.List;

public interface LocationService {
    public List<LocationDTO> findAll();
    public void createLocation(LocationRequest locationRequest);
    public List<LocationDTO> findUsersNearby(double currentLat, double currentLon, double radiusKm);
}
