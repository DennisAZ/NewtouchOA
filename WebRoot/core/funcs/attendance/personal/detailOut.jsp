<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  String flowId = request.getParameter("flowId"); 
  String month = request.getParameter("month") == null ? "" :  request.getParameter("month");
  String year = request.getParameter("year") == null ? "" :  request.getParameter("year");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>>外出历史记录</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/ExchangeSelect1.0.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript">
var year = '<%=year%>';
var month = '<%=month%>';

function updateOut(seqId){
  window.location.href = "<%=contextPath%>/core/funcs/attendance/personal/editOut.jsp?seqId=" + seqId;
}
function returnBefore(){
  window.location.href = "<%=contextPath%>/core/funcs/attendance/personal/out.jsp";
}
function doOnload(){
  var requestURL = "<%=contextPath%>/yh/core/funcs/attendance/personal/act/YHAttendOutAct/selectHistoryOut.act";
  var rtJson = getJsonRs(requestURL, "year="+year+"&month="+month);
  if(rtJson.rtState == "1"){
    alert(rtJson.rtMsrg); 
    return ;
    }
  var prcsJson = rtJson.rtData;
  if(prcsJson.length>0){
    var table = new Element('table',{"class":"TableList" ,"width":"95%"}).update("<tbody id = 'tboday'><tr class='TableHeader'><td nowrap align='center'>申请时间</td>"
      + "<td nowrap align='center'>审批人员</td>"
      + "<td nowrap align='center'>外出原因</td>"
      + "<td nowrap align='center'>开始时间</td>"
      + "<td nowrap align='center'>结束时间</td>"
      + "<td nowrap align='center'>状态</td></tr></tbody>");
    $('listDiv').appendChild(table); 
    for(var i =0;i< prcsJson.length;i++){
      var tr = new Element('tr',{"class":"TableData"});
      $('tboday').appendChild(tr);
      var prcs = prcsJson[i];
      var seqId = prcs.seqId;
      var userId = prcs.userId;
      var leaderId = prcs.leaderId;
      var leaderName = prcs.leaderName;
      var outType = prcs.outType;
      var createDate = prcs.createDate;
      var submitTime = prcs.submitTime;
      var outTime1 = prcs.outTime1;
      var outTime2 = prcs.outTime2;
      var allow =prcs.allow;
      var status = prcs.status;
      var reason = prcs.reason;
      tr.update("<td nowrap align='center'>" + createDate + "</td>"
          + "<td nowrap align='center'>" + leaderName + "</td>"
          + "<td align='center'>" + outType + "</td>"
          + "<td nowrap align='center'>" + submitTime.substr(0,16) + "</td>"
          + "<td nowrap align='center'>" + submitTime.substr(0,11) +  outTime2 + "</td>"
          + "<td nowrap align='center'>已归来  &nbsp;&nbsp;"
          + "</td>");
    }
  }else{
    var table = new Element('table',{ "class":"MessageBox" ,"align":"center" ,"width":"240"}).update("<tr>"
        + "<td class='msg info'>"
        + "<div class='content' style='font-size:12pt'>无历史记录</td></tr>");
      $('nullTable').appendChild(table);
  }
}
</script>
</head>
<body class="" topmargin="5" onload = "doOnload();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/views/attendance.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3">  &nbsp;外出历史记录</span><br>
    </td>
  </tr>
</table>

<br>
<div id = "listDiv" align="center">
</div>
<div id = "nullTable" align="center">
</div>
<br><br>
<div align="center">
<input type="button" value="关闭" class="BigButton" onClick="window.close();" title="关闭窗口">
</div>
</body>
</html>