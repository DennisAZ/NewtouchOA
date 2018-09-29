<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
String feedId = request.getParameter("feedId");

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>会签意见(手写或签章)</title>
<link rel="stylesheet" href ="<%=cssPath %>/style.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/javascript">
var feedId = '<%=feedId%>';
function doInit(){
  var url =  contextPath + "/yh/core/funcs/workflow/act/YHFeedbackAct/getSignData.act";
  var json = getJsonRs(url , "feedId=" + feedId);
  if(json.rtState == '0'){
    var data = json.rtData;
    LoadSignDataSign(data);
  }
}
function LoadSignDataSign(data) {
  try {
    var DWebSignSeal=document.getElementById("DWebSignSeal");
    DWebSignSeal.SetStoreData(data);
    DWebSignSeal.ShowWebSeals();
  
    var strObjectName ;
    strObjectName = DWebSignSeal.FindSeal("",0);
    while(strObjectName) {
      DWebSignSeal.SetSealSignData (strObjectName,"中国兵器工业信息中心");
      DWebSignSeal.SetMenuItem(strObjectName,4);
      strObjectName = DWebSignSeal.FindSeal(strObjectName,0);
    }
  } catch (e) {
  }
}
function myClose () {
  close();
}
</script>
</head>
<body onload="doInit()">

<%@ include file="/core/funcs/workflow/websign/ver.jsp" %>
<table style="position:absolute;bottom: 5px;padding-right:5px" width=98%><tr><td align='center'><input type=button style="text-align:center;" value=关闭 class="SmallButtonW" onclick="myClose()"/></td></tr></table>
</body>
</html>