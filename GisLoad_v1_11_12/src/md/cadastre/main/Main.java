package md.cadastre.main;

import md.cadastre.dbcontrollers.Upload;

public class Main {

	public static void main(String[] args) {
		Upload upload = new Upload();
		upload.startUploadCadzone("0300", 1, true);
		upload.startUploadCadzone("0300", 2, true);

	}

}
