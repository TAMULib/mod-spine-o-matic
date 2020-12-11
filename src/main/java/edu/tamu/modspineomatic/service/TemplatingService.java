package edu.tamu.modspineomatic.service;

import java.util.Locale;
import java.util.Optional;

import javax.annotation.PostConstruct;

import com.fasterxml.jackson.databind.JsonNode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import edu.tamu.modspineomatic.config.model.Location;
import edu.tamu.modspineomatic.config.model.LocationReference;

@Service
public class TemplatingService {

    private static final String SPINE_LABEL = "spine-label";
    private static final String CHRONOLOGY = "chronology";
    private static final String CALL_NUMBER = "call_number";
    private static final String PARSED_CALL_NUMBER = "parsed_call_number"; 
    private static final String CALL_NUMBER_TYPE = "call_number_type";
    private static final String CALL_NUMBER_TYPE_DESC = "call_number_type_desc";
    private static final String LOCATION_NAME = "location_name";
    private static final String LOCATION_CODE = "location_code";
    private static final String CHRONOLOGY_PROPERTY = "chronology";
    private static final String EFFECTIVE_CALL_NUMBER_PROPERTY = "effectiveCallNumberComponents";
    private static final String CALL_NUMBER_PROPERTY = "callNumber";
    private static final String CALL_NUMBER_TYPE_PROPERTY = "typeId";
    private static final String EFFECTIVE_LOCATION_PROPERTY = "effectiveLocationId";
    private static final String PARSED_CALL_NUMBER_PROPERTY = "parsedCallNumber";

    @Autowired
    private SpringTemplateEngine templateEngine;

    @Autowired
    private LocationReference reference;

    public String templateResponse(JsonNode itemNode) {
        return templateEngine.process(SPINE_LABEL, populateContext(itemNode));
    }

    private Context populateContext(JsonNode itemNode) {
        Context context = new Context(Locale.getDefault());

        String chron = itemNode.get(CHRONOLOGY_PROPERTY) != null ? itemNode.get(CHRONOLOGY_PROPERTY).asText() : "";
        context.setVariable(CHRONOLOGY, chron);
        context.setVariable(CALL_NUMBER, itemNode.get(EFFECTIVE_CALL_NUMBER_PROPERTY).get(CALL_NUMBER_PROPERTY).asText());
        // TODO: parse call number
        context.setVariable(PARSED_CALL_NUMBER, itemNode.get(EFFECTIVE_CALL_NUMBER_PROPERTY).get(CALL_NUMBER_PROPERTY).asText());

        String callNumberUUID = itemNode.get(EFFECTIVE_CALL_NUMBER_PROPERTY).get(CALL_NUMBER_TYPE_PROPERTY).asText();
        // Fall back to other type
        String callNumberType = "4";
        String callNumberTypeDesc = "Other";


        switch (callNumberUUID) {
            case "03dd64d0-5626-4ecd-8ece-4531e0069f35":
                callNumberType = "1";
                callNumberTypeDesc = "Dewey";
                break;
            case "054d460d-d6b9-4469-9e37-7a78a2266655":
                callNumberType = "0";
                callNumberTypeDesc = "Library of Congress";
                break;
            case "95467209-6d7b-468b-94df-0f5d7ad2747d":
                callNumberType = "0";
                callNumberTypeDesc = "National Library of Medicine";
                break;
            case "fc388041-6cd0-4806-8a74-ebe3b9ab4c6e":
                callNumberType = "3";
                callNumberTypeDesc = "Superintendent of Documents";
                break;
            default:
                break;
        }

        context.setVariable(CALL_NUMBER_TYPE, callNumberType);
        context.setVariable(CALL_NUMBER_TYPE_DESC, callNumberTypeDesc);

        String locationUUID = itemNode.get(EFFECTIVE_LOCATION_PROPERTY).asText();

        Optional<Location> locationOption = reference.getLocations().stream()
            .filter(l -> l.getId().equals(locationUUID))
            .findAny();

        if (locationOption.isPresent()) {
            Location location = locationOption.get();
            context.setVariable(LOCATION_NAME, location.getName());
            context.setVariable(LOCATION_CODE, location.getCode());
        }

        context.setVariable("library_description", "Evans Library");
        context.setVariable("library_code", "Evans");
        context.setVariable("title", "title");

        return context;
    }
}
