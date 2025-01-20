package com.phoenix.SpringMongoDBExample.Service;

import com.phoenix.SpringMongoDBExample.Repository.CustomRepository;
import com.phoenix.SpringMongoDBExample.Repository.HistoryMongoTemplateRepository;
import com.phoenix.SpringMongoDBExample.model.GroceryHistoryAudit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GroceryHistoryAuditService {

    @Autowired
    HistoryMongoTemplateRepository historyMongoTemplateRepository;

    public void saveGroceryHistoryAuditService(GroceryHistoryAudit groceryHistoryAudit)
    {
        System.out.println(" In the saveGroceryHistoryAuditService "+historyMongoTemplateRepository);
        historyMongoTemplateRepository.saveHistory(groceryHistoryAudit);
    }
}
