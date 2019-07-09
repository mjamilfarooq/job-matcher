package com.swipejob.matcher.service;

import com.swipejob.matcher.model.Worker;

import java.util.Optional;

public interface IWorkerDataService {
    Optional<Worker> get(int id);
}
