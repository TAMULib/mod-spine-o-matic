package edu.tamu.modspineomatic.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import edu.tamu.modspineomatic.model.Location;

@Service
public class LocationService {

    private final List<Location> locations = new ArrayList<>();

    @Autowired
    private OkapiService okapiService;

    @Value("${okapi.tenant}")
    private String tenant;

    @PostConstruct
    public void init() {
        fetchLocations();
    }

    @Scheduled(cron = "0 0 0 * * *")
    private void fetchLocations() {
        String token = okapiService.getToken(tenant);
        setLocations(StreamSupport.stream(okapiService.fetchLocations(tenant, token).spliterator(), false).map(Location::from).collect(Collectors.toList()));
    }

    public synchronized List<Location> getLocations() {
        return locations;
    }

    public synchronized void setLocations(List<Location> locations) {
        this.locations.clear();
        this.locations.addAll(locations);
    }

}
