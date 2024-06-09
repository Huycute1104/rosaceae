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

import java.util.Comparator;
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


    @Override
    public void createLocation(LocationRequest locationRequest) {
        var shop = userRepo.findUserByUsersID(locationRequest.getShopID()).orElse(null);
        if(shop == null) {
            LocationResponse.builder()
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
            LocationResponse.builder()
                    .location(null)
                    .status("Create Location Successful")
                    .build();
        }
    }

    @Override
    public List<LocationDTO> findUsersNearby(double currentLat, double currentLon, double radiusKm) {
        return locationRepo.findAll().stream()
                .map(location -> {
                    double distance = DistanceUtil.calculateDistance(
                            currentLat, currentLon,
                            Double.parseDouble(location.getLatitude()),
                            Double.parseDouble(location.getLongitude())
                    );
                    LocationDTO dto = mapToDTO(location);
                    dto.setDistance(distance);
                    return dto;
                })
                .filter(locationDTO -> locationDTO.getDistance() <= radiusKm)
                .sorted(Comparator.comparingDouble(LocationDTO::getDistance))
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
                .locationUrl(location.getUser().getLocationUrl())
                .build();
    }

    public class DistanceUtil {

        private static final int EARTH_RADIUS = 6371;

        public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
            double dLat = Math.toRadians(lat2 - lat1);
            double dLon = Math.toRadians(lon2 - lon1);
            double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                    Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                            Math.sin(dLon / 2) * Math.sin(dLon / 2);
            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
            return EARTH_RADIUS * c;
        }
    }


}
