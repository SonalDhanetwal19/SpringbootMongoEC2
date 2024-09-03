package com.phoenix.SpringMongoDBExample.Service;

import com.phoenix.SpringMongoDBExample.Repository.CustomRepository;
import com.phoenix.SpringMongoDBExample.Repository.CustomRepositoryImpl;
import com.phoenix.SpringMongoDBExample.Repository.ItemRepository;
import com.phoenix.SpringMongoDBExample.model.GroceryItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GroceryItemService {

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    CustomRepository customRepository;

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
            itemRepository.save(groceryItem);
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


}
