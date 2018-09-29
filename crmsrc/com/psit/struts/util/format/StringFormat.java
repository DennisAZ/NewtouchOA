package com.psit.struts.util.format;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 字符串格式处理 <br>
 */
public class StringFormat {
	public static String strEncode(String s) { 
		if(s!=null){
		    StringBuilder sb = new StringBuilder(s.length()+20); 
		   // sb.append('\"'); 
		    for (int i=0; i<s.length(); i++) { 
		        char c = s.charAt(i); 
		        switch (c) { 
		        case '\"':
		            sb.append("\\\""); 
		            break; 
		        case '\\': 
		            sb.append("\\\\"); 
		            break; 
		        case '/': 
		            sb.append("\\/"); 
		            break; 
		        case '\b': 
		            sb.append("\\b"); 
		            break; 
		        case '\f': 
		            sb.append("\\f"); 
		            break; 
		        case '\n': 
		            sb.append("\\n"); 
		            break; 
		        case '\r': 
		            sb.append("\\r"); 
		            break; 
		        case '\t': 
		            sb.append("\\t"); 
		            break; 
		        default: 
		            sb.append(c); 
		        } 
		    } 
		  //  sb.append('\"'); 
		    return sb.toString();
		}
		else{
			return s;
		}
	}
	/**
	 * 将回车、换行符转换为br标签 <br>
	 * @param str 原字符串
	 * @return String 转换后的字符串
	 */
	public static String toBR(String str){
		if(str!=null&&!str.equals("")){
			Pattern patternRight = Pattern.compile("\\r\n");
			str = patternRight.matcher(str).replaceAll("<br/>");
		}
		
		return str;
	}
	/**
	 * 将回车、换行符转换为空格 <br>
	 * @param str 原字符串
	 * @return String 转换后的字符串
	 */
	public static String toBlank(String str){
		if(str!=null&&!str.equals("")){
			Pattern patternRight = Pattern.compile("\\r\n");
			str = patternRight.matcher(str).replaceAll(" ");
		}
		
		return str;
	}

	public static String formatDouble(Double num){
		if(num==null){ num=0.0; }
		DecimalFormat df = new DecimalFormat("##0.00");
		return df.format(num);
	}
	
	/**
	 * 因关联数据删除失败提示信息格式化 <br>
	 * @param eMsg 原错误信息
	 * @param count 关联数据未删除记录数 
	 * @param recCount	关联数据已移至回收站记录数
	 * @param objName 删除对象名 
	 */
	public static void getDelEMsg(StringBuffer eMsg, int count, int recCount, String objName){
		eMsg.append("【"+objName+": ");
		eMsg.append((count>0)?count:"");
		eMsg.append((count>0 && recCount > 0)?"+":"");
		eMsg.append((recCount > 0)?recCount+"(回收站)":"");
		eMsg.append("】");
	}
	
	/**
	 * 去掉字符串中首尾空白符(全半角空格和换行回车等)及字符中的换行 <br>
	 * @param str 字符串
	 * @return String 处理后的字符串
	 */
	public static String removeBlank(String str){
		if(str!=null&&!str.equals("")){
			Pattern patternRight = Pattern.compile("^[\\u3000|\\s]+|[\\u3000|\\s]+$|[\\f\\n\\r\\t\\v]+");
			str = patternRight.matcher(str).replaceAll("");
		}
		return str;
	}
	
	/**
	 * 去掉字符串中所有空白符(全半角空格和换行回车等) <br>
	 * @param str 字符串
	 * @return String 处理后的字符串
	 */
	public static String removeAllBlank(String str){
		if(str!=null&&!str.equals("")){
			Pattern patternRight = Pattern.compile("[\\u3000|\\s]+");
			str = patternRight.matcher(str).replaceAll("");
		}
		return str;
	}
	
	/**
	 * 去掉最后一个分隔符 <br>
	 * @param str		原始字符串
	 * @param splitWord	分隔符
	 * @return String
	 */
	public static String removeLastSplitWord(String str, String splitWord){
		if(!StringFormat.isEmpty(str)&&!StringFormat.isEmpty(splitWord)&&(str.length()-1)==str.lastIndexOf(splitWord)){
			return str.substring(0,str.lastIndexOf(splitWord));
		}
		else{
			return str;
		}
	}
	
	/**
	 * 判断字符串是否为空（空格或回车） <br>
	 * @param str 字符串
	 * @return boolean 为空返回true，否则返回false
	 */
	public static boolean isEmpty(String str) {
		if (str!=null&&!str.isEmpty()) {
			Pattern patternRight = Pattern.compile("[\\u3000|\\s]+");
			return patternRight.matcher(str).matches();
		} else {
			return true;
		}
	}
	
	/**
	 * 判断字符串是否为数字 <br>
	 * @param str 字符串
	 * @return boolean 如果为数字则返回true，否则返回false
	 */
	public static boolean isNumeric(String str){
	    Pattern pattern = Pattern.compile("[0-9]*"); 
	    return pattern.matcher(str).matches();    
	 } 
	
	/**
	 * 截断字符串，如果字符串长度小于最大字符数则不截断 <br>
	 * @param str 字符串
	 * @param maxLength 最大字符数
	 * @return String 截断后的字符串
	 */
	public static String shortString(String str,int maxLength){
		if(str!=null&&!str.equals("")&& str.length() > maxLength){
			str = str.substring(0,maxLength);
		}
		return str;
	}

	/**
	 * 根据身份证号生成出生日期和年龄 <br>
	 * @param idNum		身份证号
	 * @return String[]	[0]出生日期(yy-MM-dd); [1]年龄;
	 */
	public static String[] getBirthByIdNum(String idNum){
		String[] bir = null;
		if(idNum!=null && !idNum.isEmpty()){
			idNum = idNum.trim();
			String year,month,day,age;
			if(idNum.length()==18){
				year = idNum.substring(6,10);
				month = idNum.substring(10,12);
				day = idNum.substring(12,14);
				age = Integer.toString(GetDate.getCurYear() - Integer.parseInt(year));
				bir = new String[]{year+"-"+month+"-"+day,age};
			}
			else if((idNum.length()==15)){
				year = "19"+idNum.substring(6,8);
				month = idNum.substring(8,10);
				day = idNum.substring(10,12);
				age = Integer.toString(GetDate.getCurYear() - Integer.parseInt(year));
				bir = new String[]{year+"-"+month+"-"+day,age};
			}
		}
		return bir;
	}
	
	/**
	 * 转换模板变量<br>
	 * @param templateStr 				待转换内容
	 * @param data						模板变量
	 * @param defaultNullReplaceVals	空数据替换字符
	 * @return String
	 */
	public static String parseTemplate(String templateStr,
			Map<String, String> data, String... defaultNullReplaceVals) {
		if (templateStr == null){
			return null;
		}
		if (data == null){
			data = Collections.EMPTY_MAP;
		}
		String nullReplaceVal = defaultNullReplaceVals.length > 0 ? defaultNullReplaceVals[0] : "";
		Pattern pattern = Pattern.compile("\\$\\{([^}]+)}");

		StringBuffer newValue = new StringBuffer();
		Matcher matcher = pattern.matcher(templateStr);
		while (matcher.find()) {
			String key = matcher.group(1);
			String r = data.get(key) != null ? data.get(key).toString() : nullReplaceVal;
			matcher.appendReplacement(newValue, r);  
		}
		matcher.appendTail(newValue);
		return newValue.toString();
	}   
}
