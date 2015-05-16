package co.edu.uniandes.taller3.shared;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

public class SparqlExample {
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	static final String DB_URL = "jdbc:mysql://localhost:3306/ml";
	static final String USER = "usml";
	static final String PASS = "12345678";

	public String getMovieInfo(String movieName) {
    	String ret="";
        String s2 = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n" +
                "PREFIX dbpedia-owl: <http://dbpedia.org/ontology/> \n" +
                "PREFIX dbpprop: <http://dbpedia.org/property/>\n" +
                "SELECT ?film ?film_name ?abstract ?director ?starring ?comment \n" +
                "WHERE \n" +
                "  { ?film rdf:type <http://dbpedia.org/ontology/Film>." +
                "    ?film foaf:name ?film_name. \n" +
                "    ?film dbpedia-owl:director ?director. \n" +
                "    ?film rdfs:comment ?comment. \n" +
                "    ?film dbpprop:starring ?starring. \n" +
                "    ?film dbpedia-owl:abstract ?abstract \n" +
                "    FILTER (LANG(?abstract)='en' && LANG(?comment)='en' && ?film_name=\"" + movieName + "\"@en) \n" +
                "  }\n" +
                "LIMIT 20\n" +
                "";
        try {
            Query query = QueryFactory.create(s2); //s2 = the query above
            QueryExecution qExe = QueryExecutionFactory.sparqlService( "http://dbpedia.org/sparql", query );
            ResultSet results = qExe.execSelect();

            while (results.hasNext()) {
                QuerySolution row= results.next();
                String director=row.get("director").toString();
                String starring=row.get("starring").toString();
                String comment=row.get("comment").toString();
                String abstractInfo=row.get("abstract").toString();
                
                if(!ret.contains(director)) {
                	ret=ret + " " + director;
                }

                if(!ret.contains(starring)) {
                	ret=ret + " " + starring;
                }

                //Se adiciona primero que el comment porque comment a veces está contenido en abstract
                if(!ret.contains(abstractInfo)) {
                	ret=ret + " " + abstractInfo;
                }

                if(!ret.contains(comment)) {
                	ret=ret + " " + comment;
                }
                //System.out.println(row.toString());
            }

        }catch (Exception e) {
        	e.printStackTrace();
        }
        
        //ResultSetFormatter.out(System.out, results, query) ;
//        System.out.println(ResultSetFormatter.asText(results));
        System.out.println(ret);
        return(ret);
    }
	
	
	public void loadData() {
		Connection conn = null;
		Statement stmt = null;
		PreparedStatement stmtUp = null;
		int times=0;
		int milis=2000;
		try{
			Class.forName(JDBC_DRIVER).newInstance();
			conn = (Connection) DriverManager.getConnection(DB_URL,USER,PASS);
			//System.out.println("Creating statement...");
			stmt = (Statement) conn.createStatement();
			String sql;
			sql = "SELECT movieId, title FROM movie LIMIT 7481,20000;";
			stmt.executeQuery(sql);
			
			java.sql.ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()){
				String info="";
				int id=rs.getInt("movieId");
				String title=rs.getString("title");
				String[] titleSeparated=title.split("\\(");
				if(titleSeparated.length>1) {
					title=titleSeparated[0].trim();
				}

				titleSeparated=title.split(",");
				if(titleSeparated.length>1) {
					title=titleSeparated[1].trim() + " " + titleSeparated[0].trim();
				}
				
				if(times==200) {
					try {
					    Thread.sleep(milis);
					} catch(InterruptedException ex) {
					    Thread.currentThread().interrupt();
					}
				}
				
				info=this.getMovieInfo(title);
				if(!info.equals("")) {
					String sqlUp;
					sqlUp = "UPDATE movie SET info=? WHERE movieId=?;";
					stmtUp = (PreparedStatement) conn.prepareStatement(sqlUp);
					stmtUp.setString(1, info);
					stmtUp.setInt(2, id);
					stmtUp.executeUpdate();
					stmtUp.close();
				}
			}
			rs.close();
			stmt.close();
			conn.close();
		}catch(SQLException se){
			se.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				if(stmt!=null)
					stmt.close();
			}catch(SQLException se2){}// nothing we can do
			try{
				if(conn!=null)
					conn.close();
			}catch(SQLException se){
				se.printStackTrace();
			}//end finally try
		}//end try
//		System.out.println("Goodbye!");
	}//end main

	public static void main(String[] args) {
		new SparqlExample().loadData();
	}
}