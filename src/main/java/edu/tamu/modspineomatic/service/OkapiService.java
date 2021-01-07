package edu.tamu.modspineomatic.service;

import java.util.Arrays;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import edu.tamu.modspineomatic.config.model.Credentials;
import edu.tamu.modspineomatic.config.model.Okapi;

@Service
public class OkapiService {
    
    private static final Logger log = LoggerFactory.getLogger(OkapiService.class);

    @Autowired
    public Okapi okapi;

    public RestTemplate restTemplate;

    public OkapiService() {
        restTemplate = new RestTemplate();
    }

    public String getToken(String tenant) {
        String url = okapi.getUrl() + "/authn/login";
        HttpEntity<Credentials> entity = new HttpEntity<>(okapi.getCredentials(), headers(tenant));
        ResponseEntity<Credentials> response = restTemplate.exchange(url, HttpMethod.POST, entity, Credentials.class);
        if (response.getStatusCodeValue() == 201) {
            return response.getHeaders().getFirst("X-Okapi-Token");
        }
        log.error("Failed to login: " + response.getStatusCodeValue());
        throw new RuntimeException("Failed to login: " + response.getStatusCodeValue());
    }

    public JsonNode fetchItem(String tenant, String token, String barcode) {
        HttpEntity<?> entity = new HttpEntity<>(headers(tenant, token));
        String url = okapi.getUrl() + "/item-storage/items?query=barcode==" + barcode;
        ResponseEntity<JsonNode> response = restTemplate.exchange(url, HttpMethod.GET, entity, JsonNode.class);
        if (response.getStatusCodeValue() == 200) {
            return response.getBody().get("items").get(0);
        }
        log.error("Failed to lookup item: " + response.getStatusCodeValue());
        throw new RuntimeException("Failed to lookup item: " + response.getStatusCodeValue());
    }

    public JsonNode fetchHoldings(String tenant, String token, String holdingsId) {
        HttpEntity<?> entity = new HttpEntity<>(headers(tenant, token));
        String url = okapi.getUrl() + "/holdings-storage/holdings/" + holdingsId;
        ResponseEntity<JsonNode> response = restTemplate.exchange(url, HttpMethod.GET, entity, JsonNode.class);
        if (response.getStatusCodeValue() == 200) {
            return response.getBody();
        }
        log.error("Failed to lookup holdings: " + response.getStatusCodeValue());
        throw new RuntimeException("Failed to lookup holdings: " + response.getStatusCodeValue());
    }

    public JsonNode fetchInstance(String tenant, String token, String instanceId) {
        HttpEntity<?> entity = new HttpEntity<>(headers(tenant, token));
        String url = okapi.getUrl() + "/instance-storage/instances/" + instanceId;
        ResponseEntity<JsonNode> response = restTemplate.exchange(url, HttpMethod.GET, entity, JsonNode.class);
        if (response.getStatusCodeValue() == 200) {
            return response.getBody();
        }
        log.error("Failed to lookup instance: " + response.getStatusCodeValue());
        throw new RuntimeException("Failed to lookup instance: " + response.getStatusCodeValue());
    }

    public ArrayNode fetchCallNumberTypes(String tenant, String token) {
        HttpEntity<?> entity = new HttpEntity<>(headers(tenant, token));
        String url = okapi.getUrl() + "/call-number-types?limit=999";
        ResponseEntity<JsonNode> response = restTemplate.exchange(url, HttpMethod.GET, entity, JsonNode.class);
        if (response.getStatusCodeValue() == 200) {
            return (ArrayNode) response.getBody().get("callNumberTypes");
        }
        log.error("Failed to fetch call number types: " + response.getStatusCodeValue());
        throw new RuntimeException("Failed to fetch call number types: " + response.getStatusCodeValue());
    }

    public ArrayNode fetchLibraries(String tenant, String token) {
        HttpEntity<?> entity = new HttpEntity<>(headers(tenant, token));
        String url = okapi.getUrl() + "/location-units/libraries?limit=999";
        ResponseEntity<JsonNode> response = restTemplate.exchange(url, HttpMethod.GET, entity, JsonNode.class);
        if (response.getStatusCodeValue() == 200) {
            return (ArrayNode) response.getBody().get("loclibs");
        }
        log.error("Failed to fetch libraries: " + response.getStatusCodeValue());
        throw new RuntimeException("Failed to fetch libraries: " + response.getStatusCodeValue());
    }

    public ArrayNode fetchLocations(String tenant, String token) {
        HttpEntity<?> entity = new HttpEntity<>(headers(tenant, token));
        String url = okapi.getUrl() + "/locations?limit=999";
        ResponseEntity<JsonNode> response = restTemplate.exchange(url, HttpMethod.GET, entity, JsonNode.class);
        if (response.getStatusCodeValue() == 200) {
            return (ArrayNode) response.getBody().get("locations");
        }
        log.error("Failed to fetch locations: " + response.getStatusCodeValue());
        throw new RuntimeException("Failed to fetch locations: " + response.getStatusCodeValue());
    }

    private HttpHeaders headers(String tenant, String token) {
        HttpHeaders headers = headers(tenant);
        headers.set("X-Okapi-Token", token);
        return headers;
    }

    // NOTE: assuming all accept and content type will be application/json
    private HttpHeaders headers(String tenant) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Okapi-Tenant", tenant);
        return headers;
    }
}
