package com.phoenix.SpringMongoDBExample.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;

@Document("GroceryHistory")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GroceryHistoryAudit {
    //@Id
    private int itemId;
    private String itemName;
    private int quantity;
    private String category;
    private Date auditTimeStamp;
}
