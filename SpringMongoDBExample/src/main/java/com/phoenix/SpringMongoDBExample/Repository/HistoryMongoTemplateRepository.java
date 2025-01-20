package com.phoenix.SpringMongoDBExample.Repository;

import com.phoenix.SpringMongoDBExample.model.GroceryHistoryAudit;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryMongoTemplateRepository {
    GroceryHistoryAudit saveHistory(GroceryHistoryAudit groceryHistoryAudit);
}
