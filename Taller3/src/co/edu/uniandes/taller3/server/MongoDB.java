package co.edu.uniandes.taller3.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.text.html.HTMLEditorKit.Parser;

import co.edu.uniandes.taller3.shared.JaccardCoefficient;
import co.edu.uniandes.taller3.shared.Movie;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.WriteResult;
import com.mongodb.util.JSON;
import com.mongodb.BasicDBList;

public class MongoDB {
	
	private static String baseName = "RecommenderWebSemantic";
	//private static String baseName = "recommenderTest";
	
	public static void main(String[] args) {
		EvaluacionOntologico();
	}
	
	public static List<Movie> EncontrarItemsSimilares(List<String> moviesUser)
	{
		List lstMovies = new ArrayList<Movie>();
		try {
			
			MongoClient mongoClient = new MongoClient("localhost");
	     	DB db = mongoClient.getDB(baseName);
	     	DBCollection collection = db.getCollection("MatrizSimilitud");
	     	
	     	HashMap<String,Double> map = new HashMap<String,Double>();
	     	for (String movieId : moviesUser) {
	     		//consulta en x
		     	BasicDBObject allQuery = new BasicDBObject("Similitud",new BasicDBObject("$gt","0.0"));
		     	BasicDBObject fields = new BasicDBObject();
		     	allQuery.put("MovieId_X", movieId);
		     	fields.put("Similitud", 1);
		     	fields.put("MovieId_Y", 1);
		     	DBCursor cursor1 = collection.find(allQuery, fields).sort( new BasicDBObject( "Similitud" , -1 )).limit(1000);
		     	
		     	//consulta en Y
		     	BasicDBObject allQuery2 = new BasicDBObject("Similitud",new BasicDBObject("$gt","0.0"));
		     	BasicDBObject fields2 = new BasicDBObject();
		     	allQuery2.put("MovieId_Y", movieId);
		     	fields2.put("Similitud", 1);
		     	fields2.put("MovieId_X", 1);
		     	DBCursor cursor2 = collection.find(allQuery2, fields2).sort( new BasicDBObject( "Similitud" , -1 )).limit(1000);
		     	
		     	while(cursor1.hasNext())
		     	{
		     		DBObject obj = (DBObject) cursor1.next();
		     		String movieY = (String)obj.get("MovieId_Y");
		     		double similitud = Double.parseDouble(obj.get("Similitud").toString());
		     		if(!map.containsKey(movieY))
		     			map.put(movieY,similitud);
		     	}
		     	cursor1.close();
		     	
		     	while(cursor2.hasNext())
		     	{
		     		DBObject obj = (DBObject) cursor2.next();
		     		String movieX = (String)obj.get("MovieId_X");
		     		double similitud = Double.parseDouble(obj.get("Similitud").toString());
		     		if(!map.containsKey(movieX))
		     			map.put(movieX,similitud);
		     	}
		     	cursor2.close();
		     	
			}
	     	
	     	ValueComparator bvc =  new ValueComparator(map);
            TreeMap<String,Double> sorted_map = new TreeMap<String,Double>(bvc);
            sorted_map.putAll(map);
            
	    
            int i = 0;
            for(Map.Entry<String,Double> entry : sorted_map.entrySet()) {
            	Movie movie = GetDatosPelicula(entry.getKey());
            	lstMovies.add(movie);
            	i++;
            	
            	if(i == 30)
            		break;
  			}
  		
		} catch (Exception e) {
			System.out.println(e);
		}
		
		return lstMovies;
	}
		
	private static void EvaluacionOntologico()
	{
		String movieId = "940";
		List lstMovies = new ArrayList<String>();
		try {
			
			MongoClient mongoClient = new MongoClient("localhost");
	     	DB db = mongoClient.getDB(baseName);
	     	DBCollection collection = db.getCollection("MatrizSimilitud");
	     	
	     	HashMap<String,Double> map = new HashMap<String,Double>();
	     	
     		//consulta en x
	     	BasicDBObject allQuery = new BasicDBObject("Similitud",new BasicDBObject("$gt","0.0"));
	     	BasicDBObject fields = new BasicDBObject();
	     	allQuery.put("MovieId_X", movieId);
	     	fields.put("Similitud", 1);
	     	fields.put("MovieId_Y", 1);
	     	DBCursor cursor1 = collection.find(allQuery, fields).sort( new BasicDBObject( "Similitud" , -1 )).limit(1000);
	     	
	     	//consulta en Y
	     	BasicDBObject allQuery2 = new BasicDBObject("Similitud",new BasicDBObject("$gt","0.0"));
	     	BasicDBObject fields2 = new BasicDBObject();
	     	allQuery2.put("MovieId_Y", movieId);
	     	fields2.put("Similitud", 1);
	     	fields2.put("MovieId_X", 1);
	     	DBCursor cursor2 = collection.find(allQuery2, fields2).sort( new BasicDBObject( "Similitud" , -1 )).limit(1000);
	     	
	     	while(cursor1.hasNext())
	     	{
	     		DBObject obj = (DBObject) cursor1.next();
	     		String movieY = (String)obj.get("MovieId_Y");
	     		double similitud = Double.parseDouble(obj.get("Similitud").toString());
	     		map.put(movieY,similitud);
	     	}
	     	cursor1.close();
	     	
	     	while(cursor2.hasNext())
	     	{
	     		DBObject obj = (DBObject) cursor2.next();
	     		String movieX = (String)obj.get("MovieId_X");
	     		double similitud = Double.parseDouble(obj.get("Similitud").toString());
	     		map.put(movieX,similitud);
	     	}
	     	cursor2.close();
	     	 	
	     	ValueComparator bvc =  new ValueComparator(map);
            TreeMap<String,Double> sorted_map = new TreeMap<String,Double>(bvc);
            sorted_map.putAll(map);
            
            int i = 0;
            for(Map.Entry<String,Double> entry : sorted_map.entrySet()) {
            	lstMovies.add(entry.getKey());
            	i++;
            	
            	if(i == 100)
            		break;
  			}
            
            ///----------------
            MySQLQuery query = new MySQLQuery();
            List<String> lstUsers = new ArrayList<String>();
            List<String> lstMoviesUsers = new ArrayList<String>();
            
            lstUsers = query.getRatingsMovieEval(movieId);

            System.out.println("MovieId: " + movieId + " Peliculas Similares: " + lstMovies);
            for (String userId : lstUsers) {
            	
            	lstMoviesUsers = query.getRatingsUserEval(userId);
            	
            	List<String> lstEncontro = new ArrayList<String>();
            	for (String movie : lstMoviesUsers) {
            		if(lstMovies.contains(movie))
            			lstEncontro.add(movie);
            	}
            	
            	double TP = lstEncontro.size();
            	double FP = lstMovies.size() - lstEncontro.size();
            	double FN = 0;
            	if(lstMoviesUsers.size() - lstMovies.size() > 0)
            		FN = lstMoviesUsers.size() - lstMovies.size();
            	
            	double presicion = TP/(TP + FP);
            	double recall = TP/(TP + FN);
            	
            	System.out.println("User: " + userId + " ; TP: " + TP + " ; FP: " + FP + " ; FN: " + FN + " ; Precision: "+ presicion+ " ; Recall: " + recall + " ; Peliculas usuario: " + lstMoviesUsers);
            }
  		
		} catch (Exception e) {
			System.out.println(e);
		}
		
		
	}
	
	private static String[] GetNodoList(DBObject objectGeneral, String nodo)
	{
		
		String[] arrValue = null;
		BasicDBList existList = (BasicDBList)objectGeneral.get(nodo);
		if (existList != null) {
			Object[] listChildren = ((BasicDBList) objectGeneral.get(nodo)).toArray();
	  		arrValue = new String[listChildren.length];
	  		for (int i = 0; i < listChildren.length; i++) {
	  			DBObject objectValue = (DBObject) listChildren[i];
	  			if(objectValue != null)
	  				arrValue[i] = objectValue.get("value").toString();
	  		}
		}  		
  		return arrValue;
	}
	
	private static String GetNodoListName(DBObject objectGeneral, String nodo)
	{
		
		String[] arrValue = null;
		BasicDBList existList = (BasicDBList)objectGeneral.get(nodo);
		if (existList != null) {
			Object[] listChildren = ((BasicDBList) objectGeneral.get(nodo)).toArray();
	  		arrValue = new String[listChildren.length];
	  		for (int i = 0; i < listChildren.length; i++) {
	  			DBObject objectValue = (DBObject) listChildren[i];
	  			if(objectValue != null)
	  				arrValue[i] = objectValue.get("value").toString();
	  		}
		}  		
  		return arrValue[0];
	}
	
	private static String[] GetNodoListAbstract(DBObject objectGeneral, String nodo)
	{
		String[] arrValue = new String[1];
		BasicDBList existList = (BasicDBList)objectGeneral.get(nodo);
		if (existList != null) {
			Object[] listChildren = ((BasicDBList) objectGeneral.get(nodo)).toArray();
	  		for (int i = 0; i < listChildren.length; i++) {
	  			DBObject objectValue = (DBObject) listChildren[i];
	  			String val  = (String)objectValue.get("lang");
	  			if(val != null && val.toString().equals("en")){
	  				arrValue[0] = objectValue.get("value").toString();
	  			}
	  		}
		}  		
  		return arrValue;
	}
	
	private static Movie GetDatosPelicula(String movieId){
		
		Movie movie = new Movie();
		
		try {
            
            MongoClient mongoClient = new MongoClient("localhost");
            DB db = mongoClient.getDB(baseName);
            
            DBCollection collection = db.getCollection("Movies");
            
            BasicDBObject allQuery = new BasicDBObject();
            BasicDBObject fields = new BasicDBObject();
      	  	allQuery.put("MovieId", movieId);
      	  	
      	  	DBCursor cursor2 = collection.find(allQuery, fields);
		  	
      	  	movie.setMovieId(movieId);
      	  	
		  	while (cursor2.hasNext()) {
		  		DBObject cursor = cursor2.next();
		  		String movieUri = (String)cursor.get("MovieUri");
		  		movie.setMovieUri(movieUri);
		  		
		  		DBObject objectGeneral = (DBObject) cursor.get(movieUri);
		  		movie.setName(GetNodoListName(objectGeneral, "http://dbpedia%2Eorg/property/name"));
		  		movie.setListAbstract(GetNodoListAbstract(objectGeneral, "http://dbpedia%2Eorg/ontology/abstract"));
		  		movie.setListCinematography(GetNodoList(objectGeneral, "http://dbpedia%2Eorg/property/cinematography"));
		  		movie.setListDirector(GetNodoList(objectGeneral, "http://dbpedia%2Eorg/property/director"));
		  		movie.setListDistribuidor(GetNodoList(objectGeneral, "http://dbpedia%2Eorg/property/distributor"));
		  		movie.setListEditor(GetNodoList(objectGeneral, "http://dbpedia%2Eorg/property/editing"));
		  		movie.setListCompositor(GetNodoList(objectGeneral, "http://dbpedia%2Eorg/ontology/musicComposer"));
		  		movie.setListCaption(GetNodoList(objectGeneral, "http://dbpedia%2Eorg/property/caption"));
		  		movie.setListAlt(GetNodoList(objectGeneral, "http://dbpedia%2Eorg/property/alt"));
		  		movie.setListPais(GetNodoList(objectGeneral, "http://dbpedia%2Eorg/property/country"));
		  		movie.setListFooter(GetNodoList(objectGeneral, "http://dbpedia%2Eorg/property/footer"));
		  		movie.setListLenguaje(GetNodoList(objectGeneral, "http://dbpedia%2Eorg/property/language"));
		  		movie.setListMusic(GetNodoList(objectGeneral, "http://dbpedia%2Eorg/property/music"));
		  		movie.setListProductor(GetNodoList(objectGeneral, "http://dbpedia%2Eorg/property/producer"));
		  		movie.setListEstudio(GetNodoList(objectGeneral, "http://dbpedia%2Eorg/property/studio"));
		  		movie.setListEscritor(GetNodoList(objectGeneral, "http://dbpedia%2Eorg/property/writer"));
		  		movie.setListSubject(GetNodoList(objectGeneral, "http://purl%2Eorg/dc/terms/subject"));
		  		movie.setListActores(GetNodoList(objectGeneral, "http://dbpedia%2Eorg/property/starring"));
			}
	         mongoClient.close();
       } 
		catch (Exception ex) {
            ex.printStackTrace();
        }
		
		return movie;
	}
	
	private static double EncontrarSimilitud(Movie movie0, Movie movie1)
	{
		/**************************************/
		
		double similitud = 0;
		JaccardCoefficient jc = new JaccardCoefficient();
		if(movie0.getListCinematography() != null && movie1.getListCinematography() != null)
			similitud = jc.similarity(movie0.getListCinematography(), movie1.getListCinematography());
		
		if(movie0.getListDirector() != null && movie1.getListDirector() != null)
			similitud += jc.similarity(movie0.getListDirector(), movie1.getListDirector());
		
		if(movie0.getListDistribuidor() != null && movie1.getListDistribuidor() != null)
			similitud += jc.similarity(movie0.getListDistribuidor(), movie1.getListDistribuidor());
		
		if(movie0.getListEditor() != null && movie1.getListEditor() != null)
			similitud += jc.similarity(movie0.getListEditor(), movie1.getListEditor());
		
		if(movie0.getListCompositor() != null && movie1.getListCompositor() != null)
			similitud += jc.similarity(movie0.getListCompositor(), movie1.getListCompositor());
		
		if(movie0.getListPais() != null && movie1.getListPais() != null)
			similitud += jc.similarity(movie0.getListPais(), movie1.getListPais());
		
		if(movie0.getListMusic() != null && movie1.getListMusic() != null)
			similitud += jc.similarity(movie0.getListMusic(), movie1.getListMusic());
		
		if(movie0.getListLenguaje() != null && movie1.getListLenguaje() != null)
			similitud += jc.similarity(movie0.getListLenguaje(), movie1.getListLenguaje());
		
		if(movie0.getListProductor() != null && movie1.getListProductor() != null)
			similitud += jc.similarity(movie0.getListProductor(), movie1.getListProductor());
		
		if(movie0.getListEstudio() != null && movie1.getListEstudio() != null)
			similitud += jc.similarity(movie0.getListEstudio(), movie1.getListEstudio());
		
		if(movie0.getListEscritor() != null && movie1.getListEscritor() != null)
			similitud += jc.similarity(movie0.getListEscritor(), movie1.getListEscritor());
		
		if(movie0.getListSubject() != null && movie1.getListSubject() != null)
			similitud += jc.similarity(movie0.getListSubject(), movie1.getListSubject());
	
		if(movie0.getListActores() != null && movie1.getListActores() != null)
			similitud += jc.similarity(movie0.getListActores(), movie1.getListActores()) * 5;
		
		return similitud;
	}
	
}

class ValueComparator implements Comparator<String> {

    Map<String, Double> base;
    public ValueComparator(Map<String, Double> base) {
        this.base = base;
    }

    // Note: this comparator imposes orderings that are inconsistent with equals.    
    public int compare(String a, String b) {
        if (base.get(a) >= base.get(b)) {
            return -1;
        } else {
            return 1;
        } // returning 0 would merge keys
    }
}