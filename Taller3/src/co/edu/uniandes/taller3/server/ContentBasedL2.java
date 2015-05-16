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
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Set;

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
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

import co.edu.uniandes.taller3.shared.CBParametersL;
import co.edu.uniandes.taller3.shared.CBResultL;

public class ContentBasedL2 {

	private Directory indexDir;
	private StandardAnalyzer analyzer;
	private IndexWriterConfig config;
	private CBResultL cblr=new CBResultL();
	//private ArrayList<Document> userDocs;
	private static final String[] FIELDS={"_id", "business_id", "open", "hours", "categories", "city", "state", "attributes", "type", "keyWordsName", "keyWordsCategories", "keyWordsComments", "keyWordsAttributes"};

	public CBResultL initCB(CBParametersL data) throws IOException {
		System.out.println("Iniciando recomendador");
		start(); //Inicia los analizadores
		
		System.out.println("Obteniendo datos de los usuarios");
		writerEntries(""+data.getUser(), (int)(data.getWaitTime()*60));
		
		System.out.println("Recomendando elementos parecidos");
		findSilimar(data);
		return(this.cblr);
	}
	
	public void start() throws IOException{
		analyzer = new StandardAnalyzer();
		config = new IndexWriterConfig(Version.LATEST, analyzer);
		config.setOpenMode(OpenMode.CREATE_OR_APPEND);
		
		indexDir = new RAMDirectory(); //don't write on disk
		//indexDir = FSDirectory.open(new File("/Path/to/luceneIndex/")); //write on disk
	}
	
	public void writerEntries(String user, int secondsToWait) throws IOException{
		IndexWriter indexWriter = new IndexWriter(indexDir, config);
		indexWriter.commit();
		Hashtable<String, Integer> userBusiness=MongoDB.getUserBusiness(user);
		MongoClient mongoClient = new MongoClient("localhost");
		DB db = mongoClient.getDB("recommenderBusiness");
		DBCollection coll = db.getCollection("business");
		BasicDBObject allQuery = new BasicDBObject().append("stars", new BasicDBObject("$gt",3));
		BasicDBObject fields = new BasicDBObject();
		for(int posicionConsulta=0;posicionConsulta<ContentBasedL2.FIELDS.length;posicionConsulta++) {
			fields.put(ContentBasedL2.FIELDS[posicionConsulta], posicionConsulta+1);
		}
		DBCursor cursor = coll.find(allQuery, fields);

		try {
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.SECOND, secondsToWait);
			Date now = new Date();
			while(cursor.hasNext() && now.before(calendar.getTime())) {
				try {
					DBObject dbo=cursor.next();
					String business_id=dbo.get("business_id").toString();
					
					if(userBusiness.get(business_id)==null) {
						String queryFields="";
						for(int i=2;i<ContentBasedL2.FIELDS.length;i++) {
							try {
								if(ContentBasedL2.FIELDS[i].equals("hours")) {
									queryFields = queryFields + " " + this.processHours(ContentBasedL2.FIELDS[i], (DBObject)dbo.get(ContentBasedL2.FIELDS[i]));
								} else if(ContentBasedL2.FIELDS[i].equals("attributes")) {
									queryFields = queryFields + " " + this.processAttributes(ContentBasedL2.FIELDS[i], (DBObject)dbo.get(ContentBasedL2.FIELDS[i]));
								} else {
									String str=dbo.get(ContentBasedL2.FIELDS[i]).toString();
									str=str.replace("\"", "").replace("[", "").replace("]", "").replace("{", "").replace("}", "").replace(",", " ").trim();
									queryFields = queryFields + " " + ContentBasedL2.FIELDS[i] + " " + str;
								}
							} catch (Exception e) {}
						}
						Document doc = createDocument(business_id, queryFields);
						//System.out.println(queryFields);
						indexWriter.addDocument(doc);
					}
				} catch (NullPointerException e) {
					e.printStackTrace();
				}
				now = new Date();
			}
			indexWriter.commit();
			indexWriter.forceMerge(100, true);
			indexWriter.close();
		} finally {
			cursor.close();
		}
		mongoClient.close();
	}

	private Document createDocument(String business_id, String values) {
		FieldType type = new FieldType();
		type.setIndexed(true);
		type.setStored(true);
		type.setStoreTermVectors(true); //TermVectors are needed for MoreLikeThis
		
		Document doc = new Document();
		doc.add(new StringField(ContentBasedL2.FIELDS[1], business_id, Store.YES));
		doc.add(new Field("values", values, type));
		return doc;
	}

	private ArrayList<Document> getUserDocs(String user) {
		ArrayList<Document> ret=new ArrayList<Document>();
		Hashtable<String, Integer> userBusiness=MongoDB.getUserBusiness(user);
		MongoClient mongoClient = new MongoClient("localhost");
		DB db = mongoClient.getDB("recommenderBusiness");
		DBCollection coll = db.getCollection("business");
		BasicDBObject allQuery = new BasicDBObject().append("stars", new BasicDBObject("$gt",3));
		allQuery.append("business_id", new BasicDBObject("$in", userBusiness.keySet()));
		BasicDBObject fields = new BasicDBObject();
		for(int posicionConsulta=0;posicionConsulta<ContentBasedL2.FIELDS.length;posicionConsulta++) {
			fields.put(ContentBasedL2.FIELDS[posicionConsulta], posicionConsulta+1);
		}

		DBCursor cursor = coll.find(allQuery, fields);
		try {
			while(cursor.hasNext()) {
				try {
					DBObject dbo=cursor.next();
					String business_id=dbo.get("business_id").toString();
					
					String queryFields="";
					for(int i=2;i<ContentBasedL2.FIELDS.length;i++) {
						try {
							if(ContentBasedL2.FIELDS[i].equals("hours")) {
								queryFields = queryFields + " " + this.processHours(ContentBasedL2.FIELDS[i], (DBObject)dbo.get(ContentBasedL2.FIELDS[i]));
							} else if(ContentBasedL2.FIELDS[i].equals("attributes")) {
								queryFields = queryFields + " " + this.processAttributes(ContentBasedL2.FIELDS[i], (DBObject)dbo.get(ContentBasedL2.FIELDS[i]));
							} else {
								String str=dbo.get(ContentBasedL2.FIELDS[i]).toString();
								str=str.replace("\"", "").replace("[", "").replace("]", "").replace("{", "").replace("}", "").replace(",", " ").trim();
								queryFields = queryFields + " " + ContentBasedL2.FIELDS[i] + " " + str;
							}
						} catch (Exception e) {}
					}
					ret.add(createDocument(business_id, queryFields));
					//System.out.println(queryFields);
				} catch (NullPointerException e) {}
			}
		} finally {
			cursor.close();
		}
		mongoClient.close();
		return(ret);
	}
	
	private void findSilimar(CBParametersL searchForSimilar) throws IOException {
		float precision=0;
		float recall=0;
		int found=0;
		Hashtable <String,Integer> businessReferenced=new Hashtable <String,Integer>();
		IndexReader reader = DirectoryReader.open(indexDir);
		IndexSearcher indexSearcher = new IndexSearcher(reader);
		ArrayList<Document> userDocs=this.getUserDocs(""+searchForSimilar.getUser());
		ArrayList<Document> userVerification=new ArrayList<Document>();
		ArrayList<Document> result=new ArrayList<Document>();
		ArrayList<String> queryText=new ArrayList<String>();
		ArrayList<String> resultText=new ArrayList<String>();
		
		MoreLikeThis mlt = new MoreLikeThis(reader);
	    mlt.setMinTermFreq(searchForSimilar.getMinTermFrequency());
	    mlt.setMinDocFreq(searchForSimilar.getMinDocFrequency());
	    mlt.setMinWordLen(searchForSimilar.getMinWordLen());
	    mlt.setFieldNames(new String[]{"business_id", "values"});
	    mlt.setAnalyzer(analyzer);
	    
	    double docsSelected=1;
	    
	    int posicion=0;
	    while((double)posicion<docsSelected) {
	    	Document doc=userDocs.get(posicion);
		    Query query = mlt.like("values",new StringReader(doc.get("values")));
		    TopDocs topDocs = indexSearcher.search(query,20);
		    queryText.add(doc.get("business_id") + ": " + doc.get("values"));
		    for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
		        Document aSimilar = indexSearcher.doc( scoreDoc.doc );
		        if(businessReferenced.get(aSimilar.get("business_id"))==null) {
		        	businessReferenced.put(aSimilar.get("business_id"),1);
		        }
		        
		        result.add(aSimilar);
		        resultText.add(aSimilar.get("business_id") + ": " + aSimilar.get("values"));
		        //System.out.println("Similar: " + aSimilar.get("business_id") + ": " + aSimilar.get("values"));
		    }
	    	posicion++;
	    }
	    while(posicion<userDocs.size()) {
	    	userVerification.add(userDocs.get(posicion));
	    	posicion++;
	    }
	    
	    String[] totalResult=new String[result.size()];
	    for(int i=0;i<result.size();i++) {
	    	totalResult[i]=result.get(i).get("business_id");
	    }
	    this.cblr.setData(totalResult);
		
		int maxDataSize=50;
		String[] retListSearch;
		if(totalResult.length<=maxDataSize) {
			maxDataSize=totalResult.length;
		}
		String[] retListData=new String[maxDataSize];
		retListSearch=new String[maxDataSize];
		
		for(int i=0;i<maxDataSize;i++) {
			retListSearch[i]=totalResult[i];
		}
		
		ArrayList<String[]> retListAllData=MongoDB.getBusinessInfo(retListSearch);
		for(int i=0;i<retListAllData.size();i++) {
			String[] businessData=retListAllData.get(i);
			retListData[i]=businessData[7] + ": " +businessData[1] + " (" + businessData[4] + " - " + businessData[5] + ")"; 
		}

		System.out.println("Calculando precision y recall");
		for(int i=0; i<userVerification.size();i++) {
			if(businessReferenced.get(userVerification.get(i).get("business_id"))!=null) {
				found++;
			}
		}
		precision=((float)found)/((float)(found + result.size()));
		recall=((float)found)/((float)(found + userDocs.size()));
		this.cblr.setDataInfo(retListData);
		this.cblr.setPrecision(precision);
		this.cblr.setRecall(recall);
		this.cblr.setUserDocs(this.arrayListToStringArray(queryText));
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
	
	private String processHours(String field, DBObject cursor) {
		String ret="";
		String[] days={"Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"};
		String[] hours={"00:00", "01:00", "02:00", "03:00", "04:00", "05:00", "06:00", "07:00", "08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00", "00:00", "01:00", "02:00", "03:00", "04:00", "05:00", "06:00", "07:00", "08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00"};
		for(int i=0;i<days.length;i++) {
			try{
				DBObject dbo=(DBObject)cursor.get(days[0]);
				String open=dbo.get("open").toString();
				String close=dbo.get("close").toString();
				//System.out.println("open: " + open + " close: " + close);
				int posOpen=-1;
				int posClose=-1;
				for(int j=0;j<23;j++) {
					if(Integer.parseInt(open.substring(0, 2))==j) {
						posOpen=j;
						j=24;
					}
				}
				for(int j=0;j<23;j++) {
					if(Integer.parseInt(close.substring(0, 2))==j) {
						posClose=j;
						j=24;
					}
				}
				
				if(posOpen!=-1) {
					if(posOpen>posClose) {
						posClose+=24;
					}
					String add="";
					for(int j=posOpen;j<posClose;j+=posClose) {
						add = add + " " + days[i] + hours[j];
					}
					ret = ret + " " + add + " " + days[i] + "Close" + close;
				}
			}catch (Exception e) {}
		}
		//System.out.println("--------------------\n" + ret);
		return(ret);
	}

	private String processAttributes(String field, DBObject cursor) {
		String ret="";
		try{
			Set<String> cursorKeys=cursor.keySet();
			for(String s:cursorKeys) {
				try {
					DBObject subCursor=(DBObject)cursor.get(s);
					Set<String> sbuCursorKeys=subCursor.keySet();
					for(String s1:sbuCursorKeys) {
						ret = ret + " " + s1;
					}
				}catch(ClassCastException e) {
					
					ret = ret + " " + s; // + lerolero;
					//System.out.println("---" + cursor.get(s).toString());
				}
			}
		}catch (Exception e) {
			System.out.println(e);
		}
		//System.out.println("--------------------\n" + ret);
		return(ret);
	}
}
