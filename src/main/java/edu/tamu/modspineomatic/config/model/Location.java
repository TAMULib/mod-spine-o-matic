package edu.tamu.modspineomatic.config.model;

import lombok.Data;

@Data
public class Location {

    private String id;
    private String institutionId;
    private String name;
    private String primaryServicePoint;
    private String[] servicePointIds;
    private String isActive;
    private String libraryId;
    private String discoveryDisplayName;
    private String campusId;
    private String code;

}
