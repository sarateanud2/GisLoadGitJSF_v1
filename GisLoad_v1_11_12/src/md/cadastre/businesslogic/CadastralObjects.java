package md.cadastre.businesslogic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import md.cadastre.dbconnection.Database;
import md.cadastre.dbcontrollers.LogDb;
import md.cadastre.dbcontrollers.Metadata;
import md.cadastre.settingsparam.DbUtilities;

public class CadastralObjects {
	
	static final Logger logger = LoggerFactory.getLogger(CadastralObjects.class);
	
	private static PreparedStatement stmt = null;
	private static ResultSet rs = null;
	private static Connection connection = null;
	
	public static boolean checkDiff(String cadzone, String filetype, String dbname, Metadata m) {
		 boolean result = false;
	        LogDb.setLog(m.getUploadid(), cadzone, m.getFileid(), 0, "Start check if object is registered in Legalcad");

	        int counter = 0;
	        do {
	            counter++;
	            result = prepareDataToCheck(cadzone, filetype);
	            if (!result) {
	                try {
	                    Thread.sleep(10000);
	                } catch (InterruptedException ex) {
	                    logger.error(ex.getMessage() + "|" + Arrays.toString(ex.getStackTrace()));
	                }
	            }
	        } while (!result && counter <= 3);

	        if (!result) {
	            return false;
	        }

	        LogDb.setLog(m.getUploadid(), cadzone, m.getFileid(), 0, "Start retrieve new uploaded objects");
	        List<String> newObjects = getNewObjects(cadzone, filetype);
	        if (newObjects.isEmpty()) {
	            return false;
	        }

	        LogDb.setLog(m.getUploadid(), cadzone, m.getFileid(), 0, "Start retrieve registered objects");
	        List<String> regObjects;
	        if ("terenuri".equals(filetype)) {
	            regObjects = getRegObjectsTerenuri(cadzone, filetype, dbname);
	        } else {
	            regObjects = getRegObjectsCladiri(cadzone, filetype, dbname);
	        }
	        if (regObjects.isEmpty()) {
	            return false;
	        }

	        LogDb.setLog(m.getUploadid(), cadzone, m.getFileid(), 0, "Start check differences");
	        newObjects.removeAll(regObjects);
	        LogDb.setLog(m.getUploadid(), cadzone, m.getFileid(), 0, "Unregistered objects=" + Integer.toString(newObjects.size()));
	        
	        LogDb.setLog(m.getUploadid(), cadzone, m.getFileid(), 0, "Start update objects");
	        updateValues(cadzone, filetype, newObjects);
	        LogDb.setLog(m.getUploadid(), cadzone, m.getFileid(), 0, "End check for objects registered in Lecalcad");
	        
	        return true;
	}
	
	public static List<String> getRegObjectsTerenuri(String cadzone, String filetype, String dbname) {
		
		String query = "SELECT trim(to_char(A.CAD_RAIONID, '00')) || trim(to_char(A.CAD_ZONEID,'00')) || trim(to_char(A.MASSIV)) ||"
                + "       trim(to_char(A.SECTOR, '00')) || trim(to_char(A.PARCELID, '0000')) codcad"
                + "    From (SELECT "
                + "          p.CAD_RAIONID,"
                + "          p.CAD_ZONEID,"
                + "          p.MASSIV,"
                + "          p.SECTOR,"
                + "          p.PARCELID"
                + "  FROM " + dbname + ".PARCEL@GISINFO.ISC.CADASTRE.MD         P,"
                + "       " + dbname + ".RIGHTS@GISINFO.ISC.CADASTRE.MD         R,"
                + "       " + dbname + ".DOCUMENT_RIGHT@GISINFO.ISC.CADASTRE.MD D"
                + " WHERE R.CAD_RAIONID = P.CAD_RAIONID"
                + "   AND R.CAD_ZONEID = P.CAD_ZONEID"
                + "   AND R.MASSIV = P.MASSIV"
                + "   AND R.SECTOR = P.SECTOR"
                + "   AND R.PARCELID = P.PARCELID"
                + "   AND R.RIGHTID = D.RIGHTID"
                + "   AND R.OFFICERAIONID = D.OFFICERAIONID"
                + "   AND R.OFFICEKOMUNEID = D.OFFICEKOMUNEID"
                + "   AND R.STRUCTUREID = 0"
                + "   AND R.LITER = '0'"
                + "   AND R.UNITID = '0'"
                + "   AND P.STATUSID = 1"
                + "   AND (P.INREGISTRAREA <> 3 OR P.INREGISTRAREA IS NULL)"
                + "   AND R.STATUSID = 1"
                + "   AND R.PID IS NOT NULL   "
                + "   and p.cad_raionid = ?"
                + "   and p.cad_zoneid = ?"
                + " GROUP BY p.CAD_RAIONID,"
                + "                  p.CAD_ZONEID,"
                + "                  p.MASSIV,"
                + "                  p.SECTOR,"
                + "                  p.PARCELID) A";
        
        List<String> newObjects = new ArrayList<String>();
        try {
        	connection = Database.getConnectionOracle();
        	stmt = connection.prepareStatement(query);
            stmt.setInt(1, Integer.parseInt(cadzone.substring(0, 2)));
            stmt.setInt(2, Integer.parseInt(cadzone.substring(2)));
            rs = stmt.executeQuery();
            while (rs.next()) {
                newObjects.add(rs.getString("codcad"));
            }
        } catch(SQLException ex) {
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
		
		return newObjects;
	}
	
	public static List<String> getRegObjectsCladiri(String cadzone, String filetype, String dbname) {
		String query = "SELECT trim(to_char(p.CAD_RAIONID, '00')) || "
                + "       trim(to_char(p.CAD_ZONEID, '00')) || trim(to_char(p.MASSIV)) || "
                + "       trim(to_char(p.SECTOR, '00')) || trim(to_char(p.PARCELID, '0000')) || '.' || "
                + "       trim(to_char(p.structureid, '00')) codcad "
                + "  FROM " + dbname + ".STRUCTURE@GISINFO.ISC.CADASTRE.MD      P, "
                + "       " + dbname + ".RIGHTS@GISINFO.ISC.CADASTRE.MD         R, "
                + "       " + dbname + ".DOCUMENT_RIGHT@GISINFO.ISC.CADASTRE.MD D "
                + " WHERE R.CAD_RAIONID = P.CAD_RAIONID "
                + "   AND R.CAD_ZONEID = P.CAD_ZONEID "
                + "   AND R.MASSIV = P.MASSIV "
                + "   AND R.SECTOR = P.SECTOR "
                + "   AND R.PARCELID = P.PARCELID "
                + "   AND R.STRUCTUREID = P.STRUCTUREID "
                + "   AND R.RIGHTID = D.RIGHTID "
                + "   AND R.OFFICERAIONID = D.OFFICERAIONID "
                + "   AND R.OFFICEKOMUNEID = D.OFFICEKOMUNEID "
                + "   AND R.STRUCTUREID <> 0 "
                + "   AND R.LITER <> '0' "
                + "   AND R.UNITID = '0' "
                + "   AND P.STATUSID = 1 "
                + "   AND (P.INREGISTRAREA <> 3 OR P.INREGISTRAREA IS NULL) "
                + "   AND R.STATUSID = 1 "
                + "   AND R.PID IS NOT NULL "
                + "   and p.cad_raionid = ?"
                + "   and p.cad_zoneid = ?"
                + " GROUP BY P.CAD_RAIONID, "
                + "          P.CAD_ZONEID, "
                + "          P.MASSIV, "
                + "          P.SECTOR, "
                + "          P.PARCELID, "
                + "          p.structureid";
        List<String> newObjects = new ArrayList<String>();
        try {
        	connection = Database.getConnectionOracle();
        	stmt = connection.prepareStatement(query);
            stmt.setInt(1, Integer.parseInt(cadzone.substring(0, 2)));
            stmt.setInt(2, Integer.parseInt(cadzone.substring(2)));
            rs = stmt.executeQuery();
            while (rs.next()) {
                newObjects.add(rs.getString("codcad"));
            }
        } catch(SQLException ex) {
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
		
		return newObjects;
	}
	
	public static List<String> getNewObjects(String cadzone, String filetype) {
		String query = "SELECT codcadastral FROM gisload." + filetype + cadzone + " where topo_status = 1";
        List<String> newCodcadList = new ArrayList<String>();
        try {
    		connection = Database.getConnectionPG();
    		stmt = connection.prepareStatement(query);
            rs = stmt.executeQuery();
            while (rs.next()) {
                newCodcadList.add(rs.getString("codcadastral"));
            }
        } catch(SQLException ex) {
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
		
		return newCodcadList;
	}
	
	public static int updateValues(String cadzone, String filetype, List<String> updval) {
		String query = "update gisload." + filetype + cadzone + " set topo_status = 2 where codcadastral = ?";
        int rowmodified = 0;
		try {
    		connection = Database.getConnectionPG();
    		stmt = connection.prepareStatement(query);

            for (String val : updval) {
                stmt.setString(1, val);
                stmt.addBatch();
            }
            stmt.executeBatch();
            rowmodified = stmt.getUpdateCount();
		} catch(SQLException ex) {
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
		
		return rowmodified;
	}
	
	public static boolean prepareDataToCheck(String cadzone, String filetype) {
		boolean result = false;
		try {
    		connection = Database.getConnectionPG();
    		String query = "update gisload." + filetype + cadzone + " set topo_status = 1";
    		stmt = connection.prepareStatement(query);
            stmt.executeUpdate();
            query = "update gisload." + filetype + cadzone + " set topo_status = 2 where length(codcadastral) < 11";
            stmt = connection.prepareStatement(query);
            stmt.executeUpdate();
            result = true;
		} catch(SQLException ex) {
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
}
