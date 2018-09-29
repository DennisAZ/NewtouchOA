<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%@ include file="/core/inc/header.jsp" %>
 <%
  String seqId = request.getParameter("seqId");
  if (seqId == null) {
	  seqId = "";
  }
%>
<html>
<head>
<title>公告通知发布情况</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link rel="stylesheet" href="<%=cssPath %>/page.css">
<link rel="stylesheet" href ="<%=cssPath %>/style.css">
<link rel="stylesheet" href ="<%=cssPath %>/cmp/tab.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript">
function doInit(){
	var seqId = "<%=seqId%>";
   var url =contextPath+'/yh/core/funcs/notify/act/YHNotifyHandleAct/showObject.act?seqId='+seqId;
   var json = getJsonRs(url);
	 if(json.rtState == "0"){
		var rtData = json.rtData;
		var toName = rtData.toName;
		var userName = rtData.userName;
		var privName = rtData.privName;
		if(toName||userName||privName) {
           $('toName').update("&nbsp;"+toName);
           $('userName').update("&nbsp;"+userName);
           $('privName').update("&nbsp;"+privName);
	    }else {
           $('noData').style.display = "";
		}
	 }else{
		    document.body.innerHTML = json.rtMsrg;
	 }
}
</script>
</head>
<body onload="doInit();" topmargin="3">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/notify.gif" align="absmiddle"><span class="big3"> 发布情况</span>
    </td>
    </tr>
</table>
<div id="hasData">
 <table class="TableBlock" width="100%" align="center">
    <tr>
      <td nowrap class="TableData" width="130"> <font color="#0000FF"><b>发布范围(部门)：</b></font></td>
      <td class="TableData" id="toName">&nbsp;</td>
    </tr>
    <tr>
      <td nowrap class="TableData" width="130"> <font color="#0000FF"><b>发布范围(人员)：</b></font></td>
      <td class="TableData" id="userName">&nbsp;</td>
    </tr>
    <tr>
      <td nowrap class="TableData" width="130"> <font color="#0000FF"><b>发布范围(角色)：</b></font></td>
      <td class="TableData" id="privName">&nbsp;</td>
    </tr>
   </table>
</div>
 <div id="noData" style="display:none">
 <TABLE class=MessageBox width=280 align=center>
<TBODY>
<TR>
<TD class="msg info">
<DIV class=content 
style="FONT-SIZE: 12pt">无发布范围</DIV></TD></TR></TBODY></TABLE>
</div>
</div>
<br>
<center><input type="button" value="关闭" class="BigButton" onClick="window.close();"></center>
</body>
</html>