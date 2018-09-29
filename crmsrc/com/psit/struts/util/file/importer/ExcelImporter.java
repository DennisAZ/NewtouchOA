package com.psit.struts.util.file.importer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.fileupload.FileItem;

import jxl.Sheet;
import jxl.Workbook;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import com.psit.struts.util.format.StringFormat;

public class ExcelImporter extends FileImporter {
	private Workbook wk = null;
	private ExcelToEntity excelToE;
	
	public ExcelImporter(ExcelToEntity excelToE){
		this.excelToE = excelToE;
	}
	
	public String analyzeFile(FileItem fi) throws Exception{
		String rsStr = "";
		InputStream is = fi.getInputStream();
		wk = Workbook.getWorkbook(is);
		/* 读取xls */
		Sheet sheet = wk.getSheet(0);
		String[][] excelData = new String[sheet.getRows()-1][sheet.getColumns()];
		for(int i=0; i < excelData.length; i++){
			for(int j = 0; j<excelData[i].length ; j++){
				excelData[i][j]= StringFormat.removeBlank(sheet.getCell(j,i+1).getContents());//去空格
			}
		}
		if(excelToE.convert(excelData) && excelToE.getFilePath()!=null){
			saveFile(excelToE.getFilePath());
		}
		rsStr = excelToE.getResult();
		wk.close();
		is.close();
		return rsStr;
	}
	
	/**
	 * 保存导入文件 <br>
	 * @param filePath	文件路径数组
	 * @throws IOException
	 * @throws WriteException
	 */
	private void saveFile(String[] filePath) throws IOException, WriteException{
		String realRootPath = contextPath + ROOT_FOLDER;
		/* 删除原excel文件 */
		if(filePath[2] != null && !filePath[2].equals("")){
			File file = new File(realRootPath + filePath[2]);
			if (file.exists() && file.isFile()) {
				file.delete();// 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
			}
		}
		File outFolder = new File(realRootPath + filePath[0]);
		if (!outFolder.isDirectory()) {
			outFolder.mkdirs();
		}
		/* 写入文件 */
		File outFile = new File(realRootPath + filePath[1]);
		WritableWorkbook wwb = Workbook.createWorkbook(outFile,wk);
		wwb.write();
		wwb.close();
	}
}
