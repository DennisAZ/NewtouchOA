<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
String FLOW_ID = request.getParameter("flowId"); 
String USER_ID = request.getParameter("userId"); 
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>加班登记历史记录</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript">


function doOnload(){
  var requestURL = "<%=contextPath%>/yh/subsys/oa/hr/salary/report/act/YHHrReportAct/selectHistoryOvertime.act";
   var rtJson = getJsonRs(requestURL,"userId=<%=USER_ID%>&flowId=<%=FLOW_ID%>"); 
  if(rtJson.rtState == "1"){
    alert(rtJson.rtMsrg); 
    return ;
    }
  var prcsJson = rtJson.rtData;
  if(prcsJson.length>0){
    var table = new Element('table',{"class":"TableList" ,"width":"95%"}).update("<tbody id = 'tboday'><tr class='TableHeader'><td nowrap align='center'>申请时间</td>"
      + "<td nowrap align='center'>审批人员</td>"
      + "<td nowrap align='center'>加班原因</td>"
      + "<td nowrap align='center'>开始时间</td>"
      + "<td nowrap align='center'>结束时间</td>"
      + "<td nowrap align='center'>状态</td>"
    );
    $('overtime').appendChild(table); 
    for(var i =0;i< prcsJson.length;i++){
      var tr = new Element('tr');
      var prcs = prcsJson[i];
      var seqId = prcs.seqId;
      var userId = prcs.userId;
      var leaderId = prcs.leaderId;
      var leaderName = prcs.leaderName;
      var status = prcs.status;
      var reason = prcs.reason;
      var beginDate = prcs.beginTime.substr(0, 10) + " " + prcs.beginDate;
      var endDate = prcs.beginTime.substr(0, 10) + " " + prcs.endDate;
      var statusDesc = "已批准";
      var statusOptDesc = ""
      if(seqId){  
          tr.update("<td nowrap align='center'>" + prcs.overtimeTime + "</td>"
              + "<td nowrap align='center'>" + leaderName + "</td>"
              + "<td  align='center'>" + prcs.overtimeDesc + "</td>"
              + "<td nowrap align='center'>" + beginDate + "</td>"
              + "<td nowrap align='center'>" +  endDate + "</td>"
              + "<td nowrap align='center' title=" + statusDesc + "> " + statusDesc + " </td>"
           );
        }
      $('tboday').appendChild(tr);
    }
  }else{
    $("nullDiv").style.display = "";
  }
}
</script>
</head>
 
<body topmargin="5" onLoad="doOnload();">
 
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/views/attendance.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3">&nbsp;加班登记历史记录</span><br>
    </td>
  </tr>
</table>
 
<br>
<br></br>
<div id="overtime"></div>
<div id="nullDiv" style="display:none">
 
<table class="MessageBox" align="center" width="440">
  <tr>
    <td class="msg info">
      <div class="content" style="font-size:12pt">无符合加班历史记录！</div>
    </td>
  </tr>
</table>

</div>
<div align="center">    <br>   <input type="button" value="关闭" class="BigButton" onClick="window.close();">&nbsp;&nbsp; </div>
</body>
</html>