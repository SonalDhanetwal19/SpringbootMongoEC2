package com.phoenix.SpringMongoDBExample.Repository;

import com.mongodb.client.result.UpdateResult;
import com.mongodb.internal.bulk.UpdateRequest;
import com.phoenix.SpringMongoDBExample.model.GroceryItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;


@Component
public class CustomRepositoryImpl implements CustomRepository {

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public String updateQuantity(String itemName, int quantity) {

        Query query = new Query(Criteria.where("itemName").is(itemName));
        Update update = new Update();
        update.set("quantity",quantity);
        UpdateResult result = mongoTemplate.updateFirst(query, update, GroceryItem.class);
        if(result == null)
            return "No document updated";
        else
            return "document updated";
    }
}
