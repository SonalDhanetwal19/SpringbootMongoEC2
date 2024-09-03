//@Component
//public class MongoDbChangeStreamsConfig implements ApplicationListener<ApplicationReadyEvent> {
//
//    @Value("${mongodb.server.host.atlas}")
//    private String mongoDbHost;
//
//    @Value("${mongodb.dbname}")
//    private String collectionName;
//
//    @Override
//    public void onApplicationEvent(ApplicationReadyEvent event) {
//        CodecRegistry codecRegistryProvider = fromProviders(PojoCodecProvider.builder().automatic(true).build());
//        CodecRegistry codecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),codecRegistryProvider);
//        String uri = mongoDbHost;
//        ConnectionString connectionString = new ConnectionString(uri);
//        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
//                .applyConnectionString(connectionString)
//                .codecRegistry(codecRegistry)
//                .build();
//        try(MongoClient mongoClient = MongoClients.create(mongoClientSettings))
//        {
//            MongoDatabase mongoDatabase = mongoClient.getDatabase(connectionString.getDatabase());
//            MongoCollection<SyncVehicleUserProfileRequestEntity> vehicleUserProfileRevisionHistory = mongoDatabase.getCollection(collectionName, SyncVehicleUserProfileRequestEntity.class);
//            //BsonValue bsonValue = new BsonString("RESUME_TOKEN");
//            //BsonDocument bsonDocument =  new BsonDocument("_data",bsonValue);
//            //List<Bson> aggregationPipeline = Collections.singletonList(match(and(in("operationType", )))
//            ChangeStreamIterable<SyncVehicleUserProfileRequestEntity> changeStreamIterable = vehicleUserProfileRevisionHistory.watch();
//            changeStreamIterable.forEach((Consumer<ChangeStreamDocument<SyncVehicleUserProfileRequestEntity>>)System.out::println);
//        }
//
//    }
//}
