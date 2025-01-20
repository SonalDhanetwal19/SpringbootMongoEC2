package com.phoenix.SpringMongoDBExample.Repository;

import com.phoenix.SpringMongoDBExample.model.GroceryHistoryAudit;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryRepository extends MongoRepository<GroceryHistoryAudit, String> {
}
