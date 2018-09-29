package com.psit.struts.util.file.importer;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;

public abstract class FileImporter {
	protected String contextPath = "";
	public static final String ROOT_FOLDER = "importFiles\\";
	public static final String TEMP_FOLDER = "temp\\";
	
	/**
	 * 得到上传文件 <br>
	 * @param request
	 * @throws FileUploadException 
	 */
	public List getUploadFile(HttpServletRequest request) throws FileUploadException{
		contextPath = request.getSession().getServletContext().getRealPath("/");
		String rootPath = contextPath+ROOT_FOLDER;//上传文件目录
		String tempPath = rootPath + TEMP_FOLDER;
		/* 创建文件夹 */
		File rootFilePath = new File(rootPath);
		File tempFilePath = new File(tempPath);
		if (!rootFilePath.isDirectory()) {
			rootFilePath.mkdir();
		}
		if (!tempFilePath.isDirectory()) {
			tempFilePath.mkdir();
		}
		/* 得到上传文件 */
		DiskFileUpload fu = new DiskFileUpload();
		fu.setHeaderEncoding("utf-8");// 转码
		fu.setSizeThreshold(1000 * 1024);// 设置缓冲区大小，这里是1M
		fu.setRepositoryPath(tempPath);// 设置临时目录
		List fileList = fu.parseRequest(request);
		return fileList;
	}
	/**
	 * 解析导入文件 <br>
	 * @param fi
	 * @return String 导入错误信息（导入成功为空）
	 */
	public abstract String analyzeFile(FileItem fi) throws Exception;
	
}
