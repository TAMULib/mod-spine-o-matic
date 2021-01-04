package edu.tamu.modspineomatic.service;

import java.util.Locale;
import java.util.Optional;

import com.fasterxml.jackson.databind.JsonNode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import edu.tamu.modspineomatic.config.model.CallNumberType;
import edu.tamu.modspineomatic.config.model.CallNumberTypeReference;
import edu.tamu.modspineomatic.config.model.Library;
import edu.tamu.modspineomatic.config.model.LibraryReference;
import edu.tamu.modspineomatic.config.model.Location;
import edu.tamu.modspineomatic.config.model.LocationReference;

@Service
public class TemplatingService {

    private static final String SPINE_LABEL = "spine-label";
    private static final String CHRONOLOGY = "chronology";
    private static final String CALL_NUMBER = "call_number";
    private static final String CALL_NUMBER_PREFIX = "call_number_prefix";
    private static final String PARSED_CALL_NUMBER = "parsed_call_number"; 
    private static final String CALL_NUMBER_TYPE = "call_number_type";
    private static final String CALL_NUMBER_TYPE_DESC = "call_number_type_desc";
    private static final String LOCATION_NAME = "location_name";
    private static final String LOCATION_CODE = "location_code";
    private static final String LIBRARY_DESCRIPTION = "library_description";
    private static final String LIBRARY_CODE = "library_code";
    private static final String CHRONOLOGY_PROPERTY = "chronology";
    private static final String EFFECTIVE_CALL_NUMBER_PROPERTY = "effectiveCallNumberComponents";
    private static final String CALL_NUMBER_PROPERTY = "callNumber";
    private static final String CALL_NUMBER_PREFIX_PROPERTY = "prefix";
    private static final String CALL_NUMBER_TYPE_PROPERTY = "typeId";
    private static final String EFFECTIVE_LOCATION_PROPERTY = "effectiveLocationId";

    @Autowired
    private SpringTemplateEngine templateEngine;

    @Autowired
    private CallNumberTypeReference callNumberTypeReference;

    @Autowired
    private LibraryReference libraryReference;

    @Autowired
    private LocationReference locationReference;

    public String templateResponse(JsonNode itemNode) {
        return templateEngine.process(SPINE_LABEL, populateContext(itemNode));
    }

    private Context populateContext(JsonNode itemNode) {
        Context context = new Context(Locale.getDefault());

        String chron = itemNode.get(CHRONOLOGY_PROPERTY) != null ? itemNode.get(CHRONOLOGY_PROPERTY).asText() : "";
        context.setVariable(CHRONOLOGY, chron);
        context.setVariable(CALL_NUMBER, itemNode.get(EFFECTIVE_CALL_NUMBER_PROPERTY).get(CALL_NUMBER_PROPERTY).asText());

        JsonNode callNumberPrefixNode = itemNode.get(EFFECTIVE_CALL_NUMBER_PROPERTY).get(CALL_NUMBER_PREFIX_PROPERTY);
        if (callNumberPrefixNode != null) {
            context.setVariable(CALL_NUMBER_PREFIX, callNumberPrefixNode.asText());
        }

        // TODO: parse call number if needed
        context.setVariable(PARSED_CALL_NUMBER, itemNode.get(EFFECTIVE_CALL_NUMBER_PROPERTY).get(CALL_NUMBER_PROPERTY).asText());

        String callNumberUUID = itemNode.get(EFFECTIVE_CALL_NUMBER_PROPERTY).get(CALL_NUMBER_TYPE_PROPERTY).asText();

        Optional<CallNumberType> callNumberTypeOption = callNumberTypeReference.getCallNumberTypes().stream()
            .filter(c -> c.getId().equals(callNumberUUID))
            .findAny();

        if (callNumberTypeOption.isPresent()) {
            CallNumberType cnType = callNumberTypeOption.get();
            context.setVariable(CALL_NUMBER_TYPE, cnType.getType());
            context.setVariable(CALL_NUMBER_TYPE_DESC, cnType.getDesc());
        }

        String locationUUID = itemNode.get(EFFECTIVE_LOCATION_PROPERTY).asText();

        Optional<Location> locationOption = locationReference.getLocations().stream()
            .filter(l -> l.getId().equals(locationUUID))
            .findAny();

        if (locationOption.isPresent()) {
            Location location = locationOption.get();
            context.setVariable(LOCATION_NAME, location.getName());
            context.setVariable(LOCATION_CODE, location.getCode());
            String libraryId = location.getLibraryId();

            Optional<Library> libraryOption = libraryReference.getLibraries().stream()
                .filter(l -> l.getId().equals(libraryId))
                .findAny();

            if (libraryOption.isPresent()) {
                Library library = libraryOption.get();
                context.setVariable(LIBRARY_DESCRIPTION, library.getName());
                context.setVariable(LIBRARY_CODE, library.getCode());
            }
        }

        // TODO: Do we need to do anything with this?
        context.setVariable("title", "title");

        return context;
    }
}
