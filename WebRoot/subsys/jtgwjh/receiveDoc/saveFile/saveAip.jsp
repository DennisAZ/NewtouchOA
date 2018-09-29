<%@ page contentType="text/html;charset=utf-8" %>

<%@page import="java.io.File"%>
<%@page import="yh.core.util.file.YHFileUtility"%>
<jsp:useBean id="mySmartUpload" scope="page" class="com.jspsmart.upload.SmartUpload" />

<%
String filePath = request.getParameter("filePath")==null ? "" : request.getParameter("filePath");
String fileName = request.getParameter("fileName")==null ? "" : request.getParameter("fileName");
//System.out.println(filePath + ":" +fileName);
try{
	// 初始化上传组件
 	mySmartUpload.initialize(pageContext);
 	
	mySmartUpload.upload();
	File file = new File(filePath);
	if(!file.exists()){
	  file.mkdirs();
	}
	
	String FileName;
	com.jspsmart.upload.File myFile = null;
	myFile = mySmartUpload.getFiles().getFile(0);
	FileName = myFile.getFileName(); 
	System.out.println(filePath);
	if (!myFile.isMissing()){
	  myFile.saveAs(filePath);
	  
	  
		//myFile.saveAs(filePath,mySmartUpload.SAVE_VIRTUAL);
	}

	//mySmartUpload.save(filePath + "/" + FileName);
	out.clear();
	out.print("ok");
	out.flush();
	}catch(Exception e){
	  out.clear();
		out.print("failed");//返回控件HttpPost()方法值。
		out.flush();
		
	}%>