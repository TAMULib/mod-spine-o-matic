package edu.tamu.modspineomatic.config.model;

import lombok.Data;

@Data
public class Library {
    
    private String id;
    private String name;
    private String code;
    private String campusId;
    private Metadata metadata;

}
