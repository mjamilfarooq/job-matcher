package com.swipejob.matcher.service;

import com.swipejob.matcher.model.Job;
import com.swipejob.matcher.model.Worker;

import java.util.List;

public interface IMatchingService {
    List<Job> match(Worker worker);
}
