package com.phoenix.SpringMongoDBExample.CDC;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.*;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.changestream.ChangeStreamDocument;
import com.mongodb.client.model.changestream.FullDocument;
import com.mongodb.client.model.changestream.FullDocumentBeforeChange;
import com.phoenix.SpringMongoDBExample.Service.GroceryHistoryAuditService;
import com.phoenix.SpringMongoDBExample.model.GroceryHistoryAudit;
import com.phoenix.SpringMongoDBExample.model.GroceryItem;
import org.bson.BsonDocument;
import org.bson.BsonString;
import org.bson.BsonValue;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;

import java.io.*;
import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import static com.mongodb.client.model.Projections.fields;
import static com.mongodb.client.model.Projections.include;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

@Configuration
public class MongoCDC implements ApplicationListener<ApplicationReadyEvent> {

    GroceryHistoryAudit groceryHistoryAudit;

    @Autowired
    GroceryHistoryAuditService groceryHistoryAuditService;

    @Value("${spring.data.mongodb.uri}")
    private String mongoDbHostUri;

//    @Value("${spring.data.mongodb.database}")
//    private String collectionName;

    @Value("${change.events.resumeTokenFile}")
    //private Resource resumeTokenFile;
    private String resumeTokenFile;

    //@Async
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        System.out.println("onApplicationEvent called");
        CodecRegistry codecRegistryProvider = fromProviders(PojoCodecProvider.builder().automatic(true).build());
        CodecRegistry codecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),codecRegistryProvider);
//        String uri = mongoDbHost;
//        String uri = "mongodb://root:rootpassword@mongo_db:27017/mygrocerylist?authSource=admin";
//        String uri = "mongodb://root:rootpassword@127.0.0.1:27017/mygrocerylist?directConnection=true";
        String uri = "mongodb+srv://Sonal:SonalFirst19@cluster0.xfonvzt.mongodb.net/mygrocerylist?authSource=admin";
        System.out.println("mongo-db-uri : "+mongoDbHostUri);
        ConnectionString connectionString = new ConnectionString(mongoDbHostUri);
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .codecRegistry(codecRegistry)
                .build();
        try(MongoClient mongoClient = MongoClients.create(mongoClientSettings)) {
            System.out.println("inside try block");
            MongoDatabase mongoDatabase = mongoClient.getDatabase(connectionString.getDatabase());
//            MongoCollection<GroceryItem> groceryCollection = mongoDatabase.getCollection("GroceryItem", GroceryItem.class);
            MongoCollection<Document> groceryCollection = mongoDatabase.getCollection("GroceryItem", Document.class);
//            BsonValue bsonValue = new BsonString("<resume_token>");
            BsonValue bsonValue = new BsonString("RESUME_TOKEN");
            BsonDocument bsonDocument = new BsonDocument("_data", bsonValue);

//            groceryCollection.watch(aggregationPipeline).forEach(printEvent()); --working
            //ChangeStreamIterable<GroceryItem> changeStreamIterable = vehicleUserProfileRevisionHistory.watch();
//            changeStreamIterable.forEach(System.out::println);
            //List<Bson> aggregationPipeline = Collections.singletonList(match(and(in("operationType", Arrays.asList("insert","update","delete")))));--working
            BsonDocument resumeToken = null;
            //resumeToken = readResumeToken();
            Bson operationMatch = Aggregates.match(Filters.in("operationType", Arrays.asList("insert", "update", "replace")));
            Bson projectFields = Aggregates.project(fields(include("_id", "ns", "documentkey", "fulldocument", "_data", "updatedFields")));
//            List<Bson> aggregationPipeline = Arrays.asList(operationMatch, projectFields);
            List<Bson> aggregationPipeline = Arrays.asList(operationMatch);
            if(resumeToken==null) {
//                MongoCursor<ChangeStreamDocument<GroceryItem>> cursor=groceryCollection.watch(aggregationPipeline).iterator();
                MongoCursor<ChangeStreamDocument<Document>> cursor=groceryCollection.watch(aggregationPipeline).iterator();
//                ChangeStreamDocument<GroceryItem> next = cursor.next();
                ChangeStreamDocument<Document> next = cursor.next();
                BsonDocument bsonDocumentResumeToken = new BsonDocument("_data", next.getResumeToken().get("_data"));
                System.out.println("resume token gerernated : "+bsonDocumentResumeToken.get("_data"));
                saveResumeTokenToFile(bsonDocumentResumeToken);
            }
            resumeToken = readResumeToken();
//            ChangeStreamOptions changeStreamOptions = ChangeStreamOptions.builder().fullDocumentBeforeChangeLookup(FullDocumentBeforeChange.WHEN_AVAILABLE).build();
//            ChangeStreamIterable<GroceryItem> changeStreamDocuments = groceryCollection.watch(aggregationPipeline);
            ChangeStreamIterable<Document> changeStreamDocuments = groceryCollection.watch(aggregationPipeline);
            System.out.println("changeStreamDocuments : "+changeStreamDocuments.toString());
            if(resumeToken!=null)
            {
                changeStreamDocuments.resumeAfter(resumeToken);
            }
            //fullDocumentBeforeChange(FullDocumentBeforeChange.WHEN_AVAILABLE).
//            MongoCursor<ChangeStreamDocument<GroceryItem>> newCursor = changeStreamDocuments.fullDocumentBeforeChange(FullDocumentBeforeChange.WHEN_AVAILABLE).fullDocument(FullDocument.UPDATE_LOOKUP).iterator();
            MongoCursor<ChangeStreamDocument<Document>> newCursor = changeStreamDocuments.fullDocumentBeforeChange(FullDocumentBeforeChange.WHEN_AVAILABLE).fullDocument(FullDocument.UPDATE_LOOKUP).iterator();
            System.out.println("CDC - mongocursor created - newCursor.hasNext() : "+newCursor.hasNext());
            while(newCursor.hasNext())
            {
                System.out.println("CDC - Entered cursor.hasNext()");
                var next = newCursor.next();
                if("replace".equals(next.getOperationType().getValue()) || "update".equals(next.getOperationType().getValue()))
                {
//                    GroceryItem newGroceryItem = next.getFullDocument();
                    Document newGroceryItem = next.getFullDocument();
                    int itemIdValue = (int) newGroceryItem.get("itemId");
                    System.out.println("item id value : "+itemIdValue);
                    System.out.println("newGroceryItem : "+newGroceryItem);
//                    GroceryItem oldGroceryItem = next.getFullDocumentBeforeChange();
                    Document oldGroceryItem = next.getFullDocumentBeforeChange();

                    System.out.println("oldGroceryItem : "+oldGroceryItem);
                    if(newGroceryItem!=null && oldGroceryItem!=null)
                    {
                        System.out.println("Before compare documents");
                        Document diffDocument = compareDocuments(newGroceryItem,oldGroceryItem, itemIdValue);
                        groceryHistoryAuditUpdate(diffDocument.toBsonDocument());
                    }
                }
                System.out.println("CDC - newCursor.next() : "+next);
                System.out.println("CDC - getFullDocument() : "+next.getFullDocument());
                //System.out.println("CDC - getFullDocumentBeforeChange : "+next.getFullDocumentBeforeChange());
                System.out.println("CDC - get update description : "+next.getUpdateDescription());
                System.out.println("CDC - newCursor.tryNext() : "+newCursor.tryNext());
            }


            System.out.println("before foreach");
            changeStreamDocuments.fullDocument(FullDocument.UPDATE_LOOKUP).forEach(groceryItemChangeStreamDocument -> {
                System.out.println("groceryItemChangeStreamDocument : "+groceryItemChangeStreamDocument.toString());
                if(groceryItemChangeStreamDocument.getFullDocument() != null) //NA
                {
                    System.out.println(groceryItemChangeStreamDocument.getFullDocument()); //NA
                    if(!Objects.isNull(groceryItemChangeStreamDocument.getUpdateDescription())&& groceryItemChangeStreamDocument.getUpdateDescription().getUpdatedFields()!=null) {
                        System.out.println(groceryItemChangeStreamDocument.getUpdateDescription().toString());
                        System.out.println(groceryItemChangeStreamDocument.getUpdateDescription().getUpdatedFields());
                        try {
                            saveResumeTokenToFile(groceryItemChangeStreamDocument.getResumeToken());
                        } catch (IOException | URISyntaxException e) {
                            e.printStackTrace();
                        }
                        String Updatedfields = groceryItemChangeStreamDocument.getUpdateDescription().getUpdatedFields().toJson();
                        System.out.println("json data : "+Updatedfields);
                        try {
                            groceryHistoryAuditUpdate(groceryItemChangeStreamDocument.getUpdateDescription().getUpdatedFields());
                        } catch (JsonProcessingException e) {
                            e.printStackTrace();
                        }
                    }
                }
                else
                {
                    System.out.println(groceryItemChangeStreamDocument.getUpdateDescription().toString());
                }
//                GroceryItem groceryItemDoc = groceryItemChangeStreamDocument.getFullDocument();

//                assert groceryItemDoc != null;
            });
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public Document compareDocuments(Document newGroceryItem, Document oldGroceryItem, int itemId)
    {
        Map<String,Object> newGroceryItemMap = flattenGroceryItemMap(newGroceryItem);
        Map<String,Object> oldGroceryItemMap = flattenGroceryItemMap(oldGroceryItem);
        AtomicReference<Document> diffDocument = new AtomicReference<Document>();
        Document tempDoc = new Document("itemId",itemId);
        diffDocument.set(tempDoc);
//        AtomicReference<Document> tempDoc = new AtomicReference<Document>();
        newGroceryItemMap.keySet().parallelStream().forEach(key -> {
            Object newGroceryMapValue = newGroceryItemMap.get(key);
            Object oldGroceryMapValue = oldGroceryItemMap.get(key);
            System.out.println("after flattenGroceryItemMap");
            if(newGroceryMapValue == null && oldGroceryMapValue == null)
            {
                return ;
            }
            else if(newGroceryMapValue != null && oldGroceryMapValue != null && !newGroceryMapValue.equals(oldGroceryMapValue))
            {
                System.out.println("Difference found at '" + key + "':");
                System.out.println("newGroceryMapValue : " + newGroceryMapValue);
                System.out.println("oldGroceryMapValue : " + oldGroceryMapValue);
                diffDocument.set(tempDoc.append(key, newGroceryMapValue));
//                tempDoc.append(key, newGroceryMapValue);
                //diffDocument.set(new Document(key, newGroceryMapValue));
            }
        });
        return diffDocument.get();
//        return diffDocument;
    }

    private Map<String, Object> flattenGroceryItemMap(Document newGroceryItem) {
        System.out.println("flattenGroceryItemMap ");
        Map<String,Object> flatMap = new HashMap<>();
        createFlatGroceryItemMap("", newGroceryItem, flatMap);
        return flatMap;
    }

    private void createFlatGroceryItemMap(String prefix, Document newGroceryItem, Map<String, Object> flatMap) {
        System.out.println("createFlatGroceryItemMap ");
        for(Map.Entry<String,Object> entry : newGroceryItem.entrySet())
        {
            System.out.println("entry.getKey() : "+entry.getKey());
            String key = prefix.isEmpty() ? entry.getKey() : prefix + "." + entry.getKey();
            System.out.println("key : "+key);
            System.out.println("entry.getValue() : "+entry.getValue());
            Object value = entry.getValue();
            System.out.println("value : "+value);

            if(value instanceof Document)
                createFlatGroceryItemMap(key,(Document) value,flatMap);
            else
                flatMap.put(key,value);
        }
    }


    private static Consumer<ChangeStreamDocument<GroceryItem>> printEvent() {
        return System.out::println;
    }

    private void saveResumeTokenToFile(BsonDocument resumeToken) throws IOException, URISyntaxException {
//        final File file = new File(resumeTokenFile.getURL().getFile());
//        System.out.println("file exists : "+file.exists());
//        if(!file.exists())
//        {
//            file.createNewFile();
//        }

//        String filename = "/resources/resume_token.txt";
//        Path source = Paths.get(this.getClass().getResource("/").getPath());
//        System.out.println("path : "+source.toString());
//        Path newFolder = Paths.get(source.toAbsolutePath()+"/resumeToken");
//        Files.createDirectories(newFolder);
//        Files.createFile(newFolder.toAbsolutePath(),"resumeTokenFile.txt")
//        final File file = new File("./resume_token.txt");

//        System.out.println("absolute path : "+file.getAbsolutePath());
//        System.out.println("path : "+file.getPath());
//        System.out.println("canonical path : "+file.getCanonicalPath());
//        try(BufferedWriter bufferedWriter = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
//        try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file.getPath()))) {
       // URL resource = this.getClass().getClassLoader().getResourceAsStream("/resume_token.txt");
//                System.out.println("path : "+path.toString());
        //System.out.println("resource - path : "+resource.toURI());
        try(OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream("resume_token3.txt"))){
            System.out.println("resume token: "+resumeToken.toJson());
//            bufferedWriter.write(resumeToken.toJson());
            outputStreamWriter.write(resumeToken.toJson());
//            outputStreamWriter.flush();
//            outputStreamWriter.close();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        } ;
    }

    private BsonDocument readResumeToken()
    {
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(resumeTokenFile))) {
//        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(resumeTokenFile.getInputStream()))) {
        String resumeTokenJson = bufferedReader.readLine();
            return BsonDocument.parse(resumeTokenJson);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

//    private void groceryHistoryAuditUpdate1(Document diffDocument)
//    {
//        diffDocument.toBsonDocument();
//        groceryHistoryAuditService.saveGroceryHistoryAuditService(groceryHistoryAudit);
//    }

    private void groceryHistoryAuditUpdate(BsonDocument updatedFields) throws JsonProcessingException {

        String updatedFieldsJson = updatedFields.toJson();
        System.out.println("json data : "+updatedFieldsJson);
        ObjectMapper historyObjectMapper = new ObjectMapper();
        GroceryHistoryAudit groceryHistoryAudit =  historyObjectMapper.readValue(updatedFieldsJson,GroceryHistoryAudit.class);
        Date date = new Date(System.currentTimeMillis());
        groceryHistoryAudit.setAuditTimeStamp(date);
        System.out.println("groceryHistoryAudit - print : "+groceryHistoryAudit.toString());
//        groceryHistoryAuditService = new GroceryHistoryAuditService();
        System.out.println("groceryHistoryAuditService : "+groceryHistoryAuditService);
//        historyRepository.save(groceryHistoryAudit);
        //historyMongoTemplateRepository.saveHistory(groceryHistoryAudit);
        groceryHistoryAuditService.saveGroceryHistoryAuditService(groceryHistoryAudit);
    }

}

