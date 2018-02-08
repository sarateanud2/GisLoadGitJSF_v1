package md.cadastre.businesslogic;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import md.cadastre.dbconnection.Database;
import md.cadastre.dbcontrollers.LogDb;
import md.cadastre.dbcontrollers.Metadata;
import md.cadastre.dbcontrollers.Upload;
import md.cadastre.objects.UploadDetails;
import md.cadastre.settingsparam.CommandLine;
import md.cadastre.settingsparam.DbUtilities;
import md.cadastre.settingsparam.Settings;

public class LoadTabTooDB {
	
	static final Logger logger = LoggerFactory.getLogger(LoadTabTooDB.class);
	
	private static PreparedStatement stmt = null;
	private static ResultSet rs = null;
	private static Connection connection = null;
	
	private int errorflag = 0;
    private String cadZone = "-1";
    private int fileId;
    private int statusId;
    private int uploadId;
    private String fileType;
    private String path1;
    private String path2;	
    private String dir;
    
    public LoadTabTooDB(int uploadId, Metadata metadata) {
    	
    	this.uploadId = uploadId;
    	UploadDetails upDit = Upload.getDetailsByUploadId(uploadId); 
    	Settings setting = new Settings();
    	this.cadZone = upDit.getCadZone();
    	this.fileId = upDit.getFileNameId();
    	this.statusId = upDit.getStatusId();
    	this.fileType = upDit.getFileName();
    	metadata.setFilename(this.fileType);
    	this.dir = upDit.getDir();
    	this.path1 = setting.getGisPath();
    	this.path2 = this.dir + "\\" + this.cadZone + "\\";
    	
	}


	public void setCountUploadedObjects(Metadata m) {
    	try {
    		connection = Database.getConnectionPG();
    		String sql = "select count(*) cnt from gisload." + this.fileType + this.cadZone;
            stmt = connection.prepareStatement(sql);

            rs = stmt.executeQuery();
            while (rs.next()) {
                m.setCountRowsUploaded(rs.getInt("cnt"));
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
    
    
    public int executeCommand(Metadata m) {
    	Settings setting = new Settings();
    	String strGisPath = setting.getGisPath();
    	File fileTab = new  File(strGisPath + this.path2 + this.fileType + ".tab");
    	if (fileTab.exists()) {
    		LogDb.setLog(uploadId, cadZone, fileId, 0, "Start execute upload command");
    		String command = setting.getImportTabToPG() + " " + this.path1 + " " + this.path2 + " " + this.fileType + " " + this.cadZone;
    		System.out.println(command + "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    		LogDb.setLog(uploadId, cadZone, fileId, 0, "Command" + command);
    		int result = CommandLine.executeCommand(command);
    		setCountUploadedObjects(m);
    		if (m.getCountRowsUploaded() <= 0) {
                result = -20;
            }
    		LogDb.setLog(uploadId, cadZone, fileId, 0, "Total uploaded:" + m.getCountRowsUploaded());
    		LogDb.setLog(uploadId, cadZone, fileId, 0, "End execute upload command result = " + result);
    		
    		return result;
    	} else {
            logger.error(uploadId + " " + cadZone + " " + fileId + " " + "Nu exista asa tab fisier in sursa");
            LogDb.setLog(uploadId, cadZone, fileId, 1, "Nu exista asa tab fisier in sursa");
            return -10;
        }
    }
    
    public boolean startUpload(Metadata m) {
    	boolean isSuccess = false;
    	try {
            int result = -1;
            int counter = 0;
            do {
                counter++;
                result = executeCommand(m);
                if (result == -10 || result == -20) {
                    Upload upl = new Upload();
                    upl.deleteErrorCadzone(this.cadZone);
                    return false;
                } else if (result != 0) {
                    Thread.sleep(10000);
                }
            } while (result != 0 && counter <= 3);
            
            
            
    	}catch (Exception ex) {
    		logger.error(ex.getMessage() + ";" + Arrays.toString(ex.getStackTrace()));
            System.out.println(ex.getMessage());
            return false;
		}
    	
    	Verification vt = new Verification(this.uploadId);
    	isSuccess = vt.startVerification(m);
    	
    	if (!isSuccess) {
            return false;
        }
    	
    	isSuccess = CadastralObjects.checkDiff(this.cadZone, this.fileType, this.dir, m);
        if (!isSuccess) {
            return false;
        }
    	
    	
    	
    	return isSuccess;
    }
    
//	Get/Set
	public int getErrorflag() {
		return errorflag;
	}
	public void setErrorflag(int errorflag) {
		this.errorflag = errorflag;
	}
	public String getCadzone() {
		return cadZone;
	}
	public void setCadzone(String cadzone) {
		this.cadZone = cadzone;
	}
	public int getFileid() {
		return fileId;
	}
	public void setFileid(int fileid) {
		this.fileId = fileid;
	}
	public int getStatusid() {
		return statusId;
	}
	public void setStatusid(int statusid) {
		this.statusId = statusid;
	}
	public int getUploadid() {
		return uploadId;
	}
	public void setUploadid(int uploadid) {
		this.uploadId = uploadid;
	}
	public String getFiletype() {
		return fileType;
	}
	public void setFiletype(String filetype) {
		this.fileType = filetype;
	}
	public String getPath1() {
		return path1;
	}
	public void setPath1(String path1) {
		this.path1 = path1;
	}
	public String getPath2() {
		return path2;
	}
	public void setPath2(String path2) {
		this.path2 = path2;
	}
	public String getDir() {
		return dir;
	}
	public void setDir(String dir) {
		this.dir = dir;
	}
    
    
}
