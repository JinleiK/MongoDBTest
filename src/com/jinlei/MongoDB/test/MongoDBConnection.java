package com.jinlei.MongoDB.test;
import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.exists;
import static com.mongodb.client.model.Filters.gt;
import static com.mongodb.client.model.Filters.lt;
import static com.mongodb.client.model.Filters.lte;
import static com.mongodb.client.model.Projections.excludeId;
import static com.mongodb.client.model.Sorts.descending;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;


public class MongoDBConnection {

	public static void main(String[] args) {
		
		// get connection
		@SuppressWarnings("resource")
		MongoClient mongoClient = new MongoClient( "localhost" , 27017 );

		MongoDatabase database = mongoClient.getDatabase("mydb");
		
		MongoCollection<Document> collection = database.getCollection("test");
		
		collection.drop();
		
		// insert a document
		Document doc = new Document("name", "MongoDB")
        .append("type", "database")
        .append("count", 1)
        .append("info", new Document("x", 555).append("y", 123));
		
		collection.insertOne(doc);
		
		Document myDoc = collection.find().first();
        System.out.println(myDoc.toJson());
        
        // insert multiple documents
        List<Document> documents = new ArrayList<Document>(10);
        for (int i = 0; i < 10; i++) {
            documents.add(new Document("i", i));
        }
        collection.insertMany(documents);
        System.out.println("total # of documents after inserting 10 small ones (should be 11) " + collection.count());
        
        
        myDoc = collection.find().first();
        System.out.println(myDoc.toJson());
        
        // find all docs in collection
        MongoCursor<Document> cursor = collection.find().iterator();
        try{
        	while(cursor.hasNext()){
        		System.out.println(cursor.next().toJson());
        	}
        } finally {
        	cursor.close();
        }
        
        // find a single doc using filters
        myDoc = collection.find(eq("i", 5)).first();
        System.out.println(myDoc.toJson());
        
        // get a set of docs with a query
        Block<Document> printBlock = new Block<Document>() {

			@Override
			public void apply(Document t) {
				// TODO Auto-generated method stub
				System.out.println(t.toJson());
			}
		};
        collection.find(gt("i", 5)).forEach(printBlock);
        
        // get a set of docs in a range
        collection.find(and(gt("i", 5), lte("i", 8))).forEach(printBlock);
        
        
        // sort docs
        myDoc = collection.find(exists("i")).sort(descending("i")).first();
        System.out.println("i should be 9, " + myDoc.toJson());
        
        // projection
        myDoc = collection.find().projection(excludeId()).first();
        System.out.println(myDoc.toJson());
        
        
        // update one doc
        collection.updateOne(eq("i", 0), new Document("$set", new Document("i", 110)));
        System.out.println(collection.find(eq("i", 110)).first().toJson());
        
        // update multiple docs
        UpdateResult updateResult = collection.updateMany(lt("i", 10), new Document("$inc", new Document("i", 10)));
        System.out.println("there are " + updateResult.getModifiedCount() + " updates");
        
        // delete one doc
        collection.deleteOne(eq("i", 110));
        
        // delete multiple docs
        DeleteResult deleteResult = collection.deleteMany(gt("i", 15));
        System.out.println("there are " + deleteResult.getDeletedCount() + "deletes");
	}

}
