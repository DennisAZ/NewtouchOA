<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<%@page import="java.text.SimpleDateFormat"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
String seqId = request.getParameter("seqId") == null ? "" :  request.getParameter("seqId");
String paperId = request.getParameter("paperId") == null ? "" :  request.getParameter("paperId");
%>
<html>
<head>
<title>考试成绩查询</title>
<link rel="stylesheet" href="<%=cssPath%>/cmp/tab.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath %>/core/js/cmp/select.js" ></script>
<script type="text/Javascript" src="<%=contextPath %>/core/js/orgselect.js" ></script>
<script type="text/javascript">
var seqId = "<%=seqId%>";
var paperId = "<%=paperId%>";
function doInit() {
  var url = "<%=contextPath %>/yh/subsys/oa/examManage/act/YHExamFlowAct/showMan2.act?seqId=" + seqId + "&paperId=" + paperId;
  var json = getJsonRs(url);
  if (json.rtState == "1") {
    alert(json.rtState);
    return;
  }
  var prcs = json.rtData;
  if (prcs.length > 0) {
    var table = new Element('table',{"class":"TableList","align":"center","cellspacing":"0","cellpadding":"3","width":"100%" }).update("<tbody id='tbody'><tr class='TableHeader' align='center'>"
        + "<td align='center' width='120'>参加考试人员</td>"
        + "<td align='center' width='120'>部门</td>"
        + "<td width='120'>角色</td>"
        + "<td nowrap align='center'>成绩</td>"
        + "<td nowrap align='center'>操作</td>"
        + "</tr><tbody>");
    $('listDiv').appendChild(table); 
    for(var i = 0; i < prcs.length; i++) {
      var prc = prcs[i];
      var participant = prc.participant;
      var flowTitle = prc.flowTitle;
      var rankman = prc.rankman;
      var flowFlag = prc.flowFlag;
      var flowDesc = prc.flowDesc;
      var tr = new Element('tr',{"align":"center","title":"参加考试人员","class":"TableData"});
      $('tbody').appendChild(tr);
         tr.update("<td>" + participant + "</td>"
               + "<td>" + flowTitle + "</td>"
               + "<td>" + rankman + "</td>"
               + "<td>" + flowFlag + "</td>"
               + "<td><a href=javascript:showList(" + seqId + "," + flowDesc + "," + paperId + ");>查卷</a></td>");
    }
  } else {
    var table = new Element('table',{ "class":"MessageBox" ,"align":"center","width":"340" }).update("<tr>"
        + "<td class='msg info'><div class='content' style='font-size:12pt'>无考试人员！</div></td></tr>");
    $('listDiv').appendChild(table); 
  }
}
function showList(flowId,userId,paperId) {
  window.location.href = "<%=contextPath%>/subsys/oa/examManage/examOnline/queryExam/scoreIndex.jsp?flowId=" + flowId + "&userId=" + userId + "&paperId=" + paperId;
}
</script>
</head>
<body class="bodycolor" topmargin="5px" onLoad="doInit();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/notify_open.gif" align="absmiddle"><span class="big3"> 试卷查询</span>
      &nbsp; &nbsp;
      <input type="button"  value="关闭" class="BigButton" onClick="window.close();">
    </td>
    </tr>
</table>
<br>
<div id="listDiv"></div>
</body>
</html>