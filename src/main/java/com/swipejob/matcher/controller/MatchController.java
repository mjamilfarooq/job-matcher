package com.swipejob.matcher.controller;

import com.swipejob.matcher.model.Job;
import com.swipejob.matcher.service.IMatchingService;
import com.swipejob.matcher.service.IWorkerDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/match")
public class MatchController {

    private final IWorkerDataService workerService;
    private final IMatchingService matchingService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    MatchController(IWorkerDataService workerService, IMatchingService matchingService) {
        this.workerService = workerService;
        this.matchingService = matchingService;
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<List<Job>> matches(@PathVariable int id) {

        //resource indicated by id doesn't exist.
        ResponseEntity<List<Job>> response = new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

        var worker = workerService.get(id);
        if (worker.isPresent()) {
            var jobs = matchingService.match(worker.get());
            response = new ResponseEntity<>(jobs, HttpStatus.OK);
        }

        return response;
    }

}
