package com.psit.struts.util.file.importer;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.fileupload.FileItem;

import com.psit.struts.util.FileOperator;
import com.psit.struts.util.file.ZipUtils;

public class ZipImporter extends FileImporter {
	private final String ZIP_TEMP_FOLDER="zipTemp\\";
	private final String EXTRACT_FOLDER="unZip\\";
	private WebToEntity webToE;
	public ZipImporter(WebToEntity webToE){
		this.webToE = webToE;
	}
	
	public String analyzeFile(FileItem fi) throws Exception{ 
		String rsStr = "";
		String zipTempFolder = "";
		try {
			zipTempFolder=contextPath+ROOT_FOLDER+ZIP_TEMP_FOLDER+ new Date().getTime()+"\\";//上传文件存放临时路径
			String unZipTempFolder = zipTempFolder + EXTRACT_FOLDER;//解压文件存放路径
			String zipPath = zipTempFolder + new Date().getTime()+".zip";//上传压缩包路径
			/* 创建压缩包临时目录 */
			File unZipTempFile  = new File(unZipTempFolder);
			unZipTempFile.mkdirs();
			/* 保存导入zip */
			fi.write(new File(zipPath));
			/* 解压zip */
			ZipUtils.unZip(zipPath, unZipTempFolder);
			File[] unZipFiles = unZipTempFile.listFiles(new WebFileFilter());
			rsStr = webToE.convert(unZipFiles);
		} catch (IOException e) {
			e.printStackTrace();
			rsStr = e.toString();
		}
		delectZip(zipTempFolder);
		return rsStr;
	}
	
	public void delectZip(String zipTempFolder){
		FileOperator.delFile(zipTempFolder,null);
	}
}
/**
 * 网页文件过滤器 <br>
 */
class WebFileFilter implements FileFilter {
	public boolean accept(File file) {
		if(file.isFile() && (file.getName().endsWith(".htm") || file.getName().endsWith(".txt")|| file.getName().endsWith(".html"))){
			return true;
		}
		return false;
	}
	
}
