package com.swipejob.matcher.service;

import com.swipejob.matcher.model.Job;
import com.swipejob.matcher.model.Worker;
import com.swipejob.matcher.model.comparator.InDecreasingOrderOfNumberOfWorkersRequired;
import com.swipejob.matcher.model.comparator.InDecreasingOrderOfPayRate;
import com.swipejob.matcher.model.function.SkillRanks;
import com.swipejob.matcher.model.predicate.JobWithinReach;
import com.swipejob.matcher.model.predicate.WorkerHasRequiredSkill;
import com.swipejob.matcher.model.predicate.WorkerSatisfyDriversLicenseCondition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
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

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    //Order By
    private final InDecreasingOrderOfNumberOfWorkersRequired
            inDecreasingOrderOfNumberOfWorkersRequired = new InDecreasingOrderOfNumberOfWorkersRequired();
    private final InDecreasingOrderOfPayRate
            inDecreasingOrderOfPayRate = new InDecreasingOrderOfPayRate();

    //Filters
    private JobWithinReach jobWithinReach = new JobWithinReach();
    private WorkerSatisfyDriversLicenseCondition workerSatisfyDriversLicenseCondition = new WorkerSatisfyDriversLicenseCondition();
    private WorkerHasRequiredSkill workerHasRequiredSkill = new WorkerHasRequiredSkill();

    //Calculating ranks based on skills
    private SkillRanks skillRanks = new SkillRanks();


    //Collection to store Salary in decreasing order
    class JobsInDecreasingOrderOfSalary extends TreeSet<Job> {
        JobsInDecreasingOrderOfSalary() {
            super(Comparator.comparing(Job::getBillRate, Comparator.reverseOrder()));
        }
    }

    //collection to store collection of jobs in order of their ranks in decreasing order
    class ReverseSortedTreeMap<K, V> extends TreeMap<K, V> {
        ReverseSortedTreeMap() {
            super(Collections.reverseOrder());
        }
    }

    @Override
    public List<Job> match(Worker worker) {

        jobWithinReach.setWorker(worker);
        workerSatisfyDriversLicenseCondition.setWorker(worker);
        workerHasRequiredSkill.setWorker(worker);
        skillRanks.setWorker(worker);

        return jobDataService.
                all().
                stream().
                filter( jobWithinReach ).   //location check
                filter( workerSatisfyDriversLicenseCondition ).
                filter( workerHasRequiredSkill ).
                sorted( inDecreasingOrderOfNumberOfWorkersRequired ).
                sorted( inDecreasingOrderOfPayRate ).
                collect( Collectors.groupingBy(
                    skillRanks,
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