package co.edu.uniandes.taller3.server;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

//STEP 1. Import required packages

public class MySQLConstant {
	// JDBC driver name and database URL
	public static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	public static final String DB_URL = "jdbc:mysql://localhost:3306/ml";

	//  Database credentials
	public static final String USER = "usml";
	public static final String PASS = "12345678";
}