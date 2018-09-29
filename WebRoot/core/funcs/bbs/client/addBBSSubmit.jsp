<%@ page language="java" import="java.util.*,java.io.*" pageEncoding="utf-8"%>
<%@ page import="org.apache.commons.fileupload.FileItem" %>
<%@ page language="java" import="oa.core.funcs.bbs.act.BBSUtil,oa.core.funcs.bbs.act.BbsService" %>
<%@ page import="java.text.SimpleDateFormat,java.util.Date" %>

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

       String dateDir=BBSUtil.getDateDir();
     String rpath=BBSUtil.bbsPath+"bbs/"+dateDir;
   
     File file = new File(rpath); 
      StringBuffer attachIds=new StringBuffer();
      StringBuffer attachNames=new StringBuffer();
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
        String uuid=BBSUtil.getUUID();
        String attachId=dateDir+"_"+uuid;
        String attachRName=uuid+"."+fileName;
        if(fileName.length() == 0 || fileName.equals("")){ 
            continue;
        }else{        
         if(item.getSize()>10000000){
           out.println("<script>alert('对不起，您上传的文件超过10M,无法完成上传！');window.close();</script>"); 
         }else{
           SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss"); 
              Date date =new Date();
              String[] f = item.getName().split("\\\\");
              //System.out.println(f[f.length-1]);
              String oldFileName = f[f.length-1];      
           //System.out.println(item.getName()+"---"+fileName);
           item.write(new java.io.File(rpath,attachRName));
           attachIds.append(attachId+",");
           attachNames.append(fileName+"*");

         }
        }        
      }
        }
   String boardName=(request.getAttribute("boardName")).toString();
     String boardId=(request.getAttribute("boardId")).toString(); 
     String parent=(request.getAttribute("parent")).toString();
      int commentId=new BbsService().addComment(request,attachIds.toString(),attachNames.toString());
     if(!parent.equals("0")){
      commentId=Integer.parseInt(parent);
      
     }
     %>
      <div id="msrg"><table class="MessageBox" align="center" width="290">
     <tbody><tr>  <td class="msg info">
     <div class="content" style="font-size:12pt">
      提交成功!&nbsp;&nbsp;&nbsp;&nbsp;<a href='core/funcs/bbs/client/commentDetail.jsp?commentId=<%=commentId%>&&boardId=<%=boardId%>&&boardName=<%=boardName%>'>返回论坛</a>
</div> </td> </tr> </tbody></table></div>
    
    <%}catch (Exception e){
       e.printStackTrace();

    }
   
   %>
</body>
</html>