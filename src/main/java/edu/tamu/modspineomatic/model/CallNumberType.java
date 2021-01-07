package edu.tamu.modspineomatic.model;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CallNumberType {

    private String id;
    private String name;
    private String type;

    public static CallNumberType from(JsonNode node) {
        return CallNumberType.builder()
            .id(node.get("id").asText())
            .name(node.get("name").asText())
            .build();
    }

}
