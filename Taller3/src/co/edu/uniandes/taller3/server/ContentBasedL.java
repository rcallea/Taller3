package co.edu.uniandes.taller3.server;

/**
 * La idea es la siguiente para el init:
 * 1. Hacer el start
 * 
 * La idea es la siguiente para el list de recomendados:
 * 1. Pedir el usuario
 * 2. Tomar todos los documentos de los otros que sean distintos a los del usuario y al elemento e indexarlos (Bien calificados?)
 * 3. Recomendar según las entradas del usuario (Se mezclan todas o se hacen por grupos?)
 * 
 * El resultado debe dejar los predict en otro vector por si lo preguntan
 * 
 */
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.queries.mlt.MoreLikeThis;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

import co.edu.uniandes.taller3.server.MySQLConstant;
import co.edu.uniandes.taller3.shared.CBParametersL;
import co.edu.uniandes.taller3.shared.CBResultL;

public class ContentBasedL {

	private Directory indexDir;
	private StandardAnalyzer analyzer;
	private IndexWriterConfig config;
	private CBResultL cblr=new CBResultL();
	private ArrayList<Document> userDocs;
	private static String FILEINDEXER="indexes";

	public static void main(String[] args) throws IOException {
		System.out.println("Iniciando recomendador");
		ContentBasedL cbl=new ContentBasedL();
		cbl.start(); //Inicia los analizadores
		
		System.out.println("Obteniendo datos de los usuarios");
		cbl.writerEntries();
	}
	
	public void start() throws IOException{
		analyzer = new StandardAnalyzer();
		config = new IndexWriterConfig(Version.LATEST, analyzer);
		config.setOpenMode(OpenMode.CREATE_OR_APPEND);
		
		//indexDir = new RAMDirectory(); //don't write on disk
		indexDir = FSDirectory.open(new File(ContentBasedL.FILEINDEXER)); //write on disk
	}
	
	public void writerEntries() throws IOException{
		IndexWriter indexWriter = new IndexWriter(indexDir, config);
		indexWriter.commit();
		
		Connection conn = null;
		Statement stmt = null;
		try{
			//STEP 2: Register JDBC driver
			Class.forName(MySQLConstant.JDBC_DRIVER).newInstance();

			//STEP 3: Open a connection
			conn = (Connection) DriverManager.getConnection(MySQLConstant.DB_URL,MySQLConstant.USER,MySQLConstant.PASS);

			//STEP 4: Execute a query
			stmt = (Statement) conn.createStatement();
			int moreRecords=1;
			int limit=0;
			int sizeData=999;
			while(moreRecords>0) {
				String sql = "SELECT movieId, title, genres, info FROM movie WHERE info IS NOT NULL LIMIT " + limit + "," + sizeData + ";";
				System.out.println(sql);
				ResultSet rs = stmt.executeQuery(sql);
				//System.out.println("Cargando desde " + limit);
				limit=limit+sizeData+1;
				moreRecords=0;
				while(rs.next()) {
					Document doc = createDocument(rs.getString("movieId"), 
							rs.getString("title"), rs.getString("genres"),
							rs.getString("info"));
					indexWriter.addDocument(doc);
					moreRecords++;
				}
				rs.close();
				indexWriter.commit();
				indexWriter.forceMerge(100, true);
			}
		//STEP 6: Clean-up environment
			stmt.close();
			conn.close();
		}catch(SQLException se){
			//Handle errors for JDBC
			se.printStackTrace();
		}catch(Exception e){
			//Handle errors for Class.forName
			e.printStackTrace();
		}finally{
			//finally block used to close resources
			try{
				if(stmt!=null)
					stmt.close();
			}catch(SQLException se2){
			}// nothing we can do
			try{
				if(conn!=null)
					conn.close();
			}catch(SQLException se){
				se.printStackTrace();
			}//end finally try
		}//end try
		
		indexWriter.close();
	}

	private Document createDocument(String movieId, String title, String genres, String info) {
		FieldType type = new FieldType();
		type.setIndexed(true);
		type.setStored(true);
		type.setStoreTermVectors(true); //TermVectors are needed for MoreLikeThis
		
		Document doc = new Document();
		doc.add(new StringField("movieId", movieId, Store.YES));
		doc.add(new Field("title", title, type));
		doc.add(new Field("genres", genres, type));
		doc.add(new Field("info", info, type));
		return doc;
	}

	private ArrayList<Document> getUserDocs(int user, String dateI, String dateF) {
		ArrayList<Document> ret=new ArrayList<Document>();
		Connection conn = null;
		Statement stmt = null;
		try{
			//STEP 2: Register JDBC driver
			Class.forName(MySQLConstant.JDBC_DRIVER).newInstance();

			//STEP 3: Open a connection
			conn = (Connection) DriverManager.getConnection(MySQLConstant.DB_URL,MySQLConstant.USER,MySQLConstant.PASS);

			//STEP 4: Execute a query
			stmt = (Statement) conn.createStatement();
			int moreRecords=1;
			int limit=0;
			int sizeData=9999;
			while(moreRecords>0) {
				String sql = "SELECT movie.movieId, title, genres, info FROM movie WHERE movieId IN (SELECT DISTINCT rating.movieId FROM rating WHERE userId=" + user + " AND timestamp>=UNIX_TIMESTAMP('" + dateI + " 00:00:00') AND timestamp<=UNIX_TIMESTAMP('" + dateF + " 23:59:00')) AND info IS NOT NULL LIMIT " + limit + "," + sizeData + ";";
				System.out.println(sql);
				ResultSet rs = stmt.executeQuery(sql);
				//System.out.println("Cargando desde " + limit);
				limit=limit+sizeData+1;
				moreRecords=0;
				while(rs.next()) {
					ret.add(this.createDocument(rs.getString("movieId"), rs.getString("title"),
							rs.getString("genres"), rs.getString("info")));
					moreRecords++;
				}
				rs.close();
			}
		//STEP 6: Clean-up environment
			stmt.close();
			conn.close();
		}catch(SQLException se){
			//Handle errors for JDBC
			se.printStackTrace();
		}catch(Exception e){
			//Handle errors for Class.forName
			e.printStackTrace();
		}finally{
			//finally block used to close resources
			try{
				if(stmt!=null)
					stmt.close();
			}catch(SQLException se2){
			}// nothing we can do
			try{
				if(conn!=null)
					conn.close();
			}catch(SQLException se){
				se.printStackTrace();
			}//end finally try
		}//end try
		return(ret);
	}
	
	public void findSilimar(CBParametersL searchForSimilar) throws IOException {
		this.start();
		float precision=0;
		float recall=0;
		int found=0;
		Hashtable <String,Integer> moviesRecommended=new Hashtable <String,Integer>();
		ArrayList<Document> result=new ArrayList<Document>();
		ArrayList<String> resultText=new ArrayList<String>();
		IndexReader reader = DirectoryReader.open(indexDir);
		IndexSearcher indexSearcher = new IndexSearcher(reader);
		System.out.println("Obteniendo datos del usuario");
		this.userDocs=this.getUserDocs(searchForSimilar.getUser(), searchForSimilar.getDateI(), searchForSimilar.getDateF());
		for(int i=0;i<this.userDocs.size();i++) {
			moviesRecommended.put(this.userDocs.get(i).get("movieId"),1);
		}
		
		System.out.println("Creando recomendador");
		MoreLikeThis mlt = new MoreLikeThis(reader);
	    mlt.setMinTermFreq(searchForSimilar.getMinTermFrequency());
	    mlt.setMinDocFreq(searchForSimilar.getMinDocFrequency());
	    mlt.setMinWordLen(searchForSimilar.getMinWordLen());
	    mlt.setFieldNames(new String[]{"movieId", "title", "genres", "info"});
	    mlt.setAnalyzer(analyzer);
	    
	    for(int i=0;i<this.userDocs.size();i++) {
	    	Document doc=this.userDocs.get(i);
	    	String tags=this.getTags(searchForSimilar.getUser(),doc.get("movieId"));
		    Query query = mlt.like("info",new StringReader(doc.get("info") + " " + tags + " " + tags));
		    TopDocs topDocs = indexSearcher.search(query,20);
		    //queryText.add(doc.get("business_id") + ": " + doc.get("text"));
		    for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
		        Document aSimilar = indexSearcher.doc( scoreDoc.doc );
		        if(moviesRecommended.get(aSimilar.get("movieId"))==null) {
		        	moviesRecommended.put(aSimilar.get("movieId"),1);
			        result.add(aSimilar);
			        resultText.add(aSimilar.get("movieId") + ": " + aSimilar.get("info"));
		        }
		    }
	    }
 
		int maxDataSize=50;
		if(result.size()<=maxDataSize) {
			maxDataSize=result.size();
		}

		String[] totalResult=new String[maxDataSize];
		String[] retListData=new String[maxDataSize];
	    for(int i=0;i<maxDataSize;i++) {
	    	totalResult[i]=result.get(i).get("movieId");
	    	retListData[i]=result.get(i).get("title") + "(" + result.get(i).get("genres") + ")";
	    }
	    this.cblr.setData(totalResult);
		this.cblr.setDataInfo(retListData);

		ArrayList<String> userVerification=this.getNextMovies(searchForSimilar.getUser(), searchForSimilar.getDateI(), searchForSimilar.getDateF());
		System.out.println("Calculando precision y recall");
		for(int i=0; i<userVerification.size();i++) {
			if(moviesRecommended.get(userVerification.get(i))!=null) {
				found++;
			}
		}
		
		precision=((float)found)/((float)(found + result.size()));
		recall=((float)found)/((float)(found + this.userDocs.size()));
		this.cblr.setDataInfo(retListData);
		this.cblr.setPrecision(precision);
		this.cblr.setRecall(recall);
		this.cblr.setOtherDocs(this.arrayListToStringArray(resultText));
		System.out.println("Fin CBL");
	}
	
	private String[] arrayListToStringArray(ArrayList<String> al) {
		String[] ret=new String[al.size()];
		for(int i=0;i<ret.length;i++) {
			ret[i]=al.get(i);
		}
		return(ret);
	}

	public ArrayList<String> getNextMovies(int user, String dateI, String dateF) {
		ArrayList<String> ret=new ArrayList<String>();
		
		Connection conn = null;
		Statement stmt = null;
		try{
			//STEP 2: Register JDBC driver
			Class.forName(MySQLConstant.JDBC_DRIVER).newInstance();

			//STEP 3: Open a connection
			conn = (Connection) DriverManager.getConnection(MySQLConstant.DB_URL,MySQLConstant.USER,MySQLConstant.PASS);

			//STEP 4: Execute a query
			stmt = (Statement) conn.createStatement();
			String sql = "SELECT userId, movieId, rating FROM rating WHERE (timestamp<UNIX_TIMESTAMP('" + dateI + " 00:00:00') OR timestamp>UNIX_TIMESTAMP('" + dateF + " 23:59:00')) AND userId= " + user + ";";
			System.out.println(sql);
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()) {
				ret.add(rs.getString("movieId"));
			}
			rs.close();
		//STEP 6: Clean-up environment
			stmt.close();
			conn.close();
		}catch(SQLException se){
			//Handle errors for JDBC
			se.printStackTrace();
		}catch(Exception e){
			//Handle errors for Class.forName
			e.printStackTrace();
		}finally{
			//finally block used to close resources
			try{
				if(stmt!=null)
					stmt.close();
			}catch(SQLException se2){
			}// nothing we can do
			try{
				if(conn!=null)
					conn.close();
			}catch(SQLException se){
				se.printStackTrace();
			}//end finally try
		}//end try
		
		return(ret);
	}
	
	private String getTags(int userId,String movieId) {
		String ret="";
		
		Connection conn = null;
		Statement stmt = null;
		try{
			//STEP 2: Register JDBC driver
			Class.forName(MySQLConstant.JDBC_DRIVER).newInstance();

			//STEP 3: Open a connection
			conn = (Connection) DriverManager.getConnection(MySQLConstant.DB_URL,MySQLConstant.USER,MySQLConstant.PASS);

			//STEP 4: Execute a query
			stmt = (Statement) conn.createStatement();
			String sql = "SELECT tag FROM tag WHERE userId=" + userId + "  AND movieId= " + movieId + ";";
			System.out.println(sql);
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()) {
				ret = ret + " " + rs.getString("tag");
			}
			rs.close();
		//STEP 6: Clean-up environment
			stmt.close();
			conn.close();
		}catch(SQLException se){
			//Handle errors for JDBC
			se.printStackTrace();
		}catch(Exception e){
			//Handle errors for Class.forName
			e.printStackTrace();
		}finally{
			//finally block used to close resources
			try{
				if(stmt!=null)
					stmt.close();
			}catch(SQLException se2){
			}// nothing we can do
			try{
				if(conn!=null)
					conn.close();
			}catch(SQLException se){
				se.printStackTrace();
			}//end finally try
		}//end try
		
		return(ret.trim());
	}

	/**
	 * @return the cblr
	 */
	public CBResultL getCblr() {
		return cblr;
	}

	/**
	 * @param cblr the cblr to set
	 */
	public void setCblr(CBResultL cblr) {
		this.cblr = cblr;
	}
}
