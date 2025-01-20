package com.phoenix.SpringMongoDBExample.Repository;

import com.phoenix.SpringMongoDBExample.model.GroceryHistoryAudit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Component
public class HistoryMongoTemplateRepositoryImpl implements HistoryMongoTemplateRepository {

    @Autowired
    MongoTemplate mongoTemplate;

//    @Autowired
//    public HistoryMongoTemplateRepositoryImpl(MongoTemplate mongoTemplate) {
//        this.mongoTemplate = mongoTemplate;
//    }

    @Override
    public GroceryHistoryAudit saveHistory(GroceryHistoryAudit groceryHistoryAudit)
    {
        return mongoTemplate.save(groceryHistoryAudit);
    }
}
