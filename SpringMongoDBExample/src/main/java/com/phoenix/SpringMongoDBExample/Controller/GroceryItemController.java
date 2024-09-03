package com.phoenix.SpringMongoDBExample.Controller;

import com.phoenix.SpringMongoDBExample.Service.GroceryItemService;
import com.phoenix.SpringMongoDBExample.model.GroceryItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class GroceryItemController {

    @Autowired
    GroceryItemService groceryItemService;

    //1. Create grocery items
    @PostMapping("/groceryItems")
    public HttpStatus createGroceryItems(@RequestBody GroceryItem groceryItem)
    {
        HttpStatus status = groceryItemService.createGroceryItems(groceryItem);
        return status;
    }

    //2. List grocery items
    @GetMapping("/groceryItems")
    public ResponseEntity<List<GroceryItem>> getGroceryItemList()
    {
        System.out.println("entered controller for get allitems");
        List<GroceryItem> groceryItemList = new ArrayList<>();
        groceryItemList = groceryItemService.getItemDetails();
        return new ResponseEntity<>(groceryItemList,HttpStatus.OK);

    }

    //3. Show item details by itemname
    @GetMapping(value = "/groceryItems/itemName")
//    @GetMapping(value = "/groceryItems/")
    public Optional<GroceryItem> getItemDetailsByName(@RequestParam(value="itemName") String itemName)
    {
        System.out.println("entered controller for id");
        return groceryItemService.getItemDetailsByName(itemName);
    }

//    //4. Get name and quantity of all items of a particular category
    @GetMapping(value = "/groceryItems/category")
    public Optional<List<GroceryItem>> getItemByCategory(@RequestParam(value="category") String category)
    {
        System.out.println("entered controller for category");
        return groceryItemService.getItemByCategory(category);
    }

    //5. Delete item by itemName
    @DeleteMapping("/groceryItems/{id}")
    public HttpStatus deleteGroceryItem(@PathVariable("id") int id){
        System.out.println("entered controller for deleting item");
        HttpStatus status = groceryItemService.deleteGroceryItem(id);
        if(status == HttpStatus.valueOf("OK"))
        System.out.println("item deleted successfully");
        else
        System.out.println("Error with item Deletion");
        return status;
    }

    //6. populate final count of items
    @GetMapping("/groceryItems/count")
    public long getCountOfDocuments()
    {
        long count = groceryItemService.getCountOfDocuments();
        System.out.println(" count of grocery items :"+count);
        return count;
    }

    //7. Update grocery item using category and quantity using MongoRepository
    @PutMapping("/groceryItems/{itemName}")
    public ResponseEntity<GroceryItem> updateItemCategoryQuantity(@PathVariable("itemName") String itemName, @RequestBody GroceryItem groceryItem )
    {
        Optional<GroceryItem> item = groceryItemService.getItemDetailsByName(itemName);
        if(item.isPresent())
        {
            return groceryItemService.updateItemCategoryQuantity(item);


        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    //8. Update grocery item quantity using MongoTemplate
    @PutMapping("/groceryItems/quantity/{itemName}")
    public String updateItemQuantity(@PathVariable("itemName") String itemName, @RequestParam int quantity)
    {
        return groceryItemService.updateItemQuantity(itemName,quantity);

    }

}
