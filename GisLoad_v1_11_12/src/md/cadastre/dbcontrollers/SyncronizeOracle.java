package md.cadastre.dbcontrollers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;

import org.slf4j.LoggerFactory;

import md.cadastre.dbconnection.Database;
import md.cadastre.settingsparam.DbUtilities;

public class SyncronizeOracle {
	
	static final org.slf4j.Logger logger = LoggerFactory.getLogger(SyncronizeOracle.class);
	
	private BufferedReader br = null;
    private FileReader fr = null;
    private BufferedWriter bw = null;
    private FileWriter fw = null;
    private static PreparedStatement stmt = null;
	private static ResultSet rs = null;
	private static Connection connection = null;
    
    public boolean terenuri(String cadzone, Metadata m) {
    	boolean result = false;
    	try {
    		connection = Database.getConnectionPG();
    		Date date = new Date();
    		System.out.println(cadzone + " " + new Timestamp(date.getTime()));
    		String sql = "select t.codcadastral, t.codtip, t.codstr, t.nrcasa, t.codnrremarc, t.codtipregistr,\n"
                    + "t.codcolect, t.suprafata, t.codadm, t.datmodif, t.timp, t.topo_status,\n"
                    + "ST_AsText(t.wkb_geometry) as wkt  from gisload.terenuri" + cadzone + " t";
    		stmt = connection.prepareStatement(sql);
    		rs = stmt.executeQuery();
    		
    	}catch(SQLException ex) {
            logger.error(ex.getMessage() + ";" + Arrays.toString(ex.getStackTrace()));
            logger.error(DbUtilities.printSQLException(ex));
        } finally {
        	try {
				Database.closeConnections(stmt, rs, connection);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	}
    	return result;
    }

	public boolean deleteCadzone(String cadzone, String terenuriTableOracle, Metadata m) {
		// TODO Auto-generated method stub
		return false;
	}

}
