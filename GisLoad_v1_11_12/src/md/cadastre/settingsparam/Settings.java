package md.cadastre.settingsparam;

public class Settings {
	private String gisPath = "\\\\frsrv\\tcodata\\gisdata\\";
	private String importOrclConn = "gisdata/gisdata@cdbsrv1test";
	private String importTabToPG = "C:\\OSGeo4W64\\bin\\import_pg.bat";
	private String cladiriTableOracle = "cladiri111";
	private String terenuriTableOracle = "terenuri111";
	
	public Settings() {
		super();
	}
	public String getGisPath() {
		return gisPath;
	}
	public String getImportOrclConn() {
		return importOrclConn;
	}
	public String getImportTabToPG() {
		return importTabToPG;
	}
	public String getCladiriTableOracle() {
		return cladiriTableOracle;
	}
	public String getTerenuriTableOracle() {
		return terenuriTableOracle;
	}
	
	
}
