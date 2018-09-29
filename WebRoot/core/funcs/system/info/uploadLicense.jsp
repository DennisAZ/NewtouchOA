<%@ page language="java" import="java.util.*,java.io.*" pageEncoding="utf-8"%>
<%@ page import="org.apache.commons.fileupload.FileItem" %>
<%@ page import="yh.core.funcs.system.info.act.YHInfoAct" %>
<%@page import="yh.core.util.ReloadLicenseUtil"%>
<%@ include file="/core/inc/header.jsp" %> 
<jsp:useBean id="factory" scope="page" class="org.apache.commons.fileupload.disk.DiskFileItemFactory" />
<jsp:useBean id="upload" scope="page" class="org.apache.commons.fileupload.servlet.ServletFileUpload" />
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <base href="<%=basePath%>">
    
    <title>上传文件</title>
    
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<base target="_self">
<link rel="stylesheet" href="/yh/core/styles/style1/css/style.css">
<!--
<link rel="stylesheet" type="text/css" href="styles.css">
-->
<script type="text/javascript" language="javascript">

    </script>
</head>

<body>
   <% 
     request.setCharacterEncoding("UTF-8"); 
	 String rpath = getServletContext().getRealPath("/") + YHInfoAct.LICENSE_FILE_PATH; //上传文件路径
   
     File file = new File(rpath); 
	if(!file.exists()){ 
	     file.mkdirs(); 
	} 
     factory.setRepository(file); 
     factory.setSizeThreshold(1024*1024); 
     
     upload.setFileItemFactory(factory);
        
     try{ 
     List<FileItem> list= upload.parseRequest(request); 

     for(FileItem item:list){ 
      if(item.isFormField()){ 
        //String value=item.getString("UTF-8"); 
        //session.setAttribute("fileName", value);
        request.setAttribute(item.getFieldName(),item.getString("UTF-8")) ;       
      }else{ 
        String value=item.getName(); 
        int start=value.lastIndexOf("\\"); 
        String fileName=value.substring(start+1); 

        String attachRName=fileName;
        if(fileName.length() == 0 || fileName.equals("")){ 
            continue;
        }else{        
         if(item.getSize()>10000000){
           out.println("<script>alert('对不起，您上传的文件超过10M,无法完成上传！');window.close();</script>"); 
         }else{  
           //System.out.println(item.getName()+"---"+fileName);
           		String license_file_path = getServletContext().getRealPath("/") + YHInfoAct.LICENSE_FILE_PATH;
			    File file1 = new File(license_file_path);
			    File[] files = file1.listFiles();
		        for(int i = 0;i<files.length;i++){
		     		if(files[i].isFile()){
    		         String fileName1 = files[i].getName();
    		       if(fileName1.contains("license_") && fileName1.contains(".lic")){
    		      		files[i].delete();
    		      		break;
    		       }
    		      }
		        }
      			item.write(new java.io.File(rpath,attachRName));
        		ReloadLicenseUtil reloadlicenseutil = new ReloadLicenseUtil();
        		reloadlicenseutil.licenseIsExistByPage(attachRName);
            }
         }        
      }
}
  
     %>
      <div id="msrg"><table class="MessageBox" align="center" width="290">
     <tbody><tr>  <td class="msg info">
     <div class="content" style="font-size:12pt">
      上传成功!&nbsp;&nbsp;&nbsp;&nbsp;
      <%if(ReloadLicenseUtil.sysLinsencePageErrorFlag){%>
    	
    <a href="javascript:parent.location.href='<%=contextPath%>/errorLinsence.jsp';">请重新刷新页面</a>
    	
   <%}else{%>
      <a href="javascript:parent.location.href='<%=contextPath %>/yhindex.jsp';">请重新刷新页面</a>
      <%} %>
</div> </td> </tr> </tbody></table></div>
    
    <%}catch (Exception e){
       e.printStackTrace();

    }
   
   %>
</body>
</html>