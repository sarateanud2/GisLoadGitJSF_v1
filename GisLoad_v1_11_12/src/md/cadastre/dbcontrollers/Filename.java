package md.cadastre.dbcontrollers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import md.cadastre.dbconnection.Database;
import md.cadastre.settingsparam.DbUtilities;

public class Filename {
	static final Logger logger = LoggerFactory.getLogger(Filename.class);
	
	private static PreparedStatement stmt = null;
	private static ResultSet rs = null;
	private static Connection connection = null;
	
	public static String getName(int id) {
		String result = "-";
        try {
        	connection = Database.getConnectionPG();
        	stmt = connection.prepareStatement("select * from gisadm.t_filenames_upload "
                    + " where id = ?");
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            while (rs.next()) {
                result = rs.getString("name");
            }
        } catch (SQLException ex) {
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
	
	public static ArrayList<String> getFileList(String type) {
		ArrayList<String> fileList = new ArrayList<>();
		try {
        	connection = Database.getConnectionPG();
        	logger.debug("Connection opened 18");
        	stmt = connection.prepareStatement("select * from gisadm.t_filenames_upload "
        										+ " where in_use = 'Y'");
        	
        	rs = stmt.executeQuery();
            while (rs.next()) {
            	fileList.add(rs.getString(type));
            }
        } catch (SQLException ex) {
            logger.error(ex.getMessage() + ";" + Arrays.toString(ex.getStackTrace()));
            logger.error(DbUtilities.printSQLException(ex));
        } catch (Exception ex) {
            logger.error(ex.getMessage() + ";" + Arrays.toString(ex.getStackTrace()));
        } finally {
        	try {
				Database.closeConnections(stmt, rs, connection);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
		
		return fileList;
	}
}
