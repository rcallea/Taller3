package co.edu.uniandes.taller3.server;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Hashtable;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class MongoDB {
	
	private void listUserScore() {
		MongoClient mongoClient = new MongoClient("localhost");
		DB db = mongoClient.getDB("recommenderBusiness");
		DBCollection coll = db.getCollection("review");
		BasicDBObject allQuery = new BasicDBObject();
		BasicDBObject fields = new BasicDBObject();
		fields.put("user_id", 1);
		fields.put("business_id", 2);
		fields.put("stars", 3);
		DBCursor cursor = coll.find(allQuery, fields);
		try {
			System.out.println("Usuario\tNegocio\tCalif");
			while(cursor.hasNext()) {
			   DBObject singleField=(DBObject) cursor.next();
			   System.out.print("" + singleField.get("user_id"));
			   System.out.print("\t" + singleField.get("business_id"));
			   System.out.println("\t" + singleField.get("stars"));
		   }
		} finally {
		   cursor.close();
		}
		mongoClient.close();
	}
	
	private void classifyTestData() {
		// db.review.update({},{$set : {"g60":0}},false,true)
		// db.review.update({},{$set : {"g70":0}},false,true)
		// db.review.update({},{$set : {"g80":0}},false,true)
		try {
			MongoClient mongoClient = new MongoClient("localhost");
			DB db = mongoClient.getDB("recommenderBusiness");
			DBCollection coll = db.getCollection("review");
			BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\Ricardo\\Documents\\Maestría\\05SRecomendacion\\distribucion.txt"));
			String line;
			line = reader.readLine();
			String[] tokens;
			while (line != null) {
				// Skip comment lines
				if (line.trim().startsWith("//")) {
					line = reader.readLine();
					continue;
				}
				tokens = line.split("\t");
				String usuario=tokens[0];
				System.out.println("Usuario actual: " + usuario);
				int g60=Integer.parseInt(tokens[2]);
				int g70=Integer.parseInt(tokens[3]);
				int g80=Integer.parseInt(tokens[4]);
				// First, add the ratings.
				//dm.addRating(Integer.parseInt(tokens[0]), Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]));
				line = reader.readLine();
				
				//TODO aquí se establece la consulta para que solo saque el usuario
				BasicDBObject allQuery = new BasicDBObject().append("user_id", usuario);
				BasicDBObject fields = new BasicDBObject();
//				fields.put("user_id", 1);
//				fields.put("business_id", 2);
//				fields.put("stars", 3);

				DBCursor cursor = coll.find(allQuery, fields);
				try {
//					System.out.println("Usuario\tNegocio\tCalif");
					while(cursor.hasNext()) {
					   BasicDBObject singleField=(BasicDBObject) cursor.next();
					   int value=0;
					   if(g60>0) {
						   g60--;
						   value=1;
					   }
					   BasicDBObject newDocument=new BasicDBObject();
					   newDocument.append("$set", new BasicDBObject().append("g60", value));
					   coll.update(singleField, newDocument);
					   value=0;

					   if(g70>0) {
						   g70--;
						   value=1;
					   }
					   newDocument=new BasicDBObject();
					   newDocument.append("$set", new BasicDBObject().append("g70", value));
					   coll.update(singleField, newDocument);
					   value=0;
					   
					   if(g80>0) {
						   g80--;
						   value=1;
					   }
					   newDocument=new BasicDBObject();
					   newDocument.append("$set", new BasicDBObject().append("g80", value));
					   coll.update(singleField, newDocument);

//					   System.out.print("" + singleField.get("user_id"));
//					   System.out.print("\t" + singleField.get("business_id"));
//					   System.out.println("\t" + singleField.get("stars"));
					   System.out.println(singleField);
					   
					}
				} finally {
				   cursor.close();
				}
			}
			reader.close();
			mongoClient.close();
		} catch (Exception e) {}
	}
	
	public static ArrayList<String[]> getBusinessInfo(String[] business_id) {
		String[] getFields={"business_id", "name", "full_address", "city", "state", "stars", "type", "latitude", "longitude"};
		ArrayList<String[]> ret=new ArrayList<String[]>();
		ArrayList<String[]> retTemp=new ArrayList<String[]>();
		MongoClient mongoClient = new MongoClient("localhost");
		DB db = mongoClient.getDB("recommenderBusiness");
		DBCollection coll = db.getCollection("business");
		BasicDBObject fields = new BasicDBObject();
		BasicDBObject allQuery = new BasicDBObject();
		allQuery.append("business_id", new BasicDBObject("$in", business_id));
		int fieldPosition=1;
		for(;fieldPosition<getFields.length + 1; fieldPosition++) {
			fields.put(getFields[fieldPosition-1], fieldPosition);
		}
		//System.out.println("Buscando: " + business_id[i]);
		DBCursor cursor = coll.find(allQuery, fields);
		try {
			//Recorre los resultados
			while(cursor.hasNext()) {
			   DBObject singleField=(DBObject) cursor.next();
			   String[] retrievedData=new String[getFields.length];
			   for(fieldPosition=0;fieldPosition<getFields.length;fieldPosition++) {
				   try {
					   retrievedData[fieldPosition] = singleField.get(getFields[fieldPosition]).toString();
				   } catch (NullPointerException e) {
					   retrievedData[fieldPosition] = "Empty";
				   }
			   }
			   retTemp.add(retrievedData);
			   //System.out.print("" + singleField.get(getFields[0]));
			   //System.out.println("\t" + singleField.get(getFields[1]));
		   }
		} catch (Exception e) {
			System.out.println(e);
			cursor.close();
		} finally {
		   cursor.close();
		}
		
		for(int i=0;i<business_id.length;i++) {
			for(int j=0;j<retTemp.size();j++) {
				String[] retrievedData=retTemp.get(j);
				if(retrievedData[0].equals(business_id[i])) {
					j=retTemp.size();
					ret.add(retrievedData);
				}
			}
		}
		
		mongoClient.close();
		return(ret);
	}

	public static Hashtable<String, Integer> getUserBusiness(String user) {
		Hashtable<String, Integer> ret=new Hashtable<String, Integer>();
		System.setProperty("DEBUG.MONGO", "false");
		MongoClient mongoClient = new MongoClient("localhost");
		DB db = mongoClient.getDB("recommenderBusiness");
		DBCollection coll = db.getCollection("review");
		BasicDBObject allQuery = new BasicDBObject().append("user_id", user);
		BasicDBObject fields = new BasicDBObject();
		int posicionConsulta=1;
		fields.put("_id", posicionConsulta++);
		fields.put("user_id", posicionConsulta++);
		fields.put("business_id", posicionConsulta++);
		fields.put("text", posicionConsulta);
		DBCursor cursor = coll.find(allQuery, fields);

		try {
			while(cursor.hasNext()) {
				try {
					DBObject dbo=cursor.next();
					String _id=dbo.get("_id").toString();
					String user_id=dbo.get("user_id").toString();
					String business_id=dbo.get("business_id").toString();
					String text=dbo.get("text").toString();
					if(ret.get(business_id)==null) {
						ret.put(business_id, 1);
					}
					//System.out.println(_id + ": \t" + user_id + "\t" + business_id);
				} catch (NullPointerException e) {}
			}
		} finally {
			cursor.close();
		}
		mongoClient.close();
		return(ret);
	}
	
	public static void main(String[] args) {
		//new MongoDB().classifyTestData();
		String[] rrr={"rv7CY8G_XibTx82YhuqQRw", "4GdYyHKukZiMiQu0KJ0Jnw"};
		ArrayList<String[]> bi=MongoDB.getBusinessInfo(rrr);
		//Hashtable<String, Integer> bi=MongoDB.getUserBusiness("__jP-g2SFRLaoPrIHFL0LA");
		for(int i=0;i<bi.size();i++) {
			String[] aaa=bi.get(i);
			System.out.println(aaa[0] + "\t" +aaa[1] + "\t" +aaa[2] + "\t" +aaa[3] + "\t" );
		}
	}

}
