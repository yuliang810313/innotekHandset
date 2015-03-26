package com.innotek.handset.entities;

public class PhotoInfo {
	private String FileName = null;
	private String FilePath = null;
	
	public PhotoInfo(String FileName, String FilePath) {
		this.FileName = FileName;
		this.FilePath = FilePath;
	}
	
	public String getFileName() {
		return FileName;
	}
	public void setFileName(String fileName) {
		FileName = fileName;
	}
	public String getFilePath() {
		return FilePath;
	}
	public void setFilePath(String filePath) {
		FilePath = filePath;
	}
}
