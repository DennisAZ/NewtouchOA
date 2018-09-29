<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page import="java.util.Map,java.util.Set" %>
<%@ include file="/core/funcs/doc/workflowUtility/workflowheader.jsp" %>
<%
String flowquery = request.getParameter("flowquery");
String bumenquery = request.getParameter("STATCS_MANNER_QUERY"); 
String starttime = request.getParameter("statrTime");
String endtime = request.getParameter("endTime");
String user = request.getParameter("user"); 
String dept = request.getParameter("dept");
String role = request.getParameter("role");



Map map = (Map)request.getAttribute("flowData");
Map data = (Map)map.get("data");
Map flowName =  (Map)map.get("flowName");
int workCount = 0 ;
int timeOutCount = 0 ;

String sortId = request.getParameter("sortId");
if (sortId == null) {
  sortId = "";
}
String skin = request.getParameter("skin");
if (skin == null) {
  skin = "";
}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>


<link rel="stylesheet" href = "<%=cssPath%>/cmp/Calendar.css">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<title>超时工作统计结果</title>
<script type="text/Javascript"><!--
var endtime = "<%=endtime%>";
var starttime = "<%=starttime%>";
var user = "<%=user%>";
var dept = "<%=dept%>";
var role = "<%=role%>";
var STATCS_MANNER_QUERY = "<%=bumenquery%>";
var flowquery = "<%=flowquery%>";
var sortId = "<%=sortId%>";
var skin = "<%=skin%>";

function viewDetail(userId , flowId) {
  var url = contextPath + "<%=moduleContextPath %>/flowrun/overtime/viewDetail.jsp?" 
     + "flowId=" + flowId
      + "&userId=" + userId
       + "&prcsDate1Query=" + starttime
       + "&prcsDate2Query=" + endtime
      + "&sortId=" + sortId
      + "&skin=" + skin;
  window.open(url);
}
function exportCsv() {
  var url = contextPath+'<%=moduleSrcPath %>/act/YHWorkOverTimeAct/getOverTimeTotal.act?';
  par = "flowquery="+ flowquery + "&statrTime="+starttime+"&endTime=" + endtime +"&STATCS_MANNER_QUERY="+ STATCS_MANNER_QUERY +"&user="+ user +"&dept="+dept+"&role=" + role + "&flag=2";
  window.open(url + par);
}
--></script>
</head>
<body>
<table id="flow_table" width="100%" class="TableList" align="center" style="border-bottom:0px;table-layout:fixed;display:<%= data.isEmpty() ? "none" : ""%>">
<tr class="TableHeader">
<td align=center nowrap>姓名</td>
<%
Set<String> flowNames = flowName.keySet();
for (String tmp : flowNames) {
  if (!"count".equals(tmp)) {
    %>
    <td align=center nowrap><%=(String)flowName.get(tmp) %></td>
    <% 
  }
}
%>
<td align=center nowrap>小计</td>
</tr>
<%
Set<String> userIds = data.keySet();
for (String userId : userIds) {
  %>
  <tr class="TableLine1">
  <% 
  Map userData = (Map)data.get(userId);
  %>
  <td align=center nowrap><%=(String)userData.get("userName")%></td>
  <% 
  for (String flowId : flowNames) {
    if (!"count".equals(flowId)) {
      String value = "0(0)";
      Map flowData =  (Map)userData.get(flowId);
      if (flowData != null) {
        int timeCountTmp = (Integer)flowData.get("timeOutCount");
        int workCountTmp = (Integer)flowData.get("workCount");
        value = timeCountTmp +  "(" + workCountTmp + ")";
        %>
        <td align=center nowrap><a onclick='viewDetail(<%=userId %>, <%=flowId %>)' href='javascript:void(0)'><%=value %></a></td>
        <%
      } else {
      %>
      <td align=center nowrap><%=value %></td>
      <% 
      }
    }
  }
  Map countData =  (Map)userData.get("count");
  String value =  "0(0)";
  if (countData != null ) {
    int timeCountTmp = (Integer)countData.get("timeOutCount");
    int workCountTmp = (Integer)countData.get("workCount");
    
    value = timeCountTmp +  "(" + workCountTmp + ")";
    workCount += workCountTmp ;
    timeOutCount += timeCountTmp ;
  } 
  %>
  <td align=center nowrap><%=value %></td>
  </tr>
  <% 
}
%>
<tr class="TableLine2">
<td align=center nowrap colspan="<%=flowName.size()%>"><b>总计</b></td>
<td align=center nowrap ><b><%=timeOutCount + "(" + workCount + ")"%></b></td>
</tr>
<tr class="TableControl">
<td align=left nowrap colspan="<%=flowName.size() + 1 %>">
<input type="button" value="导出 CSV" class="BigButton" onclick="exportCsv()"/>
</td>
</tr>
</table>
<table align="center">
<tr>
<td>
<center>
<input type="button"  style="display:<%= data.isEmpty() ? "none" : ""%>"  class="BigButton"  value="返回" onclick="javascript:window.location= contextPath + '<%=moduleContextPath %>/flowrun/overtime/overtotal.jsp?sortId=<%=sortId %>&skin=<%=skin %>'">
</center></td></tr>
  </table>
  <div id="noData" align=center style="display:<%= data.isEmpty() ? "" : "none"%>">
  <table class="MessageBox" width="300">
  <tbody>
  <tr>
  <td id="msgInfo" class="msg info"> 没有检索到数据
  </td>
  </tr>
  </tbody>
  </table>
  <div><input   class="BigButton"  type="button" value="返回 " class="Log_submit" onclick="javascript:window.location= contextPath + '<%=moduleContextPath %>/flowrun/overtime/overtotal.jsp?sortId=<%=sortId %>&skin=<%=skin %>'"/></div>
  </div>
</body>
</html>