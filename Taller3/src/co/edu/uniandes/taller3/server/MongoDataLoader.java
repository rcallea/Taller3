/** DJ **/
package co.edu.uniandes.taller3.server;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

import org.recommender101.data.DataModel;
import org.recommender101.data.Rating;
import org.recommender101.gui.annotations.R101Class;
import org.recommender101.gui.annotations.R101Setting;
import org.recommender101.gui.annotations.R101Setting.SettingsType;
import org.recommender101.tools.Debug;
import org.recommender101.tools.Utilities101;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

/**
 * A default data loader capable of loading movielens files
 * Format: user<tab>item<tab>rating<tab>timestamp
 * @author DJ
 *
 */
@R101Class (name="Default Data Loader", description="A default data loader capable of loading movielens files.")
public class MongoDataLoader  {

	// A default location
	protected String filename = "recommenderBusiness";
	protected int minNumberOfRatingsPerUser = -1;
	protected int sampleNUsers = -1;
	protected double density = 1.0;
	protected Hashtable<Integer,String> user = new Hashtable<Integer,String>();
	protected Hashtable<Integer,String> business = new Hashtable<Integer,String>();
	
	public static int maxLines = -1;

	// Should we transform the data 
	// 0 no
	// > 0: This is the threshold above which items are relevant 
	public int binarizeLevel = 0;
	
	// Should we remove 0 -valued ratings?
	public boolean useUnaryRatings = false;
	
	/**
	 * An empty constructor
	 */
	public MongoDataLoader() {
	}
	
	
	public void applyConstraints(DataModel dm) throws Exception{
		// Apply sampling procedure
		if (this.sampleNUsers > -1) {
			dm = Utilities101.sampleNUsers(dm, sampleNUsers);
			dm.recalculateUserAverages();
		}
		
		// Apply min number of ratings constraint
		if (minNumberOfRatingsPerUser > 0) {
			Utilities101.applyMinRatingsPerUserConstraint(dm, minNumberOfRatingsPerUser);
			dm.recalculateUserAverages();
		}
		
		// Apply sampling of data to vary density
		if (this.density < 1.0) {
			dm = Utilities101.applyDensityConstraint(dm, this.density);
			dm.recalculateUserAverages();
		}
		
		if (this.binarizeLevel > 0) {
			Debug.log("Binarizing at level: " + this.binarizeLevel);
			this.binarize(dm);
		}
	}
		
	// =====================================================================================

	/**
	 * The method loads the MovieLens data from the specified file location.
	 * The method can be overwritten in a subclass
	 */
	public void loadData(DataModel dm) throws Exception {
		Hashtable<String, Integer> userTemp=new Hashtable<String, Integer>();
		Hashtable<String, Integer> businessTemp=new Hashtable<String, Integer>();
		int ut=1;
		int bt=1;
		MongoClient mongoClient = new MongoClient("localhost");
		DB db = mongoClient.getDB("recommenderBusiness");
		DBCollection coll = db.getCollection("review");
		BasicDBObject allQuery = new BasicDBObject();
		BasicDBObject fields = new BasicDBObject();
		fields.put("user_id", 1);
		fields.put("business_id", 2);
		fields.put("stars", 3);
		DBCursor cursor = coll.find(allQuery, fields);
		int counter=0;

		try {
			System.out.println("Usuario\tNegocio\tCalif");
			while(cursor.hasNext()) {
				DBObject singleField=(DBObject) cursor.next();
				try {
					System.out.println(singleField.get("user_id").toString());
					String user=singleField.get("user_id").toString();
					String business=singleField.get("business_id").toString();
					int stars=new Double(singleField.get("stars").toString()).intValue();
					
					if(userTemp.get(user)==null) {
						userTemp.put(user, ut);
						this.user.put(ut, user);
						ut++;
					}
					
					if(businessTemp.get(business)==null) {
						businessTemp.put(business, bt);
						this.business.put(bt, business);
						bt++;
					}
	
					dm.addRating(userTemp.get(user), businessTemp.get(business), stars);
					counter++;
				} catch (NullPointerException e) {}
		   }
		} finally {
		   cursor.close();
		}

		Debug.log("DefaultDataLoader:loadData: Loaded " + counter + " ratings");
		Debug.log("DefaultDataLoader:loadData: " + dm.getUsers().size() + " users and " + dm.getItems().size() + " items.");
		mongoClient.close();
		applyConstraints(dm);
	}
	
	// =====================================================================================

	
	/**
	 * Sets the file name
	 * @param name
	 */
	@R101Setting(displayName="Filename", description="Sets the filename of the dataset", 
			type=SettingsType.FILE)
	public void setFilename(String name) {
		filename = name;
	}

	// =====================================================================================

	/**
	 *  Returns the filename 
	 * 
	 */
	public String getFilename() {
		return filename;
	}
	// =====================================================================================

	/**
	 * To be used by the class instantiator. Defines the minimum number of ratings a user 
	 * must have to remain in the dataset
	 * @param n the min number of ratings per user
	 */
	@R101Setting(displayName="Minimum number of ratings per user",
			description="Defines the minimum number of ratings a user must have to remain in the dataset",
			defaultValue="-1", type=SettingsType.INTEGER, minValue=-1, maxValue=Integer.MAX_VALUE)
	public void setMinNumberOfRatingsPerUser(String n) {
		minNumberOfRatingsPerUser = Integer.parseInt(n);
	}
	
	
	/**
	 * Instructs the load to sample a given number of users
	 * @param n how many users to keep 
	 */
	@R101Setting(defaultValue="-1", displayName="Sample N users", 
			description="Instructs the loader to sample a given number of users", type=SettingsType.INTEGER,
			minValue=-1, maxValue=Integer.MAX_VALUE)
	public void setSampleNUsers(String n) {
		this.sampleNUsers = Integer.parseInt(n);
	}
	
	/**
	 * Set the density
	 * @param d
	 */
	@R101Setting(displayName="Density", description="Sets the density", type=SettingsType.DOUBLE,
			minValue=0.0, maxValue=1.0, defaultValue="1.0")
	public void setDensity(String d) {
		this.density = Double.parseDouble(d);
	}
	
	/**
	 * Set the binarization method
	 * @param b
	 */
	@R101Setting(defaultValue="0", type=SettingsType.INTEGER, description="Sets the binarization method",
			displayName="Binarize Level", minValue=0, maxValue=Integer.MAX_VALUE)
	public void setBinarizeLevel(String b) {
		this.binarizeLevel = Integer.parseInt(b);
	}
	
	/**
	 * Binarizes the data model after loading
	 * @throws Exception
	 */
	public void binarize(DataModel dm) throws Exception {
		
		Set<Rating> ratingsCopy = new HashSet<Rating>(dm.getRatings());
		
		// Go through the ratings
		for (Rating r : ratingsCopy) {
			// Option one - every rating is relevant
			
			if (r.rating >= this.binarizeLevel) {
				r.rating = 1;
			}
			else {
				// Remove rating in case we only have positive feedback
				if (this.useUnaryRatings) {
					dm.removeRating(r);
				}
				// Otherwise, set it to 0
				else {
					r.rating = 0;
				}
			}
		}
		// Recalculate things
		dm.recalculateUserAverages();
//		System.out.println("Binarization done (" + dm.getRatings().size() + " ratings)");
	}
	
	/**
	 * Sould we use unary ratings? (If yes, we delete all 0 ratings)
	 * @param b
	 */
	@R101Setting(description="All 0 ratings will be deleted", type=SettingsType.BOOLEAN,
			defaultValue="false", displayName="Unary Ratings")
	public void setUnaryRatings(String b) {
		this.useUnaryRatings = Boolean.parseBoolean(b);
	}

}
