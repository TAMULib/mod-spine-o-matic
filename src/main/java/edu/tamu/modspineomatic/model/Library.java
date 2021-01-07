package edu.tamu.modspineomatic.model;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Library {
    
    private String id;
    private String name;
    private String code;
    private String campusId;
    private Metadata metadata;

    public static Library from(JsonNode node) {
        return Library.builder()
            .id(node.get("id").asText())
            .name(node.get("name").asText())
            .code(node.get("code").asText())
            .campusId(node.get("campusId").asText())
            .metadata(Metadata.from(node.get("metadata")))
            .build();
    }

}
