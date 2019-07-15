package com.swipejob.matcher.model.predicate;

import com.swipejob.matcher.model.Job;
import com.swipejob.matcher.model.Worker;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.function.Predicate;

@Data
@NoArgsConstructor
public class JobWithinReach implements Predicate<Job> {
    Worker worker;

    @Override
    public boolean test(Job job) {
        double lon1 = job.getLocation().getLongitude();
        double lon2 = worker.getJobSearchAddress().getLongitude();
        double lat1 = job.getLocation().getLatitude();
        double lat2 = worker.getJobSearchAddress().getLatitude();

        double theta = lon1 - lon2;
        double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
        dist = Math.acos(dist);
        dist = Math.toDegrees(dist);
        dist = dist * 60 * 1.1515;
        dist *= 1.609344;

        return dist < worker.getJobSearchAddress().getMaxJobDistance();
    }

}
