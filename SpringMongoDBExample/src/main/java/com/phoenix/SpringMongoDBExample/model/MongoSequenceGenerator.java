package com.phoenix.SpringMongoDBExample.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("MongoSequence")
@Data
@AllArgsConstructor
public class MongoSequenceGenerator {
    @Id
    private String id;
    private Long sequence;
}
