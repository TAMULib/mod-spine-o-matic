package edu.tamu.modspineomatic.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.annotation.PostConstruct;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import edu.tamu.modspineomatic.model.CallNumberType;
import lombok.Data;

@Service
public class CallNumberTypeService {

    private final List<CallNumberType> callNumberTypes = new ArrayList<>();

    @Autowired
    private OkapiService okapiService;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${okapi.tenant}")
    private String tenant;

    @Value("classpath:map/callNumberTypes.json")
    private Resource callNumberTypesMapResource;

    private Map<String, String> callNumberTypesMap;

    @PostConstruct
    public void init() throws JsonParseException, JsonMappingException, IOException {
        callNumberTypesMap = objectMapper.readValue(callNumberTypesMapResource.getInputStream(), new TypeReference<Map<String, String>>() {});
        fetchCallNumberTypes();
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void fetchCallNumberTypes() {
        String token = okapiService.getToken(tenant);
        System.out.println("fetching call numbers");
        setCallNumberTypes(StreamSupport.stream(okapiService.fetchCallNumberTypes(tenant, token).spliterator(), false)
            .map(CallNumberType::from)
            .map(this::addType)
            .collect(Collectors.toList()));
    }

    public synchronized List<CallNumberType> getCallNumberTypes() {
        return callNumberTypes;
    }

    private synchronized void setCallNumberTypes(List<CallNumberType> callNumberTypes) {
        this.callNumberTypes.clear();
        this.callNumberTypes.addAll(callNumberTypes);
    }

    @Data
    public class CallNumberTypeMapping {
        private String name;
        private Integer type;
    }

    private CallNumberType addType(CallNumberType callNumberType) {
        String name = callNumberType.getName();
        if (callNumberTypesMap.containsKey(name)) {
            callNumberType.setType(callNumberTypesMap.get(name));
        } else {
            callNumberType.setType("0");
        }
        return callNumberType;
    }

}
