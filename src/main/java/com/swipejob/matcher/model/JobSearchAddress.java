package com.swipejob.matcher.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class JobSearchAddress {
    String unit;
    int maxJobDistance;
    double longitude;
    double latitude;
}
