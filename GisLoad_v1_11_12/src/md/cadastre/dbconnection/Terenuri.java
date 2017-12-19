package md.cadastre.dbconnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

import org.slf4j.LoggerFactory;

import md.cadastre.settingsparam.DbUtilities;

public class Terenuri {
	
	private static org.slf4j.Logger logger = LoggerFactory.getLogger(Terenuri.class);
	
	private String CodCadastral;
    private int CodTip;
    private int CodStr;
    private String NrCasa;
    private int Codnrremarc;
    private int Codtipregistr;
    private int Codcolect;
    private String Suprafata;
    private String Codadm;
    private String Datmodif;
    private String Timp;
    private String Cadzone;
    private int TopoStatus;
    private int StatusActiveId;
    private int CadRaionid;
    private int CadZoneid;
    private int Massiv;
    private int Sector;
    private int Parcelid;
    private String ParcelidJur;
    private String geom;
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
	public int getCodnrremarc() {
		return Codnrremarc;
	}
	public void setCodnrremarc(int codnrremarc) {
		Codnrremarc = codnrremarc;
	}
	public int getCodtipregistr() {
		return Codtipregistr;
	}
	public void setCodtipregistr(int codtipregistr) {
		Codtipregistr = codtipregistr;
	}
	public int getCodcolect() {
		return Codcolect;
	}
	public void setCodcolect(int codcolect) {
		Codcolect = codcolect;
	}
	public String getSuprafata() {
		return Suprafata;
	}
	public void setSuprafata(String suprafata) {
		Suprafata = suprafata;
	}
	public String getCodadm() {
		return Codadm;
	}
	public void setCodadm(String codadm) {
		Codadm = codadm;
	}
	public String getDatmodif() {
		return Datmodif;
	}
	public void setDatmodif(String datmodif) {
		Datmodif = datmodif;
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
	public String getParcelidJur() {
		return ParcelidJur;
	}
	public void setParcelidJur(String parcelidJur) {
		ParcelidJur = parcelidJur;
	}
	public String getGeom() {
		return geom;
	}
	public void setGeom(String geom) {
		this.geom = geom;
	}
    
	private static PreparedStatement stmt = null;
	private static ResultSet rs = null;
	private static Connection connection = null;
	
	public static boolean addColumns(String cadzone) {
		boolean result = false;
		try {
			connection = Database.getConnectionPG();
			String sql = "ALTER TABLE gisload.terenuri" + cadzone + " ADD topo_status integer ";
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
