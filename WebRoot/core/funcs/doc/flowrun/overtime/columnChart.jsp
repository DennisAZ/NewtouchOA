<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
     <%@ page import="java.util.Map,java.util.Set" %>
     <%@ include file="/core/funcs/doc/workflowUtility/workflowheader.jsp" %>
 <%
Map map = (Map)request.getAttribute("flowData");
Map data = (Map)map.get("data");
StringBuffer sb = new StringBuffer();
Set<String> userSet = data.keySet();
for (String userId : userSet) {
  Map userData = (Map)data.get(userId);
  String userName = (String)userData.get("userName");
  Map map2 = (Map)userData.get("count");
  int timeoutCount = (Integer)map2.get("timeOutCount");
  sb.append("{text:'" + userName + "',");
  sb.append("value:" + timeoutCount +"},");
}
if (userSet.size() > 0 ) {
  sb.deleteCharAt(sb.length() - 1);
}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/core/inc/header.jsp" %>

<link rel="stylesheet" href = "<%=cssPath%>/cmp/Calendar.css">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<title>超时工作统计结果</title>
<script type="text/Javascript">
var objArray = [<%=sb.toString()%>];
function doInit(){
  for (var i = 0 ;i < objArray.length ;i ++ ) {
    if (objArray[i]) {
      addRow(objArray[i].text , objArray[i].value , i);
    }
  }
}

function addRow(name , value , i) {
  var className = "TableLine1";
  if (i%2 == 0) {
    className = "TableLine2";
  } 
  var tr = new Element('tr',{'class':className});
  var td = new Element('td' , {'align':'center','bgcolor':'#D3E5FA'}).update(name);
  var td2 = new Element('td' , {'align':'center','width':'50'}).update(value);
  tr.appendChild(td);
  tr.appendChild(td2);
  $('list').appendChild(tr);
}
function getColumnChartData(){
  return objArray;
}
</script>
</head>

<body onload="doInit()">
<div align="left">

<TABLE>
	<tr align="center">
		<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	</tr>
 <TR align="center">
 	<TD align="center" valign="top">
 	   <div style="width:850px;height:400px;">
       <object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000"
      id="columnCharing" width="800px" height="350px"
      codebase="http://fpdownload.macromedia.com/get/flashplayer/current/swflash.cab">
      <param name="movie" value="<%=contextPath %><%=moduleContextPath %>/workflowUtility/columnCharing.swf" />
      <param name="quality" value="high" />
      <param name="bgcolor" value="#869ca7" />
      <param name="allowScriptAccess" value="sameDomain" />
      <embed src="<%=contextPath %><%=moduleContextPath %>/workflowUtility/columnCharing.swf" quality="high" bgcolor="#869ca7"
        width="100%" height="100%" name="columnCharing" align="middle"
        play="true"
        loop="false"
        quality="high"
        allowScriptAccess="sameDomain"
        type="application/x-shockwave-flash"
        pluginspage="http://www.adobe.com/go/getflashplayer">
      </embed>
  </object>
      </div>
   </TD>
   <td align="center" valign="top">
     <table border="0" cellspacing="1" width="95%" class="TableBlock" cellpadding="3">
    <tbody id="list"></tbody>
   <tr>
		<td class="TableData" align="center" colspan="2"><input type="button" value="关闭" class="SmallButton" onclick="window.close()"></td>
	</tr>
   </table>
  
 </td>
  </tr>
</TABLE>
</div>

</body>
</html>