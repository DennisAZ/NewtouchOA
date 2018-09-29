<%@ page language="java" import="java.util.*" pageEncoding="gb2312"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    
    <title>My JSP 'upload.jsp' starting page</title>
    

  </head>
  
  <body>

    <!--    <form action="/Shop/UploadServlet" method="post" enctype="multipart/form-data">-->
 <form action="uploadc.jsp" method="post" enctype="multipart/form-data">  
   <input type="file" name="file" size="20"/>
   <input type="submit" value="ÉÏ´«"/>
 </form> 
   <!--  <form action="/Shop/ServletUpload" method="post" enctype="multipart/form-data"> 
    username:<input type="text" name="username"><br>
    
    password:<input type="password" name="password"><br>
    
    file1: <input type="file" name="file1"><br>
    file2: <input type="file" name="file2"><br>
    
    <input type="submit" value="submit">
    </form>       -->
    
  </body>
</html>
