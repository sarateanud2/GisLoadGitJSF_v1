package md.cadastre.dbcontrollers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import md.cadastre.dbconnection.Database;
import md.cadastre.settingsparam.DbUtilities;

public class LogDb {
	static final Logger logger = LoggerFactory.getLogger(LogDb.class);
	
	private static PreparedStatement stmt = null;
	private static ResultSet rs = null;
	private static Connection connection = null;
	
	public static String getLog(int loadid) {
		String result = "";
		try {
			connection = Database.getConnectionPG();
			stmt = connection.prepareStatement("select * from gisadm.t_log "
                    + " where loadid = ? order by 1");
            stmt.setInt(1, loadid);
			
			
			while (rs.next()) {
                result = result + "<tr><td>" + rs.getString("navi_date");
                result = result + "</td><td>" + rs.getString("cadzone");
                if ("1".equals(rs.getString("filetypeid"))) {
                    result = result + " terenuri";
                } else if ("2".equals(rs.getString("filetypeid"))) {
                    result = result + " cladiri";
                }
                result = result + "</td><td>" + rs.getString("result");
                result = result + "</td><td>" + rs.getString("message") + "</td></tr>";
            }
            result = "<table>" + result + "</table>";
			
			
		} catch (SQLException ex) {
            logger.error(ex.getMessage() + "|" + Arrays.toString(ex.getStackTrace()));
            logger.error(DbUtilities.printSQLException(ex));
        } finally {
        	try {
				Database.closeConnections(stmt, rs, connection);
			} catch (SQLException e) {
				e.printStackTrace();
			}
        }
		
		return result;
	}
	
	public static void setLog(int loadid, String cadzone, int filetypeid, int result, String message) {
		int id = 0;
		try {
			connection = Database.getConnectionPG();
			String sql = "INSERT INTO gisadm.t_log (loadid, cadzone, filetypeid, result, message) values (?,?,?,?,?)";
            stmt = connection.prepareStatement(sql, new String[]{"id"});
            stmt.setInt(1, loadid);
            stmt.setString(2, cadzone);
            stmt.setInt(3, filetypeid);
            stmt.setInt(4, result);
            stmt.setString(5, message);
            stmt.executeUpdate();
            rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                id = rs.getInt(1);
            }
		} catch (SQLException ex) {
            logger.error(ex.getMessage() + "|" + Arrays.toString(ex.getStackTrace()));
            logger.error(DbUtilities.printSQLException(ex));
        } finally {
        	try {
				Database.closeConnections(stmt, rs, connection);
			} catch (SQLException e) {
				e.printStackTrace();
			}
        }
	}
	
	
}
