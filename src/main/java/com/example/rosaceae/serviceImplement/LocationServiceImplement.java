package com.example.rosaceae.serviceImplement;

import com.example.rosaceae.dto.Data.LocationDTO;
import com.example.rosaceae.dto.Request.LocationRequest.LocationRequest;
import com.example.rosaceae.dto.Response.LocationResponse.LocationResponse;
import com.example.rosaceae.model.Location;
import com.example.rosaceae.repository.LocationRepo;
import com.example.rosaceae.repository.UserRepo;
import com.example.rosaceae.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LocationServiceImplement implements LocationService {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private LocationRepo locationRepo;

    @Override
    public List<LocationDTO> findAll() {
        return locationRepo.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private LocationDTO mapToDTO(Location location) {
        return LocationDTO.builder()
                .locationID(location.getLocationID())
                .latitude(location.getLatitude())
                .longitude(location.getLongitude())
                .usersID(location.getUser().getUsersID())
                .accountName(location.getUser().getAccountName())
                .email(location.getUser().getEmail())
                .phone(location.getUser().getPhone())
                .address(location.getUser().getAddress())
                .rate(location.getUser().getRate())
                .userStatus(location.getUser().isUserStatus())
                .coverImages(location.getUser().getCoverImages())
                .build();
    }




    @Override
    public LocationResponse createLocation(LocationRequest locationRequest) {
        var shop = userRepo.findUserByUsersID(locationRequest.getShopID()).orElse(null);
        if(shop == null) {
            return LocationResponse.builder()
                    .location(null)
                    .status("Shop Not Found")
                    .build();
        }else {
            Location location = Location.builder()
                    .user(shop)
                    .latitude(locationRequest.getLatitude())
                    .longitude(locationRequest.getLongitude())
                    .build();
            locationRepo.save(location);
            return LocationResponse.builder()
                    .location(null)
                    .status("Create Location Successful")
                    .build();
        }
    }
}
