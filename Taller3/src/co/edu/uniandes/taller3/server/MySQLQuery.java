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
	public static void main(String[] args) {
		
	}

	public List<String> getRatingsUser(String userId, String dateInicial, String dateFinal  ) {
		
		List<String> ret=new ArrayList<String>();
		
		Connection conn = null;
		Statement stmt = null;
		try{
			//STEP 2: Register JDBC driver
			Class.forName(MySQLConstant.JDBC_DRIVER).newInstance();

			//STEP 3: Open a connection
			conn = (Connection) DriverManager.getConnection(MySQLConstant.DB_URL,MySQLConstant.USER,MySQLConstant.PASS);

			//STEP 4: Execute a query
			stmt = (Statement) conn.createStatement();
			String sql = "select movieId "
					+ "from rating "
					+ "where userId = " + userId 
					+ " and timestamp >= UNIX_TIMESTAMP('" + dateInicial + "')"
					+ " and timestamp <= UNIX_TIMESTAMP('" + dateFinal + "')"
					+ " and rating >= 4";
					
				
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
	
	public List<String> getRatingsMovieEval(String movieId) {
		
		List<String> ret=new ArrayList<String>();
		
		Connection conn = null;
		Statement stmt = null;
		try{
			//STEP 2: Register JDBC driver
			Class.forName(MySQLConstant.JDBC_DRIVER).newInstance();

			//STEP 3: Open a connection
			conn = (Connection) DriverManager.getConnection(MySQLConstant.DB_URL,MySQLConstant.USER,MySQLConstant.PASS);

			//STEP 4: Execute a query
			stmt = (Statement) conn.createStatement();
			String sql = "select userId "
					+ "from rating "
					+ "where movieId = " + movieId + " limit 0,5"; 

			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()) {
				ret.add(rs.getString("userId"));
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
	
	public List<String> getRatingsUserEval(String userId) {
		
		List<String> ret=new ArrayList<String>();
		
		Connection conn = null;
		Statement stmt = null;
		try{
			//STEP 2: Register JDBC driver
			Class.forName(MySQLConstant.JDBC_DRIVER).newInstance();

			//STEP 3: Open a connection
			conn = (Connection) DriverManager.getConnection(MySQLConstant.DB_URL,MySQLConstant.USER,MySQLConstant.PASS);

			//STEP 4: Execute a query
			stmt = (Statement) conn.createStatement();
			String sql = "select movieId "
					+ "from rating "
					+ "where userId = " + userId;
			
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
	
}//end FirstExample


