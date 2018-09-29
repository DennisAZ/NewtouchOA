package oa.core.funcs.bbs.act;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import yh.core.global.YHSysProps;

public class BBSUtil {
	public static final String bbsPath=YHSysProps.getAttachPath()+"/";
	public static final String bbsAnonyKey="bbsAnony";
	public static final String bbsHoster="bbsHoster";
	public static final String bbsAuthor="bbsAuthor";
	 public  static Object getFieldValueByName(String fieldName, Object o) {
	        try { 
	            String firstLetter = fieldName.substring(0, 1).toUpperCase(); 
	            String getter = "get" + firstLetter + fieldName.substring(1); 
	            Method method = o.getClass().getMethod(getter, new Class[] {}); 
	            Object value = method.invoke(o, new Object[] {}); 
	            return value; 
	        } catch (Exception e) { 
	           e.printStackTrace();
	            return null; 
	        } 
	    }
	   
	   
	    private static String[] getFiledName(Object o){
	     Field[] fields=o.getClass().getDeclaredFields();
	        String[] fieldNames=new String[fields.length];
	     for(int i=0;i<fields.length;i++){
//	      System.out.println(fields[i].getType());
	      fieldNames[i]=fields[i].getName();
	     }
	     return fieldNames;
	    }
	   
	   
	    public static List getFiledsInfo(Object o){
	     Field[] fields=o.getClass().getDeclaredFields();
	        String[] fieldNames=new String[fields.length];
	        List list = new ArrayList();
	        Map infoMap=null;
	     for(int i=0;i<fields.length;i++){
	      infoMap = new HashMap();
	      infoMap.put("type", fields[i].getType().toString());
//	      System.out.println("type:"+fields[i].getType().toString());
	      infoMap.put("name", fields[i].getName());
//	      System.out.println("name:"+fields[i].getName());
	      infoMap.put("value", getFieldValueByName(fields[i].getName(), o));
//	      System.out.println("value:"+getFieldValueByName(fields[i].getName(), o));
	      list.add(infoMap);
	     }
	     return list;
	    }
	   
	   
	    public static  Object[] getFiledValues(Object o){
	     String[] fieldNames=getFiledName(o);
	     Object[] value=new Object[fieldNames.length];
	     for(int i=0;i<fieldNames.length;i++){
	      value[i]=getFieldValueByName(fieldNames[i], o);
	     }
	     return value;
	    }
	    public static boolean hasPriv(String a,String b){
	    	String [] A=a.split(",");
	    	String [] B=b.split(",");
	    	for(int i=0;i<A.length;i++){
	    		  for(int j=0;j<B.length;j++){
	    		  if(A[i].equalsIgnoreCase(B[j])){
	    		  return true;
	    		  }
	    		  }
	    		}
	    	return false;
	    }
	    
	    public static List  moveAttach(String orgId,String moveId,String orgName){
	    	List rt=new ArrayList();
	    	String [] A=split(orgId,",");
	    	String [] B=split(moveId,",");
	    	String [] C=split(orgName,"*");
	    	StringBuffer sb1=new StringBuffer();
	      	StringBuffer sb2=new StringBuffer();
	    	boolean b=false;
	    	for(int i=0;i<A.length;i++){
	    		b=false;
	    		if(A[i]==null || A[i].equals(""))
	    			continue;
	    		  for(int j=0;j<B.length;j++){
	    		  if(B[j]==null || B[j].equals(""))
	    			  continue;
	    		  if(A[i].equalsIgnoreCase(B[j])){
	    			  b=true;
	    		    break;
	    		  }
	    		  }
	    		  if(!b){
	    			  sb1.append(A[i]+",");
	    			  sb2.append(C[i]+"*");
	    		  }
	    		}
	    	rt.add(sb1.toString());
	    	rt.add(sb2.toString());
	    	return rt;
	    }
	    
	    public static String[] split(String original,String regex)
	    {
	      int startIndex = 0;           //取子串的起始位置
	      Vector v = new Vector();      //将结果数据先放入Vector中
	      String[] str = null;          //返回的结果字符串数组
	      int index = 0;                //存储取子串时起始位置
	      startIndex = original.indexOf(regex);       //获得匹配子串的位置
	//如果起始字符串的位置小于字符串的长度，则证明没有取到字符串末尾。
	//-1代表取到了末尾
	      while(startIndex < original.length() && startIndex != -1)
	      {
	        String temp = original.substring(index,startIndex);
//	        System.out.println(" " + startIndex);
	        v.addElement(temp);        //取子串
	        index = startIndex + regex.length();  //设置取子串的起始位置,向后移动一步
	        startIndex = original.indexOf(regex,startIndex + regex.length());//获得匹配子串的位置
	      }
	        v.addElement(original.substring(index + 1 - regex.length()));//取结束的子串
	        //将Vector对象转换成数组
	        str = new String[v.size()];
	        for(int i=0;i<v.size();i++ )
	        {
	          str[i] = (String)v.elementAt(i);
	         }
	        //返回生成的数组
	        return str;
	    }
	    public static String returnGif(String fileName){
	    	if(fileName==null || fileName.equals(""))
	    		return null;
	    	String []rv=split(fileName,".");
	    	int j=rv.length-1;
	    	 return rv[j];
	    }
	    public static String getEndWord(String fileName){
	    	if(fileName==null || fileName.equals(""))
	    		return null;
	    	String []rv=split(fileName,".");
	    	int j=rv.length-1;
	    	 return rv[j];
	    }

	    public static String getDateDir(){
	    	Date a=new Date();
	    	int m=a.getMonth()+1;
	      	int y=a.getYear();
	      	 y=y%100;
	    	String r="";
	    	if(y<10){
		    r+="0"+y;
		    }else{
		    	r+=y;
		    }
	    	
	    	if(m<10){
	    	 r+="0"+m;
	    	}else{
	    	  r+=m;
	    	}
	    	return r;
	    }
	    
	    public static String getUUID(){
	    	 UUID uuid = UUID.randomUUID();
	    	return  uuid.toString();
	    	 
	    }
	    public static String getSessionProperty(HttpServletRequest request,String propertyName){
	    	if(propertyName==null)
	    		return null;
	    	HttpSession s =request.getSession();

	    	Object o=s.getAttribute("LOGIN_USER");
			String rt=BBSUtil.getFieldValueByName(propertyName, o).toString();
			return rt;
		
	    }
	    public static int getContentByte(String content){
	    	if(content==null || content.equals("")){
	    		return 0;
	    	}
	    	return content.getBytes().length;
	    }
	    public static void main(String []arg){
	    	String commentIds="2wer你";
	    	
//	    	 System.out.println(commentIds.getBytes().length);
	    }
	    
}
