package com.phoenix.SpringMongoDBExample.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("GroceryItem")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GroceryItem {

    @Id
    private int id;

    private String itemName;
    private int quantity;
    private String category;
}
