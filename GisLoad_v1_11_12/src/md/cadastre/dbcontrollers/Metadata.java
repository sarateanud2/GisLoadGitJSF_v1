package md.cadastre.dbcontrollers;

public class Metadata {
	
	private int uploadid;

    private String cadzone;

    private int fileid;

    private String filename;

    private int correctedGeometries = -1;

    private int rowsSynchronizeInserted = -1;

    private int rowsSynchronizeDeleted = -1;

    private int countRowsUploaded = -1;

    private int countCodCadastralExists = -1;

    private int countInsertedOracle = -1;

    private int countDeletedOracle = -1;

    private int countMapinfo = -1;

	public int getUploadid() {
		return uploadid;
	}

	public void setUploadid(int uploadid) {
		this.uploadid = uploadid;
	}

	public String getCadzone() {
		return cadzone;
	}

	public void setCadzone(String cadzone) {
		this.cadzone = cadzone;
	}

	public int getFileid() {
		return fileid;
	}

	public void setFileid(int fileid) {
		this.fileid = fileid;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public int getCorrectedGeometries() {
		return correctedGeometries;
	}

	public void setCorrectedGeometries(int correctedGeometries) {
		this.correctedGeometries = correctedGeometries;
	}

	public int getRowsSynchronizeInserted() {
		return rowsSynchronizeInserted;
	}

	public void setRowsSynchronizeInserted(int rowsSynchronizeInserted) {
		this.rowsSynchronizeInserted = rowsSynchronizeInserted;
	}

	public int getRowsSynchronizeDeleted() {
		return rowsSynchronizeDeleted;
	}

	public void setRowsSynchronizeDeleted(int rowsSynchronizeDeleted) {
		this.rowsSynchronizeDeleted = rowsSynchronizeDeleted;
	}

	public int getCountRowsUploaded() {
		return countRowsUploaded;
	}

	public void setCountRowsUploaded(int countRowsUploaded) {
		this.countRowsUploaded = countRowsUploaded;
	}

	public int getCountCodCadastralExists() {
		return countCodCadastralExists;
	}

	public void setCountCodCadastralExists(int countCodCadastralExists) {
		this.countCodCadastralExists = countCodCadastralExists;
	}

	public int getCountInsertedOracle() {
		return countInsertedOracle;
	}

	public void setCountInsertedOracle(int countInsertedOracle) {
		this.countInsertedOracle = countInsertedOracle;
	}

	public int getCountDeletedOracle() {
		return countDeletedOracle;
	}

	public void setCountDeletedOracle(int countDeletedOracle) {
		this.countDeletedOracle = countDeletedOracle;
	}

	public int getCountMapinfo() {
		return countMapinfo;
	}

	public void setCountMapinfo(int countMapinfo) {
		this.countMapinfo = countMapinfo;
	}

}
