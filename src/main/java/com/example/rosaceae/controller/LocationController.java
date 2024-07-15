package com.example.rosaceae.controller;

import com.example.rosaceae.dto.Data.LocationDTO;
import com.example.rosaceae.dto.Data.ShopLocation;
import com.example.rosaceae.dto.Response.LocationResponse.LocationResponse;
import com.example.rosaceae.model.Location;
import com.example.rosaceae.model.User;
import com.example.rosaceae.service.LocationService;
import com.google.maps.GeoApiContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    @GetMapping("shopLocation/{shopId}")
    public ResponseEntity<List<ShopLocation>> viewLocationOfShop(@PathVariable int shopId) {
        List<ShopLocation> locations = service.ViewLocationOfShop(shopId);
        return ResponseEntity.ok(locations);
    }

    @GetMapping("/nearby-spa")
    public List<LocationDTO> getNearbyLocations(
            @RequestParam double latitude,
            @RequestParam double longitude,
            @RequestParam(defaultValue = "50") double radiusKm) {
        return service.findUsersNearby(latitude, longitude, radiusKm);
    }
    @PostMapping("/add-coordinates")
    public ResponseEntity<LocationResponse> addCoordinates(@RequestParam String url, @RequestParam int userId) {
        try {
            LocationResponse response = service.AddCoordinates(url, userId);
            if (response.getStatus().equals("Create Location Successful")) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @PutMapping("/{userId}")
    public ResponseEntity<LocationResponse> updateLocationForShop(@PathVariable int userId, @RequestParam String url) {
        try {
            LocationResponse response = service.UpdateLocationForShop(url, userId);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(LocationResponse.builder()
                    .status("Error: " + e.getMessage())
                    .location(null)
                    .build());
        }
    }

    //API test
    @GetMapping("/get-coordinates")
    public String getCoordinates(@RequestParam String url) throws IOException {
        String fullUrl = isShortenedUrl(url) ? followRedirect(url) : url;
        if (StringUtils.hasText(fullUrl)) {
            Pattern pattern = Pattern.compile("@(-?\\d+\\.\\d+),(-?\\d+\\.\\d+)");
            Matcher matcher = pattern.matcher(fullUrl);
            if (matcher.find()) {
                String latitude = matcher.group(1);
                String longitude = matcher.group(2);
                return "A(" + latitude + "," + longitude + ")";
            }
        }
        return "Location Not Found";
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

    @GetMapping("/calculate-distance")
    public String calculateDistance(@RequestParam double lat1, @RequestParam double lon1,
                                    @RequestParam double lat2, @RequestParam double lon2) {
        double distance = haversine(lat1, lon1, lat2, lon2);
        return String.format("Distance: %.2f km", distance);
    }

    private static double haversine(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Bán kính trái đất tính bằng km
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
}
