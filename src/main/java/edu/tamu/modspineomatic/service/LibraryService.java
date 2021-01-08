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

import edu.tamu.modspineomatic.model.Library;

@Service
public class LibraryService {

    private final List<Library> libraries = new ArrayList<>();

    @Autowired
    private OkapiService okapiService;

    @Value("${okapi.tenant}")
    private String tenant;

    @PostConstruct
    public void init() {
        fetchLibraries();
    }

    @Scheduled(cron = "0 0 0 * * *")
    private void fetchLibraries() {
        String token = okapiService.getToken(tenant);
        setLibraries(StreamSupport.stream(okapiService.fetchLibraries(tenant, token).spliterator(), false).map(Library::from).collect(Collectors.toList()));
    }

    public synchronized List<Library> getLibraries() {
        return libraries;
    }

    public synchronized void setLibraries(List<Library> libraries) {
        this.libraries.clear();
        this.libraries.addAll(libraries);
    }

}
