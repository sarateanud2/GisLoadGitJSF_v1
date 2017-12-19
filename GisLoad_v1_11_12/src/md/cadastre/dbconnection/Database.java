package md.cadastre.dbconnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class Database {
	
	private static Connection conn;
    private static InitialContext ic;
    private static DataSource ds;

    public static Connection getConnectionPG() {
        try {
        	
            ic = new InitialContext();
            ds = (DataSource) ic.lookup("java:comp/env/jdbc/gisdatapg");
            conn = ds.getConnection();
            
        } catch (SQLException ex) {
           System.out.println("SQLException " + ex);
        } catch (NamingException ex) {
           System.out.println("NamingException " + ex); 
        }
        
        return conn;
    }
    
    public static Connection getConnectionOracle() {
        try {
        	
            ic = new InitialContext();
            ds = (DataSource) ic.lookup("java:comp/env/jdbc/gisdataorcl");
            conn = ds.getConnection();
            
        } catch (SQLException ex) {
           System.out.println("SQLException " + ex);
        } catch (NamingException ex) {
           System.out.println("NamingException " + ex); 
        }
        
        return conn;
    }
    
    public static void closeConnections(PreparedStatement stmt, ResultSet rs, Connection connection) throws SQLException {
    	
		if (stmt != null) {
			stmt.close();
		}
        if (rs != null) {
        	rs.close();
        }
        if (conn != null) {
        	conn.close();
        }
        
    }
    
}