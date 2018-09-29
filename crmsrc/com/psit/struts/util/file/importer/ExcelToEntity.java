package com.psit.struts.util.file.importer;

import java.util.ArrayList;
import java.util.List;

public abstract class ExcelToEntity {
	protected int errCode=0;
	protected int curRowNo=0;
	protected List[] dataList;
	protected String[] filePath=null;//0>saveFolder; 1>savePath; 2>oldPath;
	
	protected ExcelToEntity(){
		dataList = new List[3];
		for(int j=0; j<dataList.length; j++){
			dataList[j] = new ArrayList();
		}
	}
	
	/**
	 * 转换excel数据，结果保存在list数组内，转换成功返回true，失败返回false <br>
	 * @param excelData	excel数据(数组)
	 * @param dataList	存储转换后的数据
	 * @return boolean
	 */
	public abstract boolean convert(String[][] excelData);
	
	/**
	 * 根据errCode返回错误描述字符串 <br>
	 * @return String 
	 */
	public abstract String getResult();
	
	public List[] getDataList() {
		return dataList;
	}
	
	public int getErrCode() {
		return errCode;
	}

	public int getCurRowNo() {
		return curRowNo;
	}

	public String[] getFilePath() {
		return filePath;
	}
}
