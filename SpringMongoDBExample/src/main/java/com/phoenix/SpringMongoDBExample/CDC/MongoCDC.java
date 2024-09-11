package com.phoenix.SpringMongoDBExample.CDC;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.changestream.ChangeStreamDocument;
import com.phoenix.SpringMongoDBExample.model.GroceryItem;
import org.bson.BsonDocument;
import org.bson.BsonString;
import org.bson.BsonValue;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import static com.mongodb.client.model.Aggregates.match;
import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.in;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class MongoCDC implements ApplicationListener<ApplicationReadyEvent> {

    @Value("${spring.data.mongodb.uri}")
    private String mongoDbHost;

//    @Value("${spring.data.mongodb.database}")
//    private String collectionName;

    @Async
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        System.out.println("onApplicationEvent called");
        CodecRegistry codecRegistryProvider = fromProviders(PojoCodecProvider.builder().automatic(true).build());
        CodecRegistry codecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),codecRegistryProvider);
//        String uri = mongoDbHost;
//        String uri = "mongodb://root:rootpassword@mongo_db:27017/mygrocerylist?authSource=admin";
        String uri = "mongodb://root:rootpassword@127.0.0.1:27017/mygrocerylist?directConnection=true";
        System.out.println("mongo-db-uri : "+uri);
        ConnectionString connectionString = new ConnectionString(uri);
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .codecRegistry(codecRegistry)
                .build();
        try(MongoClient mongoClient = MongoClients.create(mongoClientSettings))
        {
            System.out.println("inside try block");
            MongoDatabase mongoDatabase = mongoClient.getDatabase(connectionString.getDatabase());
            MongoCollection<GroceryItem> groceryCollection = mongoDatabase.getCollection("GroceryItem", GroceryItem.class);
            BsonValue bsonValue = new BsonString("RESUME_TOKEN");
            BsonDocument bsonDocument =  new BsonDocument("_data",bsonValue);
            List<Bson> aggregationPipeline = Collections.singletonList(match(and(in("operationType", Arrays.asList("insert","update","delete")))));
            groceryCollection.watch(aggregationPipeline).resumeAfter(bsonDocument).forEach(printEvent());
            //ChangeStreamIterable<GroceryItem> changeStreamIterable = vehicleUserProfileRevisionHistory.watch();
//            changeStreamIterable.forEach(System.out::println);
        }
    }

    private static Consumer<ChangeStreamDocument<GroceryItem>> printEvent() {
        return System.out::println;
    }

}

