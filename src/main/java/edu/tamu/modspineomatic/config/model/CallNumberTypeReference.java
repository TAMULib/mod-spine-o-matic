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
@PropertySource(value = "classpath:reference/callNumberTypes.yml", factory = YamlPropertySourceFactory.class)
public class CallNumberTypeReference {

    private List<CallNumberType> callNumberTypes = new ArrayList<>();

}