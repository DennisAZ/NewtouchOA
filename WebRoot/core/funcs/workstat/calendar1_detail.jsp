<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
String startDate = request.getParameter("startDate"); 
String endDate = request.getParameter("endDate"); 
String userId=request.getParameter("userId");


%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>日程安排（所有）</title>
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
  var requestURL = "<%=contextPath%>/yh/core/funcs/workstat/act/YHWorkStatAct/getCalFinishAct.act";
  var param="userId=<%=userId%>&startDate=<%=startDate%>&endDate=<%=endDate%>&status=0";

  var rtJson = getJsonRs(requestURL,param); 
  if(rtJson.rtState == "1"){
    alert(rtJson.rtMsrg); 
    return ;
    }
  var prcsJson = rtJson.rtData;
  if(prcsJson.calendar.length>0){
    var table = new Element('table',{"class":"TableList" ,"width":"95%"}).update("<tbody id = 'tboday'><tr class='TableHeader'>"
      + "<td nowrap align='center'>开始时间</td>"
      + "<td nowrap align='center'>结束时间 <img border=0 src='<%=imgPath%>/arrow_down.gif' width='11' height='10'></td>"
      + "<td nowrap align='center'>事务内容</td>"
     );
    $('listDiv').appendChild(table); 
    for(var i =0;i< prcsJson.calendar.length;i++){
      var tr = new Element('tr',{"class":"TableData"});
      $('tboday').appendChild(tr);
      var prcs = prcsJson.calendar[i];
      var calTime = prcs.startDate;
      var endTime = prcs.endDate;
      var content = prcs.content;

      tr.update("<td align='center'>" + calTime.substr(0,19) + "</td>"
        + "<td align='center'>" + endTime.substr(0,19) + "</td>"
        + "<td nowrap align='center'><a style='color:#0000ff;'>" + content.substr(0,8) + "</a></td>"
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
<body class="" topmargin="5" onLoad="doOnload();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/views/attendance.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3">  &nbsp;日程安排（所有） </span><br>
    </td>
  </tr>
</table>
<br>
<div align="center" id="listDiv">
</div>
<div align="center" id="nullTable">
</div><br><br>
<div align="center">
  <input type="button"  value="关闭" class="BigButton" onClick="window.close();">
</div>
</body>
</html>