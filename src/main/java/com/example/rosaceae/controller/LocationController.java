package com.example.rosaceae.controller;

import com.example.rosaceae.dto.Data.LocationDTO;
import com.example.rosaceae.model.Location;
import com.example.rosaceae.model.User;
import com.example.rosaceae.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/api/v1/location")
@RequiredArgsConstructor
public class LocationController {
    private final RestTemplate restTemplate;

    @Value("${google.api.key}")
    private String googleApiKey;

    @Autowired
    private LocationService service;

    @GetMapping("")
//    @PreAuthorize("hasAuthority('admin:read')")
    public List<LocationDTO> getAll() {
        return service.findAll();
    }
}
