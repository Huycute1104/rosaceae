package com.example.rosaceae.dto.Data;

import com.example.rosaceae.model.Location;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocationDTO {
    private int locationID;
    private String latitude;
    private String longitude;
    private int usersID;
    private String accountName;
    private String email;
    private String phone;
    private String address;
    private float rate;
    private boolean userStatus;
    private String coverImages;
    private double distance;
    private String locationUrl;
}
