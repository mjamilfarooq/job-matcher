package com.swipejob.matcher.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
public class Job {
    boolean driverLicenseRequired;
    Set<String> requiredCertificates;
    Location location;
    Double billRate;
    Integer workersRequired;
    LocalDateTime startDate;
    String about;
    String jobTitle;
    String company;
    String guid;
    int jobId;

    void setBillRate(String rate) {
        billRate = Double.parseDouble(rate.substring(1));
    }

}
