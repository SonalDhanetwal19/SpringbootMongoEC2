package com.phoenix.SpringMongoDBExample.configuration;

import com.mongodb.client.MongoClient;
import com.mongodb.client.model.changestream.ChangeStreamDocument;
import com.phoenix.SpringMongoDBExample.CDC.ChangeStreamProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MongoConfig {

    @Value("${spring.data.mongodb.uri}")
    private String connectionString;

    @Value("${spring.data.mongodb.database}")
    private String databaseName;

    @Value("${spring.data.mongodb.collection}")
    private String collectionName;

    @Value("${change.events.resumeTokenFile}")
    private String resumeTokenFile;

    public ChangeStreamProcessor changeStreamProcessor()
    {
        MongoClient mongoClient = null;
        return new ChangeStreamProcessor(connectionString, databaseName, collectionName, resumeTokenFile);
    }
}
