package md.cadastre.objects;

public class UploadDetails {
	
	private int uploadId;
	private String cadZone;
	private int fileNameId;
	private String path;
	private String dir;
	private String fileName;
	private int statusId;
	
	public UploadDetails() {
		super();
	}

	public UploadDetails(int uploadId, String cadZone, int fileNameId, String path, String dir, String fileName,
			int statusId) {
		super();
		this.uploadId = uploadId;
		this.cadZone = cadZone;
		this.fileNameId = fileNameId;
		this.path = path;
		this.dir = dir;
		this.fileName = fileName;
		this.statusId = statusId;
	}

	public int getUploadId() {
		return uploadId;
	}

	public void setUploadId(int uploadId) {
		this.uploadId = uploadId;
	}

	public String getCadZone() {
		return cadZone;
	}

	public void setCadZone(String cadZone) {
		this.cadZone = cadZone;
	}

	public int getFileNameId() {
		return fileNameId;
	}

	public void setFileNameId(int fileNameId) {
		this.fileNameId = fileNameId;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getDir() {
		return dir;
	}

	public void setDir(String dir) {
		this.dir = dir;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public int getStatusId() {
		return statusId;
	}

	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}
	
}
