package com.swipejob.matcher.service;

import com.swipejob.matcher.model.Worker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class WorkerRestService implements IWorkerDataService {

    @Value("${matcher.worker-service}")
    String serviceUrl;

    @Override
    public Optional<Worker> get(int id)
    {
        var restTemplate = new RestTemplate();
        var response = restTemplate.exchange(
                serviceUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Worker>>(){});
        var workers = response.getBody();
        return workers.stream().filter( w -> w.getUserId() == id ).findFirst();
    }
}
