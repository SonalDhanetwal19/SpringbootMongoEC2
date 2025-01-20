package com.phoenix.SpringMongoDBExample.Repository;

import com.phoenix.SpringMongoDBExample.model.GroceryHistoryAudit;
import com.phoenix.SpringMongoDBExample.model.GroceryItem;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomRepository {

    public String updateQuantity(String itemName, int quantity);
    public String replaceGrocery(String itemName, GroceryItem groceryItem);
    //public void insertGroceryHistoryAudit(GroceryHistoryAudit groceryHistoryAudit);
}
