package edu.tamu.modspineomatic.model;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Location {

    private String id;
    private String name;
    private String code;
    private String discoveryDisplayName;
    private String isActive;
    private String institutionId;
    private String campusId;
    private String libraryId;
    private String primaryServicePoint;
    private List<String> servicePointIds;

    public static Location from(JsonNode node) {
        return Location.builder()
            .id(node.get("id").asText())
            .name(node.get("name").asText())
            .code(node.get("code").asText())
            .discoveryDisplayName(node.get("discoveryDisplayName").asText())
            .isActive(node.get("isActive").asText())
            .institutionId(node.get("institutionId").asText())
            .campusId(node.get("campusId").asText())
            .libraryId(node.get("libraryId").asText())
            .primaryServicePoint(node.get("primaryServicePoint").asText())
            .servicePointIds(StreamSupport.stream(node.get("servicePointIds").spliterator(), false).map(JsonNode::asText).collect(Collectors.toList()))
            .build();
    }

}
