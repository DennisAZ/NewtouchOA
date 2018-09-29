package com.psit.struts.util.file.importer;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;

/**
 * 导入操作Manager <br>
 */
public class ImporterManager {
	/**
	 * 上传和解析文件 <br>
	 * @param request
	 * @param fileImporter
	 * @return String
	 * @throws Exception
	 */
	private static String importFiles(HttpServletRequest request, FileImporter fileImporter) throws Exception{
		List fileList = fileImporter.getUploadFile(request);//上传文件
		Iterator it = fileList.iterator();
		if(it.hasNext()) {
			FileItem fi = (FileItem) it.next();
			return fileImporter.analyzeFile(fi);//解析文件
		}
		else{
			return "导入失败，未上传文件至服务器！";
		}
	}
	
	/**
	 * 导入web文件 <br>
	 * @param request
	 * @param wtEntity	
	 * @param isZip		是否打包导入
	 * @return String
	 * @throws Exception 
	 */
	public static String importWeb(HttpServletRequest request, WebToEntity wtEntity,boolean isZip) throws Exception{
		FileImporter fileImporter;
		if(isZip){
			fileImporter = new ZipImporter(wtEntity);
		}
		else{
			fileImporter = new WebImporter(wtEntity);
		}
		return importFiles(request, fileImporter);
	}
	
	/**
	 * 导入excel文件 <br>
	 * @param request
	 * @param etEntity
	 * @return String
	 * @throws Exception 
	 */
	public static String importExcel(HttpServletRequest request, ExcelToEntity etEntity) throws Exception{
		FileImporter fileImporter = new ExcelImporter(etEntity);
		return importFiles(request, fileImporter);
	}
}
