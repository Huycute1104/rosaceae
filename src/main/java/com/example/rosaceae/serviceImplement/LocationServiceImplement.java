package com.example.rosaceae.serviceImplement;

import com.example.rosaceae.dto.Data.LocationDTO;
import com.example.rosaceae.dto.Request.LocationRequest.LocationRequest;
import com.example.rosaceae.dto.Response.LocationResponse.LocationResponse;

import com.example.rosaceae.model.Location;
import com.example.rosaceae.repository.LocationRepo;
import com.example.rosaceae.repository.UserRepo;
import com.example.rosaceae.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class LocationServiceImplement implements LocationService {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private LocationRepo locationRepo;


    @Value("${google.api.key}")
    private String googleApiKey;


    @Override
    public List<LocationDTO> findAll() {
        return locationRepo.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }


    @Override
    public void createLocation(LocationRequest locationRequest) {
        var shop = userRepo.findUserByUsersID(locationRequest.getShopID()).orElse(null);
        if (shop == null) {
            LocationResponse.builder()
                    .location(null)
                    .status("Shop Not Found")
                    .build();
        } else {
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

    @Override
    public LocationResponse AddCoordinates(String url, int userId) throws IOException {
        var user = userRepo.findUserByUsersID(userId).orElse(null);
        if (user == null) {
            return LocationResponse.builder()
                    .status("User Not Found")
                    .location(null)
                    .build();
        }
        var check = locationRepo.findLocationsByUserUsersID(userId);
        if(check != null){
            return LocationResponse.builder()
                    .status("Location Already Exists For This Shop")
                    .location(null)
                    .build();
        }
        String fullUrl = isShortenedUrl(url) ? followRedirect(url) : url;
        if (StringUtils.hasText(fullUrl)) {
            Pattern pattern = Pattern.compile("@(-?\\d+\\.\\d+),(-?\\d+\\.\\d+)");
            Matcher matcher = pattern.matcher(fullUrl);
            if (matcher.find()) {
                String latitude = matcher.group(1);
                String longitude = matcher.group(2);
                Location location = Location.builder()
                        .user(user)
                        .latitude(latitude)
                        .longitude(longitude)
                        .build();
                locationRepo.save(location);
                return LocationResponse.builder()
                        .status("Create Location Successful")
                        .location(location)
                        .build();
            }
        }
        return LocationResponse.builder()
                .status("Create Location Fail")
                .location(null)
                .build();
    }

    private boolean isShortenedUrl(String url) {
        return url.contains("goo.gl");
    }

    private String followRedirect(String url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setInstanceFollowRedirects(false);
        String redirectUrl = connection.getHeaderField("Location");
        connection.disconnect();

        if (redirectUrl != null) {
            return redirectUrl;
        }
        return url;
    }
}
