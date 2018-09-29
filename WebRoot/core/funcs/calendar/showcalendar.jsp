<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  List<Map<String,String>> calendarList = (List<Map<String,String>>)request.getAttribute("calendarList");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>日程安排查询</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css">
<style type="text/css">
           html {
                   overflow:auto;  /*这个可以去掉IE6,7的滚动*/
                   _overflow-x:hidden;/*去掉IE6横向滚动*/
                }
</style>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript">
function my_note(seqId,status){
  var URL = "<%=contextPath%>/core/funcs/calendar/mynote.jsp?seqId="+seqId+"&status="+status;
  window.open(URL,"my_note","height=200,width=250,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,left=500,top=300,resizable=yes");
}
</script>
</head>
<body class="" topmargin="5" >
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/infofind.gif" align="absmiddle"><span class="big3"> 日程安排查询结果</span><br>
    </td>
  </tr>
</table>
<%
  if(calendarList!=null){
    if(calendarList.size()>0){
     
%>
<table class="TableList" width="95%" align="center">
    <thead class="TableHeader">
      <td nowrap align="center">开始时间</td>
      <td nowrap align="center">结束时间</td>
      <td nowrap align="center">事务类型</td>
      <td nowrap align="center">事务内容</td>
      <td nowrap align="center">安排人</td>
   </thead>
<% for(int i=0; i<calendarList.size();i++){ 
     Map<String,String> map = new HashMap<String,String>();
     map = calendarList.get(i);
     String status = map.get("status");
     String[] calLevelNames = {"未指定","重要/紧急","重要/不紧急","不重要/紧急","不重要/不紧急"}; 
     String calTypeName = "工作事务";
     String calLevelStr = map.get("calLevel");
     int calLevelInt = 0;
     if(calLevelStr!=null&&!calLevelStr.trim().equals("")){
       calLevelInt = Integer.parseInt(calLevelStr);
     }
     if(map.get("calType")!=null&&map.get("calType").equals("2")){
       calTypeName = "个人事务";
     }
     if(map.get("overStatus")!=null&&map.get("overStatus").equals("1")){
%>
   <tr class="TableData">
      <td nowrap align="center"><%=map.get("calTime") %></td>
      <td nowrap align="center"><%=map.get("endTime") %></td>
      <td nowrap align="center"><%=calTypeName %></td>
      <td title="状态：已完成"><span class="CalLevel<%=map.get("calLevel") %>" title="<%=calLevelNames[calLevelInt] %>">&nbsp</span><a href="javascript:my_note(<%=map.get("seqId") %>,<%=map.get("overStatus") %>);" style="color:#00AA00;"><%=map.get("content") %></a></td>
      <td nowrap align="center"><%=map.get("managerName") %></td>
    </tr>

<% 
       }else{
         if(status!=null&&status.equals("0")){
%>
     <tr class="TableData">
      <td nowrap align="center"><%=map.get("calTime") %></td>
      <td nowrap align="center"><%=map.get("endTime") %></td>
      <td nowrap align="center"><%=calTypeName %></td>
      <td title="状态：进行中"><span class="CalLevel<%=map.get("calLevel") %>" title="<%=calLevelNames[calLevelInt] %>">&nbsp</span><a href="javascript:my_note(<%=map.get("seqId") %>,<%=map.get("status") %>);" style="color:#0000FF;"><%=map.get("content") %></a></td>
      <td nowrap align="center"><%=map.get("managerName") %></td>
    </tr>      
<%
         }
         if(status!=null&&status.equals("1")){
%>
   <tr class="TableData">
      <td nowrap align="center"><%=map.get("calTime") %></td>
      <td nowrap align="center"><%=map.get("endTime") %></td>
      <td nowrap align="center"><%=calTypeName %></td>
      <td title="状态：未开始"><span class="CalLevel<%=map.get("calLevel") %>" title="<%=calLevelNames[calLevelInt] %>">&nbsp</span><a href="javascript:my_note(<%=map.get("seqId") %>,<%=map.get("status") %>);" style="color:#0000FF;"><%=map.get("content") %></a></td>
      <td nowrap align="center"><%=map.get("managerName") %></td>
    </tr>
<%          
         }
         if(status!=null&&status.equals("2")){
%>
    <tr class="TableData">
         <td nowrap align="center"><%=map.get("calTime") %></td>
      <td nowrap align="center"><%=map.get("endTime") %></td>
      <td nowrap align="center"><%=calTypeName %></td>
      <td title="状态：已超时"><span class="CalLevel<%=map.get("calLevel") %>" title="<%=calLevelNames[calLevelInt] %>">&nbsp</span><a href="javascript:my_note(<%=map.get("seqId") %>,<%=map.get("status") %>);" style="color:#FF0000;"><%=map.get("content") +map.get("seqId")%></a></td>
      <td nowrap align="center"><%=map.get("managerName") %></td>
    </tr>
<%           
         }
%>

<%
       }
   }
%>
</table>
<%
    }else{
%>
<table class="MessageBox" align="center" width="340">
  <tr>
    <td class="msg info">
      <div class="content" style="font-size:12pt">无符合条件的日程安排</div>
    </td>
  </tr>
</table>
<%
    }
  }else{
%>
<table class="MessageBox" align="center" width="340">
  <tr>
    <td class="msg info">
      <div class="content" style="font-size:12pt">无符合条件的日程安排</div>
    </td>
  </tr>
</table>
<%
  }
%>
<br>
<center>
<input type="button" value="打印" class="BigButton" onclick="document.execCommand('Print');">
<input type="button" class="BigButton" value="返回" onclick=" history.go(-1);"></center>
</body>

</html>