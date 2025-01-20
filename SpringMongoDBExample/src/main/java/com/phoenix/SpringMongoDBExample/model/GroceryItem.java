package com.phoenix.SpringMongoDBExample.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("GroceryItem")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class GroceryItem{

    //@Id
    private int itemId;
//    @Transient
//    public static final String SEQUENCE_NAME = "grocery_sequence";
    private String itemName;
    private int quantity;
    private String category;

    public GroceryItem(String itemName, int quantity, String category) {
        this.itemName = itemName;
        this.quantity = quantity;
        this.category = category;
    }
}
