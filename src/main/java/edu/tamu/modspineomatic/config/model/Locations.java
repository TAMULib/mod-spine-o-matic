package edu.tamu.modspineomatic.config.model;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@PropertySource(value = "classpath:reference/locations.json")
@ConfigurationProperties
public class Locations {

    private List<Location> locations;

}
