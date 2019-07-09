package com.swipejob.matcher.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
public class Worker {
    int rating;
    boolean isActive;
    Set<String> certificates;
    Set<String> skills;

    JobSearchAddress jobSearchAddress;

    String transportation;
    boolean hasDriversLicense;

    List<Availability> availability;

    String phone;

    Name name;

    int age;
    String guid;
    int userId;

}
