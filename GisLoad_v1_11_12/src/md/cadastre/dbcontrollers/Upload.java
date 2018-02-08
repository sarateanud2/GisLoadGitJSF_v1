package md.cadastre.dbcontrollers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import md.cadastre.businesslogic.LoadTabTooDB;
import md.cadastre.dbconnection.Database;
import md.cadastre.objects.UploadDetails;
import md.cadastre.settingsparam.DbUtilities;

public class Upload {
	
	static final Logger logger = LoggerFactory.getLogger(Upload.class);
	
	private static PreparedStatement stmt = null;
	private static ResultSet rs = null;
	private static Connection connection = null;
	
	public void startUploadCadzone(String cadzone, int fileid, boolean forceinit) {
		Upload upl = new Upload();
		int uploadid = 0;
		do {
            uploadid = upl.initializeUpload(cadzone, fileid, forceinit);
        } while (uploadid == 0);
		Metadata m = new Metadata();
        m.setCadzone(cadzone);
        m.setFileid(fileid);
        m.setUploadid(uploadid);
        LogDb.setLog(m.getUploadid(), m.getCadzone(), m.getFileid(), 0, "Start upload");
        LoadTabTooDB lt = new LoadTabTooDB(uploadid, m);
        boolean result = lt.startUpload(m);
        LogDb.setUploadStats(m);
        if(!result){
            //insertErrorCadzone(cadzone);
            LogDb.setLog(m.getUploadid(), m.getCadzone(), m.getFileid(), 1, "Upload Finished unsuccessfully");
        }else{
            deleteErrorCadzone(cadzone);
        }
        LogDb.setLog(m.getUploadid(), m.getCadzone(), m.getFileid(), 0, "End upload");
	}
	
	
	public boolean modifyUpload(int id) {
		boolean result = false;
		try {
    		connection = Database.getConnectionPG();
    		String sql = "UPDATE gisadm.t_upload set end_date = CURRENT_TIMESTAMP WHERE id = ?";
    		stmt = connection.prepareStatement(sql);
    		stmt.setInt(1, id);
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
	
	public boolean modifyUpload(String cadzone, int filenameid) {
		boolean result = false;
		try {
    		connection = Database.getConnectionPG();
    		String sql = "UPDATE gisadm.t_upload set end_date = CURRENT_TIMESTAMP "
                    + " WHERE cadzone = ? and filenameid = ?"
                    + " and start_date <= CURRENT_TIMESTAMP"
                    + " and end_date > CURRENT_TIMESTAMP";
    		stmt = connection.prepareStatement(sql);
    		stmt.setString(1, cadzone);
    		stmt.setInt(2, filenameid);
    		stmt.executeUpdate();
    		result = true;
		} catch(SQLException ex) {
            logger.error(ex.getMessage() + ";" + Arrays.toString(ex.getStackTrace()));
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
	
	public boolean existActiveInstance(String cadzone, int filenameid) {
		boolean result = false;
		try {
    		connection = Database.getConnectionPG();
    		String sql = "select count(*) cnt from gisadm.t_upload "
                    + " where cadzone = ? and filenameid = ? "
                    + " and start_date <= CURRENT_TIMESTAMP"
                    + " and end_date > CURRENT_TIMESTAMP";
    		stmt = connection.prepareStatement(sql);
    		stmt.setString(1, cadzone);
    		stmt.setInt(2, filenameid);
    		rs = stmt.executeQuery();
    		int count = 1;
    		
            while (rs.next()) {
                count = rs.getInt("cnt");
            }
            if (count > 0) {
                result = true;
            } else {
                result = false;
            }
            
		} catch(SQLException ex) {
            logger.error(ex.getMessage() + ";" + Arrays.toString(ex.getStackTrace()));
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
	
	public int initializeUpload(String cadzone, int filenameid, boolean forceinit) {
		int id = 0;
		try {
            boolean existActiveInstance = existActiveInstance(cadzone, filenameid);
            if (existActiveInstance && forceinit) {
                modifyUpload(cadzone, filenameid);
                existActiveInstance = false;
            }
            if (!existActiveInstance) {
                do {
                    id = insertUpload(cadzone, filenameid);
                    if (id != 0) {
                        modifyUpload(id);
                    } else {
                        Thread.sleep(10000);
                    }
                } while (id == 0);
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage() + "|" + Arrays.toString(ex.getStackTrace()));
        }
		
		return 0;
	}

	private int insertUpload(String cadzone, int filenameid) {
		int id = 0;
		try {
    		connection = Database.getConnectionPG();
    		String sql = "select count(*) cnt from gisadm.t_upload "
                    + " where cadzone = ? and filenameid = ? "
                    + " and start_date <= CURRENT_TIMESTAMP"
                    + " and end_date > CURRENT_TIMESTAMP";
    		stmt = connection.prepareStatement(sql, new String[] {"id"});
    		stmt.setString(1, cadzone);
    		stmt.setInt(2, filenameid);
    		rs = stmt.getGeneratedKeys();
    		if (rs.next()) {
                id = rs.getInt(1);
            }
		} catch(SQLException ex) {
            logger.error(ex.getMessage() + ";" + Arrays.toString(ex.getStackTrace()));
            logger.error(DbUtilities.printSQLException(ex));
        } finally {
        	try {
				Database.closeConnections(stmt, rs, connection);
			} catch (SQLException e) {
				e.printStackTrace();
			}
        }

		return id;
	}
	
	public void deleteErrorCadzone(String cadzone) {
		try {
    		connection = Database.getConnectionPG();
    		String sql = "delete from gisadm.t_upload_error where cadzone = ?";
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, cadzone);
            stmt.executeUpdate();
		} catch(SQLException ex) {
            logger.error(ex.getMessage() + ";" + Arrays.toString(ex.getStackTrace()));
            logger.error(DbUtilities.printSQLException(ex));
        } finally {
        	try {
				Database.closeConnections(stmt, rs, connection);
			} catch (SQLException e) {
				e.printStackTrace();
			}
        }
	}
	
	public List<String> getErrorCadzone() {
		String query = "SELECT cadzone FROM gisadm.t_upload_error";
        List<String> cadZoneList = new ArrayList<String>();
		try {
    		connection = Database.getConnectionPG();
    		stmt = connection.prepareStatement(query);
            rs = stmt.executeQuery();
            while (rs.next()) {
                cadZoneList.add(rs.getString("cadzone"));
            }
		} catch(SQLException ex) {
            logger.error(ex.getMessage() + ";" + Arrays.toString(ex.getStackTrace()));
            logger.error(DbUtilities.printSQLException(ex));
        } finally {
        	try {
				Database.closeConnections(stmt, rs, connection);
			} catch (SQLException e) {
				e.printStackTrace();
			}
        }
		
		return cadZoneList;
	}
	
	public static UploadDetails getDetailsByUploadId(int uploadid) {
		UploadDetails up = new UploadDetails();
		try {
    		connection = Database.getConnectionPG();
    		String query = "select * from gisadm.v_task_upload "
                    + " where uploadid = ?";
    		stmt = connection.prepareStatement(query);
    		stmt.setInt(1, uploadid);
            rs = stmt.executeQuery();
            while (rs.next()) {
            	up.setUploadId(rs.getInt("uploadid"));
            	up.setCadZone(rs.getString("cadzone"));
            	up.setFileNameId(rs.getInt("filenameid"));
            	up.setPath(rs.getString("path"));
            	up.setDir(rs.getString("dir"));
            	up.setFileName(rs.getString("filename"));
            	up.setStatusId(0);
            }
		} catch(SQLException ex) {
            logger.error(ex.getMessage() + ";" + Arrays.toString(ex.getStackTrace()));
            logger.error(DbUtilities.printSQLException(ex));
        } finally {
        	try {
				Database.closeConnections(stmt, rs, connection);
			} catch (SQLException e) {
				e.printStackTrace();
			}
        }
		return up;
	}
	
	
	
}
