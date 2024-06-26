package com.mongotest.MonGo.model;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document
@Data
public class Photo {
    private String id;
    private String fileName;
    private String fileType;
    private byte[] imageData;

}
