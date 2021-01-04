package edu.tamu.modspineomatic.config.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import edu.tamu.modspineomatic.factory.YamlPropertySourceFactory;
import lombok.Data;

@Data
@Component
@ConfigurationProperties
@PropertySource(value = "classpath:reference/locations.yml", factory = YamlPropertySourceFactory.class)
public class LocationReference {

    private List<Location> locations = new ArrayList<>();

}
