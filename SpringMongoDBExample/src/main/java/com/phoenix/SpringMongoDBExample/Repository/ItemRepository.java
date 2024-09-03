package com.phoenix.SpringMongoDBExample.Repository;

import com.phoenix.SpringMongoDBExample.model.GroceryItem;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ItemRepository extends MongoRepository<GroceryItem, Integer> {

//    @Query("{name:'?0'}")
//    GroceryItem findItemByName(String Name);

//    "{'itemName':?0}", fields = "{'name':1, 'quantity':1}") -- this one works
    @Query(value = "{'itemName':?0}")
    GroceryItem findItemByName(String itemName);
    @Query(value = "{'category':?0}")
    List<GroceryItem> findItemByCategory(String category);

    public long count();
}
