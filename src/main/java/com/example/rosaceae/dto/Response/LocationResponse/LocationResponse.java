package com.example.rosaceae.dto.Response.LocationResponse;

import com.example.rosaceae.model.Location;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LocationResponse {
    private String status;
    private Location location;
}
