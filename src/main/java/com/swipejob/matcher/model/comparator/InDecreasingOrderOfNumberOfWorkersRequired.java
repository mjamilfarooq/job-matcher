package com.swipejob.matcher.model.comparator;

import com.swipejob.matcher.model.Job;
import lombok.NoArgsConstructor;

import java.util.Comparator;

@NoArgsConstructor
public class InDecreasingOrderOfNumberOfWorkersRequired implements Comparator<Job> {
    @Override
    public int compare(Job job1, Job job2) {
        return job2.getWorkersRequired().compareTo(job1.getWorkersRequired());
    }
}
