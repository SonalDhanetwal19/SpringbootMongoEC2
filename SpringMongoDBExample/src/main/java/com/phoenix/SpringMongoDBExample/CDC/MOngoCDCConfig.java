//package com.phoenix.SpringMongoDBExample.CDC;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class MOngoCDCConfig {
//
//    @Value("${change.events.resumeTokenFile}")
//    private String resumeTokenFile;
//
//    @Value("${spring.data.mongodb.uri}")
//    public String connectionString;
//
//    @Value("${spring.data.mongodb.database}")
//    public String databaseName;
//
//    @Value("${spring.data.mongodb.collection}")
//    public String collectionName;
//
//    public ChangeStreamProcessorNew changeStreamProcessor()
//    {
//        //MongoClient mongoClient = null;
//        return new ChangeStreamProcessorNew(connectionString,databaseName,collectionName, resumeTokenFile);
//    }
//}
