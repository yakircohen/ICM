package entity;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;

import common.MyFile;

/**
 * this class represents a document the user attached to his request
 *
 */
public class Document implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String iddocument; // this attribute will be set in server side
	private String fileName;
	private String fileType;
	private String path;
	private String idrequest;
	private MyFile myFile;

	/**
	 * set the MyFile version of the file
	 * @param myFile
	 */
	public void setMyFile(MyFile myFile) {
		this.myFile = myFile;
	}

	/**
	 * return the id of the document
	 * @return the id of the document
	 */
	public String getIddocument() {
		return iddocument;
	}

	/**
	 * set the id of the document, used to insert or select a file from the DB
	 * @param iddocument
	 */
	public void setIddocument(String iddocument) {
		this.iddocument = iddocument;
	}

	/**
	 * return the name of the file
	 * @return the name of the file
	 */
	public String getFileName() {
		return fileName;
	}
/**
 * set the name of the file
 * @param fileName - the name of the file
 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
/**
 * the file format/type
 * @return the file format/type
 */
	public String getFileType() {
		return fileType;
	}
/**
 * set the type of the file
 * @param fileType - the type of the file
 */
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
/**
 * the path of the file
 * @return the path of the file
 */
	public String getPath() {
		return path;
	}
/**
 * set the path of the file
 * @param path - the path of the file
 */
	public void setPath(String path) {
		this.path = path;
	}
/**
 * get the id of the request this file belongs to
 * @return the id of the request this file belongs to
 */
	public String getIdrequest() {
		return idrequest;
	}
/**
 * set the id of the request this file belongs to
 * @param idrequest - the id of the request this file belongs to
 */
	public void setIdrequest(String idrequest) {
		this.idrequest = idrequest;
	}
/**
 * return MyFile version of the file
 * @return MyFile version of the file
 */
	public MyFile getMyFile() {
		MyFile msg = new MyFile(path);
		String LocalfilePath = path;

		try {
			File newFile = new File(LocalfilePath);
			byte[] mybytearray = new byte[(int) newFile.length()];
			FileInputStream fis = new FileInputStream(newFile);
			BufferedInputStream bis = new BufferedInputStream(fis);

			msg.initArray(mybytearray.length);
			msg.setSize(mybytearray.length);

			bis.read(msg.getMybytearray(), 0, mybytearray.length);
		} catch (Exception e) {
			System.out.println("Error send (Files)msg) to Server");
		}
		return msg;
	}

}
