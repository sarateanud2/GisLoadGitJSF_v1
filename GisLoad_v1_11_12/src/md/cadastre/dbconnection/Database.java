package md.cadastre.dbconnection;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class Database {
	
	private static Connection conn;
    private static InitialContext ic;
    private static DataSource ds;

    public static Connection getConnection() {
        try {
            ic = new InitialContext();
            ds = (DataSource) ic.lookup("java:comp/env/jdbc/ascop_v20_11");
            conn = ds.getConnection();
        } catch (SQLException ex) {
           System.out.println("SQLException " + ex);
        } catch (NamingException ex) {
           System.out.println("NamingException " + ex); 
        }

        return conn;
    }
}
