package md.cadastre.businesslogic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

import org.slf4j.LoggerFactory;

import md.cadastre.dbconnection.Database;
import md.cadastre.dbcontrollers.LogDb;
import md.cadastre.dbcontrollers.Metadata;
import md.cadastre.dbcontrollers.SyncronizeOracle;
import md.cadastre.settingsparam.DbUtilities;
import md.cadastre.settingsparam.Settings;

public class Syncronize {
	
	static final org.slf4j.Logger logger = LoggerFactory.getLogger(Syncronize.class);
	
	private static PreparedStatement stmt = null;
	private static ResultSet rs = null;
	private static Connection connection = null;
	
	public static boolean deleteCadzone(String cadzone, String filetype, Metadata m) {
		boolean result = false;
		try {
    		connection = Database.getConnectionPG();
    		String sql = "delete from gis." + filetype + " t where t.cadzone = ?";
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, cadzone);
            stmt.executeUpdate();
            m.setRowsSynchronizeDeleted(stmt.getUpdateCount());
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
	
	public static boolean insertCadzone(String cadzone, String filetype, Metadata m) {
		boolean result = false;
		try {
    		connection = Database.getConnectionPG();
    		String sql = "insert into gis." + filetype + "  (select *, ? from gisload." + filetype + cadzone + ")";
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, cadzone);
            stmt.executeUpdate();
            m.setRowsSynchronizeInserted(stmt.getUpdateCount());
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
	
	/*public static boolean executeSync(int uploadid, String cadzone, String filetype, Metadata m) {
		boolean result = false;
		Settings settings = new Settings();

        LogDb.setLog(uploadid, cadzone, m.getFileid(), 0, "Start synchronize");
        if (deleteCadzone(cadzone, filetype, m)) {
            if (insertCadzone(cadzone, filetype, m)) {
                LogDb.setLog(uploadid, cadzone, m.getFileid(), 0, "Synchronize successfully completed");
            } else {
                LogDb.setLog(uploadid, cadzone, m.getFileid(), 1, "Error on insert");
                return false;
            }
        } else {
            LogDb.setLog(uploadid, cadzone, m.getFileid(), 0, "Error on delete");
            return false;
        }

        if (null != filetype) {
            int counter = 0;
            switch (filetype) {
                case "terenuri":
                    SyncronizeOracle so = new SyncronizeOracle();
                    do {
                        result = so.deleteCadzone(cadzone, settings.getTerenuriTableOracle(), m);
                        if (!result) {
                            try {
                                Thread.sleep(20000);
                            } catch (InterruptedException ex) {
                                logger.error(ex.getMessage() + "\n" + Arrays.toString(ex.getStackTrace()));
                            }
                        }
                    } while (!result);

                    do {
                        counter++;
                        result = so.Terenuri(cadzone, m);
                        if (!result) {
                            try {
                                Thread.sleep(20000);
                            } catch (InterruptedException ex) {
                                logger.error(ex.getMessage() + "\n" + Arrays.toString(ex.getStackTrace()));
                            }
                        }
                    } while (!result && counter <= 2);

                    LogDb.setLog(m.getUploadid(), cadzone, m.getFileid(), 0, " Rows inserted " + m.getCountInsertedOracle());
                    break;
                case "cladiri":
                    SyncronizeOracle soc = new SyncronizeOracle();
                    do {
                        result = soc.deleteCadzone(cadzone, GlobalParams.getCladiri_table_oracle(), m);
                        if (!result) {
                            try {
                                Thread.sleep(20000);
                            } catch (InterruptedException ex) {
                                logger.error(ex.getMessage() + "\n" + Arrays.toString(ex.getStackTrace()));
                            }
                        }
                    } while (!result);

                    do {
                        counter++;
                        result = soc.Cladiri(cadzone, m);
                        if (!result) {
                            try {
                                Thread.sleep(20000);
                            } catch (InterruptedException ex) {
                                logger.error(ex.getMessage() + "\n" + Arrays.toString(ex.getStackTrace()));
                            }
                        }
                    } while (!result && counter <= 2);

                    LogDb.setLog(m.getUploadid(), cadzone, m.getFileid(), 0, " Rows inserted Oracle " + m.getCountInsertedOracle());
                    break;
            }
        }
        LogDb.setLog(uploadid, cadzone, m.getFileid(), 0, "End synchronization");
        return result;
	}*/
	
	

}
