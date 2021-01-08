package edu.tamu.modspineomatic.service;

import java.io.IOException;
import java.util.Map;

import javax.annotation.PostConstruct;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
public class LocationGlossService {

    @Autowired
    private ObjectMapper objectMapper;

    @Value("classpath:/map/locationGlosses.json")
    private Resource locationGlossesResource;

    private Map<String, String> locationGlossesMap;

    @PostConstruct
    public void init() throws JsonParseException, JsonMappingException, IOException {
        locationGlossesMap = objectMapper.readValue(locationGlossesResource.getInputStream(), new TypeReference<Map<String, String>>() {});
    }

    public Map<String, String> getLocationGlossesMap() {
        return locationGlossesMap;
    }

}
