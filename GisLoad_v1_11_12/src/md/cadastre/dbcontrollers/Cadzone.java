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

public class Cadzone {
	
	static final Logger logger = LoggerFactory.getLogger(Cadzone.class);

    private String cadzone;
    private String filiala;
    private String codtco;
    private long terenuri_date_modified;
    private long cladiri_date_modified;
    
    public String getCadzone() {
		return cadzone;
	}

	public void setCadzone(String cadzone) {
		this.cadzone = cadzone;
	}

	public String getFiliala() {
		return filiala;
	}

	public void setFiliala(String filiala) {
		this.filiala = filiala;
	}

	public String getCodtco() {
		return codtco;
	}

	public void setCodtco(String codtco) {
		this.codtco = codtco;
	}

	public long getTerenuri_date_modified() {
		return terenuri_date_modified;
	}

	public void setTerenuri_date_modified(long terenuri_date_modified) {
		this.terenuri_date_modified = terenuri_date_modified;
	}

	public long getCladiri_date_modified() {
		return cladiri_date_modified;
	}

	public void setCladiri_date_modified(long cladiri_date_modified) {
		this.cladiri_date_modified = cladiri_date_modified;
	}
	
	

	private static PreparedStatement stmt = null;
	private static ResultSet rs = null;
	private static Connection connection = null;
	
    public static ArrayList<String> getCadzoneList(String database) {
    	ArrayList<String> cadzone = new ArrayList<>();
    	
    	connection = Database.getConnectionPG();
    	
            try {
            	if (!"-".equals(database)) {
            		stmt = connection.prepareStatement("select cadzone from gisadm.t_zone " + " where description = '" 
            											+ database.toUpperCase() + "'and in_use = 'Y'");
		        } else {
		            stmt = connection.prepareStatement("select cadzone from gisadm.t_zone "
		                    + " where in_use = 'Y'");
		        }
            	
            	rs = stmt.executeQuery();
                while (rs.next()) {
                    cadzone.add(rs.getString("cadzone"));
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
    	
    	return cadzone;
    }
    
    public static ArrayList<Cadzone> getCadzoneArray(String database) {
    	ArrayList<Cadzone> cadzone = new ArrayList<>();
    	
    	try {
    		connection = Database.getConnectionPG();
    		if (!"-".equals(database)) {
                stmt = connection.prepareStatement("select * from gisadm.t_zone "
                        + " where description = '" + database.toUpperCase() + "' and in_use = 'Y'");

            } else {
                stmt = connection.prepareStatement("select * from gisadm.t_zone "
                        + " where in_use = 'Y'");
            }
            rs = stmt.executeQuery();
            while (rs.next()) {
                Cadzone c = new Cadzone();
                c.setCadzone(rs.getString("cadzone"));
                c.setFiliala(rs.getString("description"));
                c.setCodtco(rs.getString("codtco"));
                c.setTerenuri_date_modified(rs.getLong("terenuri_date_modified"));
                c.setCladiri_date_modified(rs.getLong("cladiri_date_modified"));
                cadzone.add(c);
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
    	
    	return cadzone;
    }
    
    public static void insertUploadCadzone() {
    	connection = Database.getConnectionPG();
    	try {
    		String sql = "insert into gisadm.t_upload_error "
                    + "select cadzone from gisadm.t_zone where in_use = 'Y'";
    		stmt = connection.prepareStatement(sql);
            stmt.executeUpdate();
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
