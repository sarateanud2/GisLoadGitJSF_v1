package md.cadastre.properties;

import java.util.Properties;

public class GetAllProperties {
	private Properties prop = new Properties();
	
	private String oracleCladiriTabel;
	private String oracleTerenuriTabel;
	private String gisPath;
	private String tempPath;
	private String importOrclConn;
	private String importToOracle;
	
	public GetAllProperties() {
		this.oracleCladiriTabel = prop.getProperty("cladiri_table_oracle");
		this.oracleTerenuriTabel = prop.getProperty("terenuri_table_oracle");
		this.gisPath = prop.getProperty("gispath");
		this.tempPath = prop.getProperty("temp_path");
		this.importOrclConn = prop.getProperty("import_orcl_conn");
		this.importToOracle = prop.getProperty("import_to_oracle");
	}
	
	public String getOracleCladiriTabel() {
		return oracleCladiriTabel;
	}
	public String getOracleTerenuriTabel() {
		return oracleTerenuriTabel;
	}
	public String getGisPath() {
		return gisPath;
	}
	public String getTempPath() {
		return tempPath;
	}
	public String getImportOrclConn() {
		return importOrclConn;
	}
	public String getImportToOracle() {
		return importToOracle;
	}
	
	
	
}
