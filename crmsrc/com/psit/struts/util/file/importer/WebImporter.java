package com.psit.struts.util.file.importer;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.fileupload.FileItem;

import com.psit.struts.util.FileOperator;
import com.psit.struts.util.file.ZipUtils;

public class WebImporter extends FileImporter {
	private final String WEB_TEMP_FOLDER="webTemp\\";
	private WebToEntity webToE;
	public WebImporter(WebToEntity webToE){
		this.webToE = webToE;
	}
	
	public String analyzeFile(FileItem fi) throws Exception {
		String rsStr = "";

		String webTempFold = contextPath + ROOT_FOLDER + WEB_TEMP_FOLDER;//上传文件存放临时路径
		String webTempPath = webTempFold + fi.getSize() + new Date().getTime() + ".html";
		/* 创建临时目录 */
		File tempFolder = new File(webTempFold);
		if (!tempFolder.isDirectory()) {
			tempFolder.mkdir();
		}

		/* 保存导入网页文件 */
		File webFile = new File(webTempPath);
		fi.write(webFile);
		rsStr = webToE.convert(new File[] { webFile });
		delectTempWebFile(webTempPath);
		return rsStr;
	}

	public void delectTempWebFile(String webTempPath) {
		FileOperator.delFile(webTempPath, null);
	}
}
