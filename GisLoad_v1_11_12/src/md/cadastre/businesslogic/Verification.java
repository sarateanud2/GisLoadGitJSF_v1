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
import md.cadastre.dbcontrollers.Upload;
import md.cadastre.objects.Cladiri;
import md.cadastre.objects.Terenuri;
import md.cadastre.objects.UploadDetails;
import md.cadastre.settingsparam.DbUtilities;

public class Verification {
	static final org.slf4j.Logger logger = LoggerFactory.getLogger(Verification.class);
	
	private String cadZone;
    private int fileId;
    private int uploadId;
    private String fileType;
    
    private static PreparedStatement stmt = null;
	private static ResultSet rs = null;
	private static Connection connection = null;
    
    public Verification(int uploadId) {
    	this.uploadId = uploadId;
    	
    	UploadDetails upDit = Upload.getDetailsByUploadId(uploadId);
    	
    	this.cadZone = upDit.getCadZone();
    	this.fileType = upDit.getFileName();
    	this.uploadId = upDit.getFileNameId();
	}
    
    @SuppressWarnings("unused")
	private boolean makeValidGeometries(Metadata m) {
    	boolean result = false;
    	try {
    		connection = Database.getConnectionPG();
    		String sql = "update gisload." + this.fileType + this.cadZone
                    + " set wkb_geometry = ST_MakeValid(wkb_geometry) "
                    + " where ST_isValid(wkb_geometry) = false;";
    		stmt = connection.prepareStatement(sql);
            stmt.executeUpdate();
            m.setCorrectedGeometries(stmt.getUpdateCount());
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
    	
    	LogDb.setLog(this.uploadId, this.cadZone, this.fileId, 0, " Invalid geometries corrected = " + m.getCorrectedGeometries());
        LogDb.setLog(this.uploadId, this.cadZone, this.fileId, 0, " End validate geometries");
    	
    	return  result;
    }
    
    public boolean startVerification(Metadata m) {
    	boolean result = false;
    	LogDb.setLog(this.uploadId, this.cadZone, this.fileId, 0, "Start verification ");
    	try {
            int counter = 0;
            do {
                counter++;
                result = executeCommand(m);
                if (result) {
                    LogDb.setLog(this.uploadId, this.cadZone, this.fileId, 0, "Verification executed successfully");
                } else {
                    LogDb.setLog(this.uploadId, this.cadZone, this.fileId, 0, "Error on task verification execution");
                }
            } while (!result && (counter <= 3));
        } catch (Exception ex) {
            logger.error(ex.getMessage() + ";" + Arrays.toString(ex.getStackTrace()));
        }
    	
    	return result;
    }
    
    public void setCountWithCodCadastralObjects(Metadata m) {
    	try {
    		connection = Database.getConnectionPG();
    		String sql = "select count(*) cnt from gisload." + this.fileType + this.cadZone + " where codcadastral is not null";
            stmt = connection.prepareStatement(sql);

            rs = stmt.executeQuery();
            while (rs.next()) {
                m.setCountCodCadastralExists(rs.getInt("cnt"));
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
    }
    
    @SuppressWarnings("unused")
	private boolean setCodCadastralCladiri(Metadata m) {
    	LogDb.setLog(this.uploadId, this.cadZone, this.fileId, 0, "Start set cod cadastral cladiri");
        boolean result = false;
        
        try {
        	connection = Database.getConnectionPG();
            String sql = "update gisload.cladiri" + this.cadZone + " clad "
                    + " Set codcadastral = (select "
                    + "    codcadastral "
                    + "  from (select "
                    + "    s.codcadastral, "
                    + "    prc "
                    + "  from (select "
                    + "    t.codcadastral, "
                    + "    st_area(st_intersection(c.wkb_geometry, t.wkb_geometry))/st_area(c.wkb_geometry)*100 prc "
                    + "  from gisload.cladiri" + this.cadZone + " c, gisload.terenuri" + this.cadZone + " t "
                    + "  where "
                    + "    c.ogc_fid = clad.ogc_fid and st_numgeometries(c.wkb_geometry) = 1 and st_numgeometries(t.wkb_geometry) = 1 and st_intersects(c.wkb_geometry, t.wkb_geometry)) s "
                    + "  where "
                    + "    s.prc > 50 "
                    + "  order by "
                    + "    2 desc) v "
                    + "  limit 1) || '.' || codcadastral"
                    + " where st_area(clad.wkb_geometry) != 0 and length(trim(clad.codcadastral)) < 8";
            stmt = connection.prepareStatement(sql);
            stmt.executeUpdate();
            m.setCountCodCadastralExists(stmt.getUpdateCount());
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
    
  
    
	private boolean executeCommand(Metadata m) {
		boolean result = true;
        do {
            result = makeValidGeometries(m);
            if (!result) {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException ex) {
                    logger.error(ex.getMessage() + "|" + Arrays.toString(ex.getStackTrace()));
                }
            }
        } while (!result);

        if (this.fileId == 2) {
            Cladiri.addColumns(this.cadZone);
            if (!setCodCadastralCladiri(m)) {
                result = false;
            }
        } else if (this.fileId == 1) {
            Terenuri.addColumns(this.cadZone);
            setCountWithCodCadastralObjects(m);
        }
        return result;
	}

	//    Get/Set
	public String getCadZone() {
		return cadZone;
	}
	public void setCadZone(String cadZone) {
		this.cadZone = cadZone;
	}
	public int getFileId() {
		return fileId;
	}
	public void setFileId(int fileId) {
		this.fileId = fileId;
	}
	public int getUploadId() {
		return uploadId;
	}
	public void setUploadId(int uploadId) {
		this.uploadId = uploadId;
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
    
    
    
    

}
