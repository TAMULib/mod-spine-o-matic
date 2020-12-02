package edu.tamu.modspineomatic.service;

import java.util.Locale;

import javax.annotation.Resource;

import com.fasterxml.jackson.databind.JsonNode;

import org.springframework.beans.factory.annotation.Autowired;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templatemode.TemplateMode;

public class TemplatingService {

    private static final String XML = ".xml";
    private static final String SPINE_LABEL = "spine-label";
    private static final String YEAR_OF_ISSUE = "year_of_issue";
    private static final String CALL_NUMBER = "call_number";
    private static final String PARSED_CALL_NUMBER = "parsed_call_number";
    private static final String YEAR_OF_ISSUE_PROPERTY = "";
    private static final String CALL_NUMBER_PROPERTY = "callNumber";
    private static final String PARSED_CALL_NUMBER_PROPERTY = "parsedCallNumber";

    @Resource(name = "templateResolver")
    private SpringResourceTemplateResolver resolver;

    @Autowired
    private SpringTemplateEngine templateEngine;

    public String templateResponse(JsonNode json) {
        resolver.setSuffix(XML);
        resolver.setTemplateMode(TemplateMode.XML);
        resolver.setCacheable(false);

        return templateEngine.process(SPINE_LABEL, populateContext(json));
    }

    private Context populateContext(JsonNode json) {
        Context context = new Context(Locale.getDefault());

        context.setVariable(YEAR_OF_ISSUE, json.get(YEAR_OF_ISSUE_PROPERTY).asText());
        context.setVariable(CALL_NUMBER, json.get(CALL_NUMBER_PROPERTY).asText());
        context.setVariable(PARSED_CALL_NUMBER, json.get(PARSED_CALL_NUMBER_PROPERTY).asText());

        return context;
    }
}
