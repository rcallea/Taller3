/** DJ **/
package co.edu.uniandes.taller3.server;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

/**
 * A default data loader capable of loading movielens files
 * Format: user<tab>item<tab>rating<tab>timestamp
 * @author DJ
 *
 */
@R101Class (name="Default Data Loader", description="A default data loader capable of loading movielens files.")
public class DefaultDataLoader  {

	// A default location
	protected String fromDate="2015/03/01";
	protected String toDate="2015/03/31";
	protected int minNumberOfRatingsPerUser = -1;
	protected int sampleNUsers = -1;
	protected double density = 1.0;
	
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
	public DefaultDataLoader() {
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
		// Read the file line by line and add the ratings to the data model.
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
				String sql = "SELECT userId, movieId, rating FROM rating WHERE timestamp>=UNIX_TIMESTAMP('" + this.fromDate + " 00:00:00') AND timestamp<=UNIX_TIMESTAMP('" + this.toDate + " 23:59:00') LIMIT " + limit + "," + sizeData + ";";
				System.out.println(sql);
				ResultSet rs = stmt.executeQuery(sql);
				//System.out.println("Cargando desde " + limit);
				limit=limit+sizeData+1;
				moreRecords=0;
				while(rs.next()) {
					dm.addRating(rs.getInt("userId"), rs.getInt("movieId"), (int)rs.getFloat("rating"));
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
		applyConstraints(dm);
	}
	
	// =====================================================================================

	/**
	 * The method loads the MovieLens data from the specified file location.
	 * The method can be overwritten in a subclass
	 */
	public void loadVerifyData1(DataModel dm, String theUser) throws Exception {
		int counter = 0;
		
		
		// Read the file line by line and add the ratings to the data model.
//		BufferedReader reader = new BufferedReader(new FileReader(filename));
//		String line;
//		line = reader.readLine();
//		String[] tokens;
//		while (line != null) {
//			// Skip comment lines
//			if (line.trim().startsWith("//")) {
//				line = reader.readLine();
//				continue;
//			}
//			tokens = line.split("\t");
//			int userid=Integer.parseInt(tokens[0]);
//			int busid=Integer.parseInt(tokens[1]);
//			String useridText=tokens[2];
//			String busidText=tokens[3];
//			int stars=Integer.parseInt(tokens[4]);
//			// First, add the ratings.
//			if(useridText.equalsIgnoreCase(theUser)) {
//				dm.addRating(userid, busid, stars);
//	
//				if(user.get(userid)==null) {
//					this.user.put(userid, useridText);
//					this.userId.put(useridText,userid);
//				}
//				
//				if(business.get(busid)==null) {
//					this.business.put(busid, busidText);
//					this.businessId.put(busidText, busid);
//				}
//			}
//			line = reader.readLine();
////			System.out.println("line.." + line + " (" + counter + ")");
//			counter++;
////			// debugging here..
//			if (maxLines != -1) {
//				if (counter >= maxLines) {
//					System.out.println("DataLoader: Stopping after " + (counter)  + " lines for debug");
//					break;
//				}
//			}
//		}
		Debug.log("DefaultDataLoader:loadData: Loaded " + counter + " ratings");
		Debug.log("DefaultDataLoader:loadData: " + dm.getUsers().size() + " users and " + dm.getItems().size() + " items.");
//		reader.close();
		applyConstraints(dm);
	}
	
	public String[] getMovieInfo(String[] movie) {
		String[] ret=new String[movie.length];
		
		if(movie.length>0) {
			String movieToSearch=movie[0];
			for(int i=1;i<movie.length;i++) {
				movieToSearch=movieToSearch + ", " + movie[i];
			}
			
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
				String sql = "SELECT movieId, title, genres FROM movie WHERE movieId IN (" + movieToSearch + ");";
				System.out.println(sql);
				ResultSet rs = stmt.executeQuery(sql);
				while(rs.next()) {
					String currentMovie=rs.getString("movieId");
					String currentTitle=rs.getString("title");
					String currentGenres=rs.getString("genres");
					for(int i=0;i<movie.length;i++) {
						if(movie[i].equals(currentMovie)) {
							ret[i]=currentTitle + "-----" + currentGenres;
							i=movie.length;
						}
					}
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
			
		}

		return(ret);
	}

	public String[] getNextMovies(int user) {
		String[] ret=new String[0];
		ArrayList<String> retTemp=new ArrayList<String>();
		
		Connection conn = null;
		Statement stmt = null;
		try{
			//STEP 2: Register JDBC driver
			Class.forName(MySQLConstant.JDBC_DRIVER).newInstance();

			//STEP 3: Open a connection
			conn = (Connection) DriverManager.getConnection(MySQLConstant.DB_URL,MySQLConstant.USER,MySQLConstant.PASS);

			//STEP 4: Execute a query
			stmt = (Statement) conn.createStatement();
			String sql = "SELECT userId, movieId, rating FROM rating WHERE (timestamp<UNIX_TIMESTAMP('" + this.fromDate + " 00:00:00') OR timestamp>UNIX_TIMESTAMP('" + this.toDate + " 23:59:00')) AND userId= " + user + ";";
			System.out.println(sql);
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()) {
				retTemp.add(rs.getString("movieId"));
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
		
		ret=new String[retTemp.size()];
		for(int i=0;i<retTemp.size();i++) {
			ret[i]=retTemp.get(i);
		}
		return(ret);
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


	/**
	 * @return the fromDate
	 */
	public String getFromDate() {
		return fromDate;
	}


	/**
	 * @param fromDate the fromDate to set
	 */
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}


	/**
	 * @return the toDate
	 */
	public String getToDate() {
		return toDate;
	}


	/**
	 * @param toDate the toDate to set
	 */
	public void setToDate(String toDate) {
		this.toDate = toDate;
	}



}
