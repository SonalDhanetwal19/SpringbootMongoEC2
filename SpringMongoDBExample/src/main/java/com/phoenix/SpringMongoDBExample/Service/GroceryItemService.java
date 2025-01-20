package com.phoenix.SpringMongoDBExample.Service;

import com.phoenix.SpringMongoDBExample.Repository.CustomRepository;
import com.phoenix.SpringMongoDBExample.Repository.ItemRepository;
import com.phoenix.SpringMongoDBExample.model.GroceryItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class GroceryItemService {

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    CustomRepository customRepository;

//    @Autowired
//    GroceryItem groceryItemGlobal;

    @Autowired
    SequenceGeneratorService sequenceGeneratorService;

//    public GroceryItemService(ItemRepository itemRepository, CustomRepository customRepository, GroceryItem groceryItemGlobal, SequenceGeneratorService sequenceGeneratorService) {
//        this.itemRepository = itemRepository;
//        this.customRepository = customRepository;
//        this.groceryItemGlobal = groceryItemGlobal;
//        this.sequenceGeneratorService = sequenceGeneratorService;
//    }

    public HttpStatus createGroceryItems(GroceryItem groceryItem)
    {
        System.out.println(" create grocery items");
        try {
            // to load initial data enable  the below lines and remove line 30 (itemRepository.save(groceryItem));
//            itemRepository.save(new GroceryItem(1, "krackjack", 5, "snacks"));
//            itemRepository.save(new GroceryItem(2, "monaco", 4, "snacks"));
//            itemRepository.save(new GroceryItem(3, "tropicana", 6, "Beverages"));
//            itemRepository.save(new GroceryItem(4, "ice tea", 7, "Beverages"));
//            itemRepository.save(new GroceryItem(5, "mexican burger", 10, "Burgers"));
//            itemRepository.save(new GroceryItem(6, "Americano Burger", 20, "Burgers"));
            GroceryItem groceryItemGlobal =  new GroceryItem();
           // groceryItemGlobal.setId((int) sequenceGeneratorService.sequenceGeneratorService(GroceryItem.SEQUENCE_NAME));
            groceryItemGlobal.setItemId(groceryItem.getItemId());
            groceryItemGlobal.setCategory(groceryItem.getCategory());
            groceryItemGlobal.setQuantity(groceryItem.getQuantity());
            groceryItemGlobal.setItemName(groceryItem.getItemName());
            itemRepository.save(groceryItemGlobal);
            return HttpStatus.CREATED;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }

    public List<GroceryItem> getItemDetails()
    {
        List<GroceryItem> groceryItemList = new ArrayList<>();
        itemRepository.findAll().forEach(groceryItemList::add);
//        System.out.println("item-id "+ item.getId() +
//                "item-name :"+ item.getItemName() +
//                "item-quantity" + item.getQuantity() +
//                "item-category"+ item.getCategory());
        return groceryItemList;
    }

    // Show grocery items by name
    public Optional<GroceryItem> getItemDetailsByName(String itemName)
    {
        System.out.println("Show item details by item-id");
        Optional<GroceryItem> item = Optional.ofNullable(itemRepository.findItemByName(itemName));
        System.out.println("item details : "+item);
        return item;
    }

    //Get name and quantity of all items of a particular category
    public Optional<List<GroceryItem>> getItemByCategory(String itemCategory)
    {
        System.out.println("Show item details by item-category"+itemCategory);
        Optional<List<GroceryItem>> groceryItemList = Optional.ofNullable(itemRepository.findItemByCategory(itemCategory));
//        groceryItemList.forEach(item -> System.out.println("item name : "+item.getItemName() + "item quantity : "+ item.getQuantity()));
        System.out.println("grocery item list"+groceryItemList);
        return groceryItemList;
    }


    //Get count of documents in the collection
    public long getCountOfDocuments()
    {
        long count = itemRepository.count();
        System.out.println(" number of documents : "+count);
        return count;
    }

    //Delete Grocery Item by itemName
    public HttpStatus deleteGroceryItem(int id)
    {
        try {
            itemRepository.deleteById(id);

            System.out.println("item deleted succesfully");
            return HttpStatus.OK;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }

    //update ItemCategory & Quantity
    public ResponseEntity<GroceryItem> updateItemCategoryQuantity(Optional<GroceryItem> item)
    {
        GroceryItem updateItem = item.get();
        updateItem.setCategory("special category");
        updateItem.setQuantity(5);
        itemRepository.save(updateItem);
        return new ResponseEntity<>(updateItem,HttpStatus.OK);
    }

    //update Item Quantity using MongoTemplate
    public String updateItemQuantity(String itemName, int newQuantity)
    {
        return customRepository.updateQuantity(itemName, newQuantity);
    }

    public ResponseEntity<String> replaceGroceryDetails(int itemNumber, GroceryItem groceryItem)
    {
        String result = null;
        Optional<GroceryItem> existingItem = itemRepository.findById(itemNumber);
        if(!Objects.isNull(existingItem))
        {
            //result = customRepository.replaceGrocery(itemNumber,groceryItem);

        }
        ResponseEntity responseEntity = new ResponseEntity<>(result,HttpStatus.OK);
        if(result == "document updated")
        return new ResponseEntity<>(result,HttpStatus.OK);
        else return ResponseEntity.internalServerError().body("Error while replacing the document");
    }

    public ResponseEntity<String> replaceGroceryDetailsByItemName(String itemName, GroceryItem groceryItem)
    {
        System.out.println("entered service - replaceGroceryDetailsByItemName");
        String result = null;
        //Optional<GroceryItem> existingItem = itemRepository.findById(itemNumber);
        //if(!Objects.isNull(existingItem))
        //{
            result = customRepository.replaceGrocery(itemName,groceryItem);

        //}
        ResponseEntity responseEntity = new ResponseEntity<>(result,HttpStatus.OK);
        if(result == "document updated")
            return new ResponseEntity<>(result,HttpStatus.OK);
        else return ResponseEntity.internalServerError().body("Error while replacing the document");
    }


}
