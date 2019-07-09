package com.swipejob.matcher.service;

import com.swipejob.matcher.model.Job;
import com.swipejob.matcher.model.Worker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;


@Service
public class TopNMatches implements IMatchingService {

    @Value("${matcher.number-of-matches}")
    int numberOfMatches;


    private final IJobDataService jobDataService;

    @Autowired
    TopNMatches(IJobDataService jobDataService) {
        this.jobDataService = jobDataService;
    }

    Logger logger = LoggerFactory.getLogger(this.getClass());

    private Long rankCertifications(Job job, Worker worker) {
        return worker.getCertificates().
                stream().
                filter(job.getRequiredCertificates()::contains).
                count();
    }

    class JobsInDecreasingOrderOfSalary extends TreeSet<Job> {
        JobsInDecreasingOrderOfSalary() {
            super(Comparator.comparing(Job::getBillRate, Comparator.reverseOrder()));
        }
    }

    class ReverseSortedTreeMap<K, V> extends TreeMap<K, V> {
        ReverseSortedTreeMap() {
            super(Collections.reverseOrder());
        }
    }

    class JobWithinReach implements Predicate<Job> {
        final Worker worker;
        JobWithinReach(Worker worker) {
            this.worker = worker;
        }

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

    @Override
    public List<Job> match(Worker worker) {

        JobWithinReach jobWithinReach = new JobWithinReach(worker);

        return jobDataService.
                all().
                stream().
                filter( jobWithinReach ).   //location check
                filter( job -> !job.isDriverLicenseRequired() || worker.isHasDriversLicense() ).
                filter( job -> worker.getSkills().contains(job.getJobTitle())).
                sorted( Comparator.comparing(Job::getWorkersRequired, Comparator.reverseOrder())).
                sorted( Comparator.comparing(Job::getStartDate)).
                collect( Collectors.groupingBy(
                    job -> rankCertifications(job, worker),
                    ReverseSortedTreeMap::new,
                    Collectors.toCollection(JobsInDecreasingOrderOfSalary::new)
                )).
                values().
                stream().
                limit(numberOfMatches).
                flatMap( Set<Job>::stream ).
                limit(numberOfMatches).
                collect(Collectors.toList());
    }
}