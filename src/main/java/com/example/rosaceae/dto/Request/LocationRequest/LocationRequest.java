package com.example.rosaceae.dto.Request.LocationRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LocationRequest {
    private String latitude;
    private String longitude;
    private int shopID;
}
