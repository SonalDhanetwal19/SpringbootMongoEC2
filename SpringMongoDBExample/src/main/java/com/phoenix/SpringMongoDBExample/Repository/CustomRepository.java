package com.phoenix.SpringMongoDBExample.Repository;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.stereotype.Repository;


public interface CustomRepository {

    public String updateQuantity(String itemName, int quantity);
}
