<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*,yh.core.funcs.person.data.YHPerson" %>
<%@ include file="/core/inc/header.jsp" %>
<%
  String beginDate = request.getParameter("beginDate");
  if (beginDate == null) {
    beginDate = "";
  }
  String endDate = request.getParameter("endDate");
  if (endDate == null) {
    endDate = "";
  }

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>手机短信统计</title>
<link rel="stylesheet" href="<%=cssPath%>/views.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath%>/style.css" type="text/css" />
<link rel="stylesheet" href ="<%=cssPath%>/style.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/grid.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/system/mobilesms/js/mobilesmsLogic.js"></script>
<script type="text/javascript">
var beginDate = "<%=beginDate%>";
var endDate = "<%=endDate%>";
function doInit() {
  var count = 0;
  var url =  contextPath + "/yh/core/funcs/system/mobilesms/act/YHMobileSmsAct/getReportDeptSearchList.act";
  var rtJson = getJsonRs(url, "beginDate=" + beginDate + "&endDate=" + endDate);
  if (rtJson.rtState == "0") {
    var table=new Element('table',{ "width":"100%","class":"TableList","align":"center"})
		.update("<tbody id='tbody'><tr class='TableHeader' align='center'>"
				+"<td nowrap width='200' align='center'>部门</td>"
				+"<td nowrap width='200' align='center'>发送成功</td>"				
				+"<td nowrap width='200' align='center'>未发送</td>"				
				+"<td nowrap width='200' align='center'>发送失败</td></tr><tbody>");
		$('listDiv').appendChild(table);
  	for(var i = 0; i < rtJson.rtData.length; i++){
      count++;
      var deptName = rtJson.rtData[i].deptName;
  	  var count1 = rtJson.rtData[i].count1;
  	  var count2 = rtJson.rtData[i].count2;
  	  var count3 = rtJson.rtData[i].count3;
      if(count%2 == 1){
        color = 'TableLine1';
      }else{
        color = 'TableLine2';
      }
  	  var tr=new Element('tr',{'class':color});			
			table.firstChild.appendChild(tr);
			tr.update("<td align='center'>"					
			  + deptName+"</td><td align='center'>"
				+ count1 + "</td><td align='center'>"					
				+ count2 + "</td><td align='center'>"					
				+ count3 + "</td>"					
			);
  	}
  }else{
   alert(rtJson.rtMsrg); 
  }
}


</script>
</head>
<body topmargin="5" onload="doInit();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/system.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3"> 手机短信发送统计(按部门)</span>
    </td>
  </tr>
</table>
<div id="listDiv" align="center"></div>
<div align="center">
 <input type="button"  value="返回" class="BigButton" onClick="window.history.back();">
</div>
</body>
</html>