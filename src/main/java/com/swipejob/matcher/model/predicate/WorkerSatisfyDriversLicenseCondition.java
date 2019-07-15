package com.swipejob.matcher.model.predicate;

import com.swipejob.matcher.model.Job;
import com.swipejob.matcher.model.Worker;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.function.Predicate;

@Data
@NoArgsConstructor
public class WorkerSatisfyDriversLicenseCondition implements Predicate<Job> {
    Worker worker;

    @Override
    public boolean test(Job job) {
        return !job.isDriverLicenseRequired() || worker.isHasDriversLicense();
    }
}
