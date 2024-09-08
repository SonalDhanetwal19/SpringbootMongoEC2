package com.phoenix.SpringMongoDBExample.CDC;

import com.mongodb.client.ChangeStreamIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.changestream.ChangeStreamDocument;
import com.mongodb.client.model.changestream.FullDocument;
import org.bson.*;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

import static com.mongodb.client.model.Projections.fields;
import static com.mongodb.client.model.Projections.include;

@Component
public class ChangeStreamProcessor {

    private MongoClient mongoClient;
    private MongoCollection<Document> mongoCollection;
    private String mongoResumeTokenFile;

    @Value("${spring.data.mongodb.uri}")
    public String connectionString;

    @Value("${spring.data.mongodb.database}")
    public String databaseName;

    @Value("${spring.data.mongodb.collection}")
    public String collectionName;

    @Value("${change.events.resumeTokenFile}")
    public String resumeTokenFile;

    public ChangeStreamProcessor() {
        System.out.println("ChangeStreamProcessor no arg Constructor");
//        this.mongoClient = MongoClients.create("mongodb://mongodb:mongodb@GroceryListCluster.mongodb.net/mygrocerylist?authSource=admin");
        this.mongoClient = MongoClients.create("mongodb://mongodb:mongodb@127.0.0.1:27017/mygrocerylist?authSource=admin");
        this.mongoCollection = mongoClient.getDatabase("mygrocerylist").getCollection("GroceryItem");
        this.mongoResumeTokenFile = "resume_token.txt";
    }

    /*public ChangeStreamProcessor(String connectionString, String databaseName, String collectionName, String resumeTokenFile){
        System.out.println("ChangeStreamProcessor parameterized Constructor");
        this.mongoClient = MongoClients.create(connectionString);
        this.mongoCollection = mongoClient.getDatabase(databaseName).getCollection(collectionName);
        this.mongoResumeTokenFile = resumeTokenFile;
    }*/

    public void subscribeToChangeEventMethods()
    {
        BsonValue bsonValue = new BsonString("RESUME_TOKEN");
        BsonDocument resumeToken =  new BsonDocument("_data",bsonValue);
        BsonDocument readResumeTokenFromFile = readResumeTokenFromFile();
        Bson match = Aggregates.match(Filters.in("operationType", Arrays.asList("update", "replace", "insert")));

        // Pick the field you are most interested in
        Bson project = Aggregates.project(fields(include("id","ns","documentKey","fullDocument")));

        List<Bson> pipeline = Arrays.asList(match,project);
        ChangeStreamIterable<Document> changeStreamDocuments = mongoCollection.watch(pipeline);

        if(resumeToken!= null)
        {
//            changeStreamDocuments.resumeAfter(resumeToken);
            changeStreamDocuments.resumeAfter(readResumeTokenFromFile);
        }

        changeStreamDocuments.fullDocument(FullDocument.UPDATE_LOOKUP).forEach((Consumer<? super ChangeStreamDocument<Document>>) change ->
        {
            Document document = change.getFullDocument();
            assert document != null;
            saveDocumentToFile(document);
            saveResumeTokenToFile(change.getResumeToken());
        });
    }


    private void saveDocumentToFile(Document document)
    {
        try(BufferedWriter writer = new BufferedWriter(new FileWriter("documents.txt",true)))
        {
            writer.write(document.toJson() + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveResumeTokenToFile(BsonDocument resumeToken) {
        try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(mongoResumeTokenFile)))
        {
            bufferedWriter.write(resumeToken.toJson());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private BsonDocument readResumeTokenFromFile()
    {
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(mongoResumeTokenFile)))
        {
            String tokenJson = bufferedReader.readLine();
            return BsonDocument.parse(tokenJson);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        catch (Exception e)
        {
            System.out.println("Inside exception with readResumeTokenFromFile");
            e.printStackTrace();
        }
        return null;
    }
}

