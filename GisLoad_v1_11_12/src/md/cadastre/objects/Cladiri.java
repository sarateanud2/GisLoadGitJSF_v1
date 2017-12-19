package md.cadastre.objects;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import md.cadastre.dbconnection.Database;
import md.cadastre.settingsparam.DbUtilities;

public class Cladiri {
	
	static final Logger logger = LoggerFactory.getLogger(Cladiri.class);
	
	private String CodCadastral;
    private int CodTip;
    private int CodStr;
    private String NrCasa;
    private String Litera;
    private int Codnrremarc;
    private String Destfunc;
    private int Codcolect;
    private String Codadm;
    private String Timp;
    private String Cadzone;
    private int TopoStatus;
    private int StatusActiveId;
    private int CadRaionid;
    private int CadZoneid;
    private int Massiv;
    private int Sector;
    private int Parcelid;
    private int Structureid;
    private String Liter;
    private String geom;
    private int ogcFid;
	public String getCodCadastral() {
		return CodCadastral;
	}
	public void setCodCadastral(String codCadastral) {
		CodCadastral = codCadastral;
	}
	public int getCodTip() {
		return CodTip;
	}
	public void setCodTip(int codTip) {
		CodTip = codTip;
	}
	public int getCodStr() {
		return CodStr;
	}
	public void setCodStr(int codStr) {
		CodStr = codStr;
	}
	public String getNrCasa() {
		return NrCasa;
	}
	public void setNrCasa(String nrCasa) {
		NrCasa = nrCasa;
	}
	public String getLitera() {
		return Litera;
	}
	public void setLitera(String litera) {
		Litera = litera;
	}
	public int getCodnrremarc() {
		return Codnrremarc;
	}
	public void setCodnrremarc(int codnrremarc) {
		Codnrremarc = codnrremarc;
	}
	public String getDestfunc() {
		return Destfunc;
	}
	public void setDestfunc(String destfunc) {
		Destfunc = destfunc;
	}
	public int getCodcolect() {
		return Codcolect;
	}
	public void setCodcolect(int codcolect) {
		Codcolect = codcolect;
	}
	public String getCodadm() {
		return Codadm;
	}
	public void setCodadm(String codadm) {
		Codadm = codadm;
	}
	public String getTimp() {
		return Timp;
	}
	public void setTimp(String timp) {
		Timp = timp;
	}
	public String getCadzone() {
		return Cadzone;
	}
	public void setCadzone(String cadzone) {
		Cadzone = cadzone;
	}
	public int getTopoStatus() {
		return TopoStatus;
	}
	public void setTopoStatus(int topoStatus) {
		TopoStatus = topoStatus;
	}
	public int getStatusActiveId() {
		return StatusActiveId;
	}
	public void setStatusActiveId(int statusActiveId) {
		StatusActiveId = statusActiveId;
	}
	public int getCadRaionid() {
		return CadRaionid;
	}
	public void setCadRaionid(int cadRaionid) {
		CadRaionid = cadRaionid;
	}
	public int getCadZoneid() {
		return CadZoneid;
	}
	public void setCadZoneid(int cadZoneid) {
		CadZoneid = cadZoneid;
	}
	public int getMassiv() {
		return Massiv;
	}
	public void setMassiv(int massiv) {
		Massiv = massiv;
	}
	public int getSector() {
		return Sector;
	}
	public void setSector(int sector) {
		Sector = sector;
	}
	public int getParcelid() {
		return Parcelid;
	}
	public void setParcelid(int parcelid) {
		Parcelid = parcelid;
	}
	public int getStructureid() {
		return Structureid;
	}
	public void setStructureid(int structureid) {
		Structureid = structureid;
	}
	public String getLiter() {
		return Liter;
	}
	public void setLiter(String liter) {
		Liter = liter;
	}
	public String getGeom() {
		return geom;
	}
	public void setGeom(String geom) {
		this.geom = geom;
	}
	public int getOgcFid() {
		return ogcFid;
	}
	public void setOgcFid(int ogcFid) {
		this.ogcFid = ogcFid;
	}
    
	
	private static PreparedStatement stmt = null;
	private static ResultSet rs = null;
	private static Connection connection = null;
	
	public static boolean addColumns(String cadzone) {
		boolean result = false;
		try {
			connection = Database.getConnectionPG();
			String sql = "ALTER TABLE gisload.cladiri" + cadzone + " ADD newcodcadastral CHAR(30) ";
			stmt = connection.prepareStatement(sql);
			stmt.execute();
			sql = "ALTER TABLE gisload.cladiri" + cadzone + " ADD topo_status integer ";
            stmt = connection.prepareStatement(sql);
            stmt.execute();
            sql = "ALTER TABLE gisload.cladiri" + cadzone + " ALTER COLUMN codcadastral TYPE VARCHAR(20) ";
            stmt = connection.prepareStatement(sql);
            stmt.execute();
			
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
	
	

}
