<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  String month = request.getParameter("month") == null ? "" :  request.getParameter("month");
  String year = request.getParameter("year") == null ? "" :  request.getParameter("year");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>请假历史记录</title>
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

function returnBefore(){
  window.location.href = "<%=contextPath%>/core/funcs/attendance/personal/leave.jsp";
}
function updateLeave(seqId){
  window.location.href = "<%=contextPath%>/core/funcs/attendance/personal/editLeave.jsp?seqId=" + seqId;
}
function doOnload(){
  var requestURL = "<%=contextPath%>/yh/core/funcs/attendance/personal/act/YHAttendLeaveAct/selectHistroyLeave.act";
  var rtJson = getJsonRs(requestURL, "year="+year+"&month="+month);
  if(rtJson.rtState == "1"){
    alert(rtJson.rtMsrg); 
    return ;
    }
  var prcsJson = rtJson.rtData;
  if(prcsJson.length>0){
    var table = new Element('table',{"class":"TableList" ,"width":"95%"}).update("<tbody id = 'tboday'><tr class='TableHeader'><td nowrap align='center'>请假原因</td>"
      + "<td nowrap align='center'>审批人员</td>"
      + "<td nowrap align='center'>开始时间</td>"
      + "<td nowrap align='center'>结束时间</td>"
      //+ "<td nowrap align='center'>申请销假时间</td>"
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
      var leaveType = prcs.leaveType;
      var leaveDate1 = prcs.leaveDate1;
      var leaveDate2 = prcs.leaveDate2;
      var allow =prcs.allow;
      var status = prcs.status;
      var reason = prcs.reason;
      var destroyTime = prcs.destroyTime;
      var annualLeave = prcs.annualLeave;
      tr.update("<td align='left'>补假：" + leaveType + "</td>"
          + "<td nowrap align='center'>" + leaderName + "</td>"
          + "<td nowrap align='center'>" + leaveDate1 + "</td>"
          + "<td nowrap align='center'>" + leaveDate2 + "</td>"
          //+ "<td nowrap align='center'>" + destroyTime + "</td>"
          + "<td nowrap align='center'>已批准</td> "
       );
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
<body onload = "doOnload();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/views/attendance.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3">  &nbsp;请假登记</span><br>
    </td>
  </tr>
</table>
<br>
<div align="center" id="listDiv">
</div>
<div align="center" id="nullTable">
</div>
<br><br>
<div align="center">
 <input type="button" value="关闭" class="BigButton" onClick="window.close();" title="关闭窗口">
</div>
</body>
</html>