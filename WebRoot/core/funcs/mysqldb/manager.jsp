<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  String action = request.getParameter("actions");
  String tables = request.getParameter("tables");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<title>操作界面</title>
<script type="text/javascript">
function doInit(){
  var url = contextPath + "/yh/core/funcs/mysqldb/act/YHMySqldbAct/doaction.act";
  var param = "action=<%=action%>&tables=<%=tables%>";
  var rtJson = getJsonRs(url,param);
  if(rtJson.rtState == "0"){
    listMsrg(rtJson.rtData);
  }else{
    alert(rtJson.rtMsrg);
  }
}

function listMsrg(dataArray){
  var table = $('mrsgTab');
  for(var i = 0; i < dataArray.length ; i ++ ){
    var obj = dataArray[i];
    var html = "<tr class=\"TableData\">"
      + "<td nowrap width=\"150\">" + obj.tableName + "</td>"
      + "<td nowrap align=\"center\" width=\"60\">" + obj.op + "</td>"
      + "<td nowrap align=\"center\" width=\"100\">" + obj.msgType + "</td>"
      + "<td nowrap>" + obj.msgText + "</td>"
      + "</tr>";
    table.insert(html,'bottom');
  }
}
</script>
</head>
<body topmargin="5" onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/sys_config.gif" align="absmiddle"><span class="big3"> 数据库管理</span></td>
    <td><input type="button" class="BigButton" onclick="history.back()" value="返回"></td>
  </tr>
</table>

<table id="mrsgTab" class="TableList" width="450" align="center">
  <tr class="TableHeader">
      <td nowrap align="center">表名</td>
      <td nowrap align="center">操作</td>
      <td nowrap align="center">消息类型</td>
      <td nowrap align="center">消息内容</td>
  </tr>
</table>
</body>
</html>