package com.phoenix.SpringMongoDBExample.Repository;

import com.mongodb.client.result.UpdateResult;
import com.phoenix.SpringMongoDBExample.model.GroceryItem;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndReplaceOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import static org.springframework.data.mongodb.core.query.Criteria.where;


@Component
public class CustomRepositoryImpl implements CustomRepository {

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public String updateQuantity(String itemName, int quantity) {

        Query query = new Query(where("itemName").is(itemName));
        Update update = new Update();
        update.set("quantity",quantity);
        UpdateResult result = mongoTemplate.updateFirst(query, update, GroceryItem.class);
        if(result == null)
            return "No document updated";
        else
            return "document updated";
    }

    @Override
    public String replaceGrocery(String itemName, GroceryItem groceryItem)
    {
        System.out.println("entered custom repository implementation - replaceGrocery");
        System.out.println("item name : "+itemName);
        System.out.println("grocery details : "+groceryItem.toString());
/*        Optional<GroceryItem> result = mongoTemplate.update(GroceryItem.class)
                .matching(Query.query(where("itemName").is(itemName)))
                .replaceWith(new GroceryItem(groceryItem.getItemName(),groceryItem.getQuantity(),groceryItem.getCategory()))
                //.withOptions(FindAndReplaceOptions.options().upsert())
                .as(GroceryItem.class)
                .findAndReplace();*/
        //Query query = new Query().addCriteria(where("itemName").is(itemName));
        Query query1 = new Query();
        query1.addCriteria(Criteria.where("itemName").is(itemName));
        GroceryItem existingGroceryItem = mongoTemplate.findOne(query1,GroceryItem.class);
        System.out.println("temp result : "+mongoTemplate.findOne(query1,GroceryItem.class));
        GroceryItem newGroceryitem = new GroceryItem();
        newGroceryitem.setItemId(groceryItem.getItemId());
        newGroceryitem.setQuantity(groceryItem.getQuantity());
        newGroceryitem.setCategory(groceryItem.getCategory());
        newGroceryitem.setItemName(existingGroceryItem.getItemName());
//        groceryItem.setId(existingGroceryItem.getId());
        Document querynew = new Document("itemName",itemName);
        //Document replacement = new Document("_id",existingGroceryItem.ge).append("itemName","Monaco1").append("quantity",10).append("category","Snacks");
        FindAndReplaceOptions options = new FindAndReplaceOptions().upsert().returnNew();
        System.out.println("options : "+options);
        GroceryItem result = mongoTemplate.findAndReplace(query1,newGroceryitem);

//        GroceryItem groceryItem1 = mongoTemplate.insert(groceryItem);
//        Query query = new Query().addCriteria(where("itemName").is(itemName));
//        //FindAndReplaceOptions options = new FindAndReplaceOptions().upsert().returnNew();
//        //mongoTemplate.findAndReplace(query,groceryItem1,options, "GroceryItem",GroceryItem.class,)
//        GroceryItem result = mongoTemplate.findAndReplace(query, groceryItem1);

        if(result == null)
            return "No document updated";
        else
            return "document updated";
    }


//    @Override
//    public void insertGroceryHistoryAudit(GroceryHistoryAudit groceryHistoryAudit)
//    {
//
//        mongoTemplate.insert(groceryHistoryAudit,GroceryHistoryAudit.class);
//    }
}
