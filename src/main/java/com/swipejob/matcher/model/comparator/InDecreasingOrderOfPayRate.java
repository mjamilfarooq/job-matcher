package com.swipejob.matcher.model.comparator;

import com.swipejob.matcher.model.Job;

import java.util.Comparator;

public class InDecreasingOrderOfPayRate implements Comparator<Job> {

    public int compare(Job job1, Job job2) {
        return job2.getBillRate().compareTo((job1.getBillRate()));
    }
}
