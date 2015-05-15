package co.edu.uniandes.taller3.server;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.recommender101.data.DataModel;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

//STEP 1. Import required packages

public class MySQLQuery {

	/**
	 * Tries to save the user data in database
	 * @param data
	 * @return true if data could be st	ored or updated
	 */
	
	public boolean saveUser(String data) {
		boolean ret = false;
		Connection conn = null;
		Statement stmt = null;
		try{
			//STEP 2: Register JDBC driver
			Class.forName(MySQLConstant.JDBC_DRIVER).newInstance();

			//STEP 3: Open a connection
			conn = (Connection) DriverManager.getConnection(MySQLConstant.DB_URL,MySQLConstant.USER,MySQLConstant.PASS);

			//STEP 4: Execute a query
			stmt = (Statement) conn.createStatement();
			String sqlcheck, sql;
			String[] input = data.split("\\|");
			sqlcheck = "SELECT * FROM user WHERE userId=" + input[0] + ";";
			ResultSet rs = stmt.executeQuery(sqlcheck);
			
			if(rs.next()) { 
				sql="UPDATE user SET name='" + input[1] + "', twitterId='" + input[2]
						+ "', email='" + input[3] + "' WHERE userId=" + input[0] + ";";
			}
			else {
				sql="INSERT INTO user (userId, name, twitterId, email) VALUES (" + input[0]
						+ ", '" + input[1] + "', '" + input[2] + "', '" + input[3] + "');";
			}
			stmt.executeUpdate(sql);
			ret=true;
			
//			}
		//STEP 6: Clean-up environment
			rs.close();
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
//		System.out.println("Goodbye!");
		return ret;
	}//end main
	
	public String[] getUserList() {
		String[] ret = {};
		Connection conn = null;
		Statement stmt = null;
		ArrayList<String> temporalElement=new ArrayList<String>();
		try{
			//STEP 2: Register JDBC driver
			Class.forName(MySQLConstant.JDBC_DRIVER).newInstance();

			//STEP 3: Open a connection
			conn = (Connection) DriverManager.getConnection(MySQLConstant.DB_URL,MySQLConstant.USER,MySQLConstant.PASS);

			//STEP 4: Execute a query
			stmt = (Statement) conn.createStatement();
			String sql = "SELECT userId FROM user WHERE userId>34999 ORDER BY userId;";
			ResultSet rs = stmt.executeQuery(sql);
			
			while(rs.next()) {
				temporalElement.add(rs.getString("userId"));
			}
		//STEP 6: Clean-up environment
			rs.close();
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
		
		ret=new String[temporalElement.size()];
		
		for(int i=0;i<temporalElement.size();i++) {
			ret[i]=temporalElement.get(i);
		}

		return ret;
	}
	
	public String[] getMovieList(String movie) {
		String[] ret = {};
		Connection conn = null;
		Statement stmt = null;
		ArrayList<String> temporalElement=new ArrayList<String>();
		try{
			//STEP 2: Register JDBC driver
			Class.forName(MySQLConstant.JDBC_DRIVER).newInstance();

			//STEP 3: Open a connection
			conn = (Connection) DriverManager.getConnection(MySQLConstant.DB_URL,MySQLConstant.USER,MySQLConstant.PASS);

			//STEP 4: Execute a query
			stmt = (Statement) conn.createStatement();
			String sql = "SELECT movieId, tittle FROM movie WHERE tittle LIKE '%" + movie + "%'ORDER BY movieId;";
			ResultSet rs = stmt.executeQuery(sql);
			
			while(rs.next()) {
				temporalElement.add(rs.getString("movieId") + "|" + rs.getString("tittle"));
			}
		//STEP 6: Clean-up environment
			rs.close();
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
		
		ret=new String[temporalElement.size()];
		
		for(int i=0;i<temporalElement.size();i++) {
			ret[i]=temporalElement.get(i);
		}

		return ret;
	}
	
	public boolean saveMovie(String data) {
		boolean ret = false;
		Connection conn = null;
		Statement stmt = null;
		try{
			//STEP 2: Register JDBC driver
			Class.forName(MySQLConstant.JDBC_DRIVER).newInstance();

			//STEP 3: Open a connection
			conn = (Connection) DriverManager.getConnection(MySQLConstant.DB_URL,MySQLConstant.USER,MySQLConstant.PASS);

			//STEP 4: Execute a query
			stmt = (Statement) conn.createStatement();
			String sqlcheck, sql;
			String[] input = data.split("\\|");
			sqlcheck = "SELECT * FROM rating WHERE userId=" + input[0] + " AND movieId='" + input[1] + "';";
			ResultSet rs = stmt.executeQuery(sqlcheck);
			
			if(rs.next()) { 
				sql="UPDATE rating SET rating=" + input[2] + " WHERE userId=" + input[0] + " AND movieId=" + input[1] + ";";
			}
			else {
				sql="INSERT INTO rating (userId, movieId, rating, ratingTimeStamp) VALUES (" + input[0]
						+ ", '" + input[1] + "', " + input[2] + ", 0);";
			}
			stmt.executeUpdate(sql);
			ret=true;
			
//			}
		//STEP 6: Clean-up environment
			rs.close();
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
//		System.out.println("Goodbye!");
		return ret;
	}//end main

	public static void main(String[] args) {
		new MySQLQuery().saveUser("35000|a|b|c");
	}

	public String[] getRatings(int lower, int number) {
		String[] ret = {};
		Connection conn = null;
		Statement stmt = null;
		ArrayList<String> temporalElement=new ArrayList<String>();
		try{
			//STEP 2: Register JDBC driver
			Class.forName(MySQLConstant.JDBC_DRIVER).newInstance();

			//STEP 3: Open a connection
			conn = (Connection) DriverManager.getConnection(MySQLConstant.DB_URL,MySQLConstant.USER,MySQLConstant.PASS);

			//STEP 4: Execute a query
			stmt = (Statement) conn.createStatement();
			String sql = "SELECT DISTINCT userId, rating.movieId, rating, tittle FROM rating INNER JOIN movie ON rating.movieId=movie.movieId WHERE userId<34999 LIMIT " + lower + ", " + number;
			ResultSet rs = stmt.executeQuery(sql);
			
			int i=0;
			while(rs.next()) {
					temporalElement.add(rs.getString("userId") + "|" +rs.getString("movieId") + "|" +rs.getString("rating") + "|" + i++);
			}
		//STEP 6: Clean-up environment
			rs.close();
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
		
		ret=new String[temporalElement.size()];
		
		for(int i=0;i<temporalElement.size();i++) {
			ret[i]=temporalElement.get(i);
		}

		return ret;
	}

	public DataModel getRatings(int items) {
		DataModel dm=new DataModel();
		Connection conn = null;
		Statement stmt = null;
		try{
			//STEP 2: Register JDBC driver
			Class.forName(MySQLConstant.JDBC_DRIVER).newInstance();

			//STEP 3: Open a connection
			conn = (Connection) DriverManager.getConnection(MySQLConstant.DB_URL,MySQLConstant.USER,MySQLConstant.PASS);

			//STEP 4: Execute a query
			stmt = (Statement) conn.createStatement();
			String sql = "SELECT userId, movieId, rating FROM rating LIMIT " + items + " UNION SELECT userId, movieId, rating FROM rating WHERE userId>34999;";
			ResultSet rs = stmt.executeQuery(sql);
			
			int i=0;
			while(rs.next()) {
				int user=0, item=0, value=0;
				try {
					user=Integer.parseInt(rs.getString("userId"));
					item=Integer.parseInt(rs.getString("movieId"));
					value=Integer.parseInt(rs.getString("rating"));
					
				} catch (NumberFormatException e) {
					
				}
				dm.addRating(user, item, value);
			}
		//STEP 6: Clean-up environment
			rs.close();
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
		
		return dm;
	}
	
	public DataModel getRatingsInverse(int items) {
		DataModel dm=new DataModel();
		Connection conn = null;
		Statement stmt = null;
		try{
			//STEP 2: Register JDBC driver
			Class.forName(MySQLConstant.JDBC_DRIVER).newInstance();

			//STEP 3: Open a connection
			conn = (Connection) DriverManager.getConnection(MySQLConstant.DB_URL,MySQLConstant.USER,MySQLConstant.PASS);

			//STEP 4: Execute a query
			stmt = (Statement) conn.createStatement();
			String sql = "SELECT userId, movieId, rating FROM rating LIMIT " + items + " UNION SELECT userId, movieId, rating FROM rating WHERE userId>34999;";
			ResultSet rs = stmt.executeQuery(sql);
			
			int i=0;
			while(rs.next()) {
				int user=0, item=0, value=0;
				try {
					user=Integer.parseInt(rs.getString("userId"));
					item=Integer.parseInt(rs.getString("movieId"));
					value=Integer.parseInt(rs.getString("rating"));
					
				} catch (NumberFormatException e) {
					
				}
				dm.addRating(user, value, item);
			}
		//STEP 6: Clean-up environment
			rs.close();
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
		
		return dm;
	}
	
	
	public List<String> getMovieName(List<String> movieCodes) {
		ArrayList<String> movies=new ArrayList<String>();
		Connection conn = null;
		Statement stmt = null;
		try{
			//STEP 2: Register JDBC driver
			Class.forName(MySQLConstant.JDBC_DRIVER).newInstance();

			//STEP 3: Open a connection
			conn = (Connection) DriverManager.getConnection(MySQLConstant.DB_URL,MySQLConstant.USER,MySQLConstant.PASS);

			//STEP 4: Execute a query
			stmt = (Statement) conn.createStatement();
			
			ResultSet rs = stmt.executeQuery("show tables;");
			for(int i=0;i<movieCodes.size();i++) {
				String[] values=movieCodes.get(i).split("\\|");
				String sql = "SELECT tittle FROM movie WHERE movieId='" + this.repeat("0", 7-values[0].length()) + values[0] + "';";
				rs = stmt.executeQuery(sql);
				while(rs.next()) {
					movies.add(rs.getString("tittle") + "|" + values[1]);
				}
			}
			rs.close();
//			}
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
//		System.out.println("Goodbye!");
		return(movies);
	}

	public List<String> getOtherRated(int limit, int user, int rows) {
		ArrayList<String> movies=new ArrayList<String>();
		Connection conn = null;
		Statement stmt = null;
		try{
			//STEP 2: Register JDBC driver
			Class.forName(MySQLConstant.JDBC_DRIVER).newInstance();

			//STEP 3: Open a connection
			conn = (Connection) DriverManager.getConnection(MySQLConstant.DB_URL,MySQLConstant.USER,MySQLConstant.PASS);

			//STEP 4: Execute a query
			stmt = (Statement) conn.createStatement();
			
			String sql = "SELECT movie.movieId, tittle, rating FROM "
					+ " (SELECT ratingId, userId, movieId, rating FROM rating LIMIT " + limit +"," + Integer.MAX_VALUE + ") AS subrating"
					+ " INNER JOIN movie ON subrating.movieId=movie.movieId WHERE userId= " + user + " ORDER BY rating LIMIT " + rows + ";";
			System.out.println(sql);
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()) {
				movies.add(rs.getString("movieId") + "|" + rs.getString("tittle") + "|" + rs.getString("rating"));
			}
			rs.close();
//			}
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
//		System.out.println("Goodbye!");
		return(movies);
	}
	
	public String repeat(String value, int size) {
		String ret="";
		for(int i=0;i<size;i++) {
			ret=ret+value;
		}
		return(ret);
	}
}//end FirstExample


