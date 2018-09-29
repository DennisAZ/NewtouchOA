package com.psit.struts.util.format;

import java.io.UnsupportedEncodingException;
/**
 * 字符类型转换 <br>
 */
public class TransStr 
{	
	/**
	 * 
	 * 字符类型转换方法 <br>
	 * create_date: Aug 23, 2010,2:12:18 PM <br>
	 * @param str 需要转换的数据
	 * @return String 返回转换后的数据<br>
	 */
	public static String transStr(String str)
	{
		if(str!=null){
			try {
				str=(new String(str.getBytes("iso-8859-1"),"UTF-8")).trim();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return str;
	}
	

}
