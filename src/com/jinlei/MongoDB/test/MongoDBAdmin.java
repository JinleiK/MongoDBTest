package com.jinlei.MongoDB.test;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class MongoDBAdmin {
	public static void main(String[] args) {
		@SuppressWarnings("resource")
		MongoClient mongoClient = new MongoClient();
		MongoDatabase mongoDatabase = mongoClient.getDatabase("mydb");
		MongoCollection<Document> collection = mongoDatabase.getCollection("test");
		
		// get the available databases
		for (String name: mongoClient.listDatabaseNames()) {
		    System.out.println(name);
		}
		
		// create a collection
//		mongoDatabase.createCollection("testCollection1",
//				  new CreateCollectionOptions().capped(true).sizeInBytes(0x100000));
		
		// drop a collection
		mongoDatabase.getCollection("testCollection1").drop();
		
		// get the collection list
		for(String name : mongoDatabase.listCollectionNames()){
			System.out.println(name);
		}
	}
}
