package com.swipejob.matcher.service;

import com.swipejob.matcher.model.Job;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class JobRestService implements IJobDataService {

    @Value("${matcher.job-service}")
    String serviceUrl;

    @Override
    public List<Job> all() {
        var restTemplate = new RestTemplate();
        var response = restTemplate.exchange(
                serviceUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Job>>(){});
        return response.getBody();

    }
}
