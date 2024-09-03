package com.phoenix.SpringMongoDBExample;

import com.phoenix.SpringMongoDBExample.Repository.ItemRepository;
import com.phoenix.SpringMongoDBExample.model.GroceryItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@EnableMongoRepositories
public class SpringMongoDbExampleApplication {

	@Autowired
	ItemRepository itemRepository;

	List<GroceryItem> groceryItemList = new ArrayList<GroceryItem>();

	public static void main(String[] args) {

		SpringApplication.run(SpringMongoDbExampleApplication.class, args);
	}

//	@Override
//	public void run(String... args) throws Exception {
//
//		// Clean up any previous data
//		itemRepository.deleteAll();

//		//1. Create grocery items
//		createGroceryItems();
//
//		//2. List grocery items
//		getGroceryItemList();
//
//		//3. Show item details by itemname
//		getItemDetailsByName("Krackjack");
//
//		//4. Get name and quantity of all items of a particular category
//		getItemByCategory("Beverages");
//
//		//5. Delete item by itemName
//		deleteGroceryItem("ice tea");
//
//		//6. populate final count of items
//		getCountOfDocuments();

//	}

	//add grocery items
//	void createGroceryItems()
//	{
//		System.out.println(" create grocery items");
//		itemRepository.save(new GroceryItem(1,"krackjack",5,"snacks"));
//		itemRepository.save(new GroceryItem(2,"monaco",4,"snacks"));
//		itemRepository.save(new GroceryItem(3,"tropicana",6,"Beverages"));
//		itemRepository.save(new GroceryItem(4,"ice tea",7,"Beverages"));
//		itemRepository.save(new GroceryItem(5,"mexican burger",10,"Burgers"));
//		itemRepository.save(new GroceryItem(6,"Americano Burger",20,"Burgers"));
//	}
//
//	// Show all grocery items
//	public void getGroceryItemList()
//	{
//			itemRepository.findAll().forEach(item -> getItemDetails(item));
//	}
//
//	public void getItemDetails(GroceryItem item)
//	{
//		System.out.println("item-id "+ item.getId() +
//							"item-name :"+ item.getItemName() +
//							"item-quantity" + item.getQuantity() +
//							"item-category"+ item.getCategory());
//	}
//
//	// Show grocery items by name
//	public void getItemDetailsByName(String itemName)
//	{
//		System.out.println("Show item details by item-name");
//		GroceryItem item = itemRepository.findItemByName(itemName);
//		System.out.println("item details : "+item);
//		getItemDetails(item);
//	}
//
//	//Get name and quantity of all items of a particular category
//	public void getItemByCategory(String itemCategory)
//	{
//		System.out.println("Show item details by item-category");
//		groceryItemList = itemRepository.findItemByCategory(itemCategory);
//		groceryItemList.forEach(item -> System.out.println("item name : "+item.getItemName() + "item quantity : "+ item.getQuantity()));
//	}
//
//
//	//Get count of documents in the collection
//	public void getCountOfDocuments()
//	{
//		long count = itemRepository.count();
//		System.out.println(" number of documents : "+count);
//	}
//
//	//Delete Grocery Item by ID
//	public void deleteGroceryItem(String itemName)
//	{
//		itemRepository.deleteById(itemName);
//		System.out.println("item deleted succesfully");
//	}

}
