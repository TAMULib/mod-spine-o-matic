package edu.tamu.modspineomatic.config.model;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "okapi")
public class Okapi {

    private String url;

    private Credentials credentials;

}
