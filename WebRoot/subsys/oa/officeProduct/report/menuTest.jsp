<%@ page contentType="application/msexcel;charset=UTF-8" %>  
<%   
  //独立打开excel软件   
  response.setHeader("Content-disposition","attachment; filename=%E6%8A%A5%E8%A1%A8.xls");   
%>  
<html>  
<head>  
<title>测试导出Excel和Word</title>  
</head>  
<body> 
<% out.print(request.getParameter("table")); %> 
</body>  
</html>