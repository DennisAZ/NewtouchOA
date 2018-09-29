package com.psit.struts.util.file.importer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class WebToEntity {
	protected int errCode=0;
	protected List dataList;
	protected String curFileName;
	
	protected WebToEntity(){
		dataList = new ArrayList();
	}
	
	/**
	 * 转换网页文本 <br>
	 * @param files	解压后的网页文件
	 * @throws IOException 
	 */
	public abstract String convert(File[] files) throws IOException;
	
	/**
	 * 根据errCode返回错误描述字符串 <br>
	 * @return String 
	 */
	public abstract String getResult();
	
	public int getErrCode() {
		return errCode;
	}

	public List getDataList() {
		return dataList;
	}

}
