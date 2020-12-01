package edu.tamu.modspineomatic.service;

import java.util.Arrays;

import com.fasterxml.jackson.databind.JsonNode;

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
        String url = okapi.getUrl() + "/items?query=barcode==" + barcode;
        ResponseEntity<JsonNode> response = restTemplate.exchange(url, HttpMethod.GET, entity, JsonNode.class);
        if (response.getStatusCodeValue() == 200) {
            return response.getBody();
        }
        log.error("Failed to lookup item: " + response.getStatusCodeValue());
        throw new RuntimeException("Failed to lookup item: " + response.getStatusCodeValue());
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
