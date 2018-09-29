<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<%@page import="java.text.SimpleDateFormat"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
  String participant = request.getParameter("participant") == null ? "" :  request.getParameter("participant");
%>
<html>
<head>
<title>被考核人员列表</title>
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
var participant = "<%=participant%>";
function doInit() {
  var url = "<%=contextPath %>/yh/subsys/oa/examManage/act/YHExamFlowAct/showMan.act?participant=" + participant;
  var json = getJsonRs(url);
  if(json.rtState == "1") {
    alert(json.rtState);
    return;
  }
  var prcs = json.rtData;
  if (prcs.length > 0) {
    var table = new Element('table',{"class":"TableList","align":"center","cellspacing":"0","cellpadding":"3" }).update("<tbody id='tbody'><tr class='TableHeader' align='center'>"
        + "<td align='center' width='120'>被考核人员</td>"
        + "<td align='center' width='120'>部门</td>"
        + "<td width='120'>角色</td></tr><tbody>");
    $('listDiv').appendChild(table); 
    for(var i = 0; i < prcs.length; i++) {
      var prc = prcs[i];
      var userName = prc.userName;
      var deptId = prc.userId;
      var userPriv = prc.userPriv; 
      var tr = new Element('tr',{"align":"center","title":"被考核人员","class":"TableData"});
      $('tbody').appendChild(tr);
         tr.update("<td>" + userName + "</td>"
               + "<td>" + deptId + "</td>"
               + "<td>" + userPriv + "</td>");
    }
    var tr2 = new Element('tr',{"align":"center","title":"关闭","class":"TableControl"});
    $('tbody').appendChild(tr2);
    tr2.update("<td colspan='3'><input class='BigButton' type='button' value='关闭' onClick='window.close();'></td>");
  } else {
    var table = new Element('table',{"class":"TableList","align":"center","cellspacing":"0","cellpadding":"3" }).update("<tbody id='tbody'><tr class='TableHeader' align='center'>"
        + "<td align='center' width='120'>被考核人员</td>"
        + "<td align='center' width='120'>部门</td>"
        + "<td width='120'>角色</td></tr><tbody>");
    $('listDiv').appendChild(table); 
  }
}
</script>
</head>
<body class="bodycolor" topmargin="5px" onLoad="doInit();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/hrms.gif" align="absmiddle"><span class="big3">&nbsp;被考核人员</span><br>
    </td>
    </tr>
</table>
<br>
<div id="listDiv"></div>
</body>
</html>