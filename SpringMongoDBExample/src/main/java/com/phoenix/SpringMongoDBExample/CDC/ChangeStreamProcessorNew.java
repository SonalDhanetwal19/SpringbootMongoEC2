//package com.phoenix.SpringMongoDBExample.CDC;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.mongodb.client.ChangeStreamIterable;
//import com.mongodb.client.MongoClient;
//import com.mongodb.client.MongoClients;
//import com.mongodb.client.MongoCollection;
//import com.mongodb.client.model.Aggregates;
//import com.mongodb.client.model.Filters;
//import com.mongodb.client.model.changestream.FullDocument;
//import com.phoenix.SpringMongoDBExample.Service.GroceryHistoryAuditService;
//import com.phoenix.SpringMongoDBExample.model.GroceryHistoryAudit;
//import com.phoenix.SpringMongoDBExample.model.GroceryItem;
//import org.bson.BsonDocument;
//import org.bson.Document;
//import org.bson.conversions.Bson;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.ApplicationArguments;
//import org.springframework.boot.ApplicationRunner;
//import org.springframework.stereotype.Component;
//
//import java.io.*;
//import java.util.Arrays;
//import java.util.Date;
//import java.util.List;
//
//import static com.mongodb.client.model.Projections.fields;
//import static com.mongodb.client.model.Projections.include;
//
//@Component
//public class ChangeStreamProcessorNew {
//
//    private MongoClient mongoClient;
//    private MongoCollection<Document> mongoCollection;
//    private String mongoResumeTokenFile;
//
//    GroceryHistoryAudit groceryHistoryAudit;
//
//    @Autowired
//    GroceryHistoryAuditService groceryHistoryAuditService;
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
//    @Value("${change.events.resumeTokenFile}")
//    public String resumeTokenFile;
//
//
//    public ChangeStreamProcessorNew(String connectionString, String databaseName, String collectionName, String resumeTokenFile){
//        System.out.println("ChangeStreamProcessor no arg Constructor");
////        this.mongoClient = MongoClients.create("mongodb://mongodb:mongodb@GroceryListCluster.mongodb.net/mygrocerylist?authSource=admin");
//        //this.mongoClient = MongoClients.create("mongodb+srv://Sonal:SonalFirst19@cluster0.xfonvzt.mongodb.net/mygrocerylist?authSource=admin");
//        this.mongoClient = MongoClients.create(connectionString);
//        this.mongoCollection = mongoClient.getDatabase(databaseName).getCollection(collectionName);
//        this.mongoResumeTokenFile = resumeTokenFile;
//    }
//
//    public void subscribeToChangeEventMethods()
//    {
//        try{
//        BsonDocument resumeToken = null;
//        resumeToken = readResumeToken();
//        Bson operationMatch = Aggregates.match(Filters.in("operationType", Arrays.asList("insert", "update", "replace")));
//        Bson projectFields = Aggregates.project(fields(include("_id", "ns", "documentkey", "fulldocument", "_data", "updatedFields")));
////            List<Bson> aggregationPipeline = Arrays.asList(operationMatch, projectFields);
//        List<Bson> aggregationPipeline = Arrays.asList(operationMatch);
//        ChangeStreamIterable<Document> changeStreamDocuments = mongoCollection.watch(aggregationPipeline);
//        if(resumeToken!=null)
//        {
//            changeStreamDocuments.resumeAfter(resumeToken);
//        }
//        changeStreamDocuments.fullDocument(FullDocument.UPDATE_LOOKUP).forEach(groceryItemChangeStreamDocument -> {
//            if(groceryItemChangeStreamDocument.getFullDocument() != null) //NA
//            {
//                System.out.println(groceryItemChangeStreamDocument.getFullDocument()); //NA
//                if(groceryItemChangeStreamDocument.getUpdateDescription().getUpdatedFields()!=null) {
//                    System.out.println(groceryItemChangeStreamDocument.getUpdateDescription().toString());
//                    System.out.println(groceryItemChangeStreamDocument.getUpdateDescription().getUpdatedFields());
//                    saveResumeTokenToFile(groceryItemChangeStreamDocument.getResumeToken());
//                    String Updatedfields = groceryItemChangeStreamDocument.getUpdateDescription().getUpdatedFields().toJson();
//                    System.out.println("json data : "+Updatedfields);
////                    try {
////                        groceryHistoryAuditUpdate(groceryItemChangeStreamDocument.getUpdateDescription().getUpdatedFields());
////                    } catch (JsonProcessingException e) {
////                        e.printStackTrace();
////                    }
//                }
//            }
//            else
//            {
//                System.out.println(groceryItemChangeStreamDocument.getUpdateDescription().toString());
//            }
////                GroceryItem groceryItemDoc = groceryItemChangeStreamDocument.getFullDocument();
//
////                assert groceryItemDoc != null;
//        });
//    } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    private void saveResumeTokenToFile(BsonDocument resumeToken)
//    {
//        try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(resumeTokenFile))) {
//            System.out.println("resume token: "+resumeToken.toJson());
//            bufferedWriter.write(resumeToken.toJson());
//        } catch (IOException e) {
//            e.printStackTrace();
//        } ;
//    }
//
//    private BsonDocument readResumeToken()
//    {
//        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(resumeTokenFile))) {
//            String resumeTokenJson = bufferedReader.readLine();
//            return BsonDocument.parse(resumeTokenJson);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    private void groceryHistoryAuditUpdate(BsonDocument updatedFields) throws JsonProcessingException {
//
//        String updatedFieldsJson = updatedFields.toJson();
//        System.out.println("json data : "+updatedFieldsJson);
//        ObjectMapper historyObjectMapper = new ObjectMapper();
//        GroceryHistoryAudit groceryHistoryAudit =  historyObjectMapper.readValue(updatedFieldsJson,GroceryHistoryAudit.class);
//        Date date = new Date(System.currentTimeMillis());
//        groceryHistoryAudit.setAuditTimeStamp(date);
//        System.out.println("groceryHistoryAudit - print : "+groceryHistoryAudit.toString());
////        groceryHistoryAuditService = new GroceryHistoryAuditService();
//        System.out.println("groceryHistoryAuditService : "+groceryHistoryAuditService);
////        historyRepository.save(groceryHistoryAudit);
//        //historyMongoTemplateRepository.saveHistory(groceryHistoryAudit);
//        groceryHistoryAuditService.saveGroceryHistoryAuditService(groceryHistoryAudit);
//    }
//
//
//}
