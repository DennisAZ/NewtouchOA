<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%
  String seqId = request.getParameter("seqId") == null ? "" : request.getParameter("seqId") ;
%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href = "<%=cssPath%>/cmp/Calendar.css">
<link rel="stylesheet" href="<%=cssPath %>/page.css">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/system/wordmodel/js/wordLogic.js"></script>
<title>上传成功</title>
</head>
<script type="text/javascript">
 var id = "<%=seqId%>"; 
 function doInit(){
   if(id){

    }
 }
 function goback(){
   var url = contextPath + '/core/funcs/system/wordmodel/addModel.jsp?';
   if(id){
     url += "seqId=" + id;
   }
   window.location= url;
 }
</script>
<body>
<table class="MessageBox" align="center" width="270">
  <tr>
    <td class="msg info">
      <div class="content" style="font-size:12pt">模板上传成功!</div>
    </td>
  </tr>
</table>
<table border="0" width="15%" cellspacing="0" cellpadding="3" class="small" align="center">
  <tr>
    <td class="Big">
    	 <input type="button" class="BigButton" value="继续上传" onclick="window.location='<%=contextPath %>/core/funcs/system/wordmodel/addModel.jsp';">
    </td>
    <!-- <td align="right">
    	 <input type="button" class="BigButton" value="返回" onclick="goback()">
    </td> -->
      <td align="right">
       <input type="button" class="BigButtonC" value="返回列表页面" onclick="window.location='<%=contextPath %>/core/funcs/system/wordmodel/index.jsp';">
    </td>
 </tr>
</table>

</body>
</html>