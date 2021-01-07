package edu.tamu.modspineomatic.model;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Metadata {

    private String createdDate;
    private String createdByUserId;
    private String updatedDate;
    private String updatedByUserId;

    public static Metadata from(JsonNode node) {
        return Metadata.builder()
            .createdDate(node.get("createdDate").asText())
            .createdByUserId(node.get("createdByUserId").asText())
            .updatedDate(node.get("updatedDate").asText())
            .updatedByUserId(node.get("updatedByUserId").asText())
            .build();
    }

}
