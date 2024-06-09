package com.example.rosaceae.service;

import com.example.rosaceae.dto.Data.LocationDTO;
import com.example.rosaceae.dto.Request.LocationRequest.LocationRequest;
import com.example.rosaceae.dto.Response.LocationResponse.LocationResponse;
import com.example.rosaceae.model.Location;

import java.util.List;

public interface LocationService {
    public List<LocationDTO> findAll();
    public LocationResponse createLocation(LocationRequest locationRequest);
}
