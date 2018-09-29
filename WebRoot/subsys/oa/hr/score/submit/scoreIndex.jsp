<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %> 
<%
  String flowId = request.getParameter("flowId") == null ? "" :  request.getParameter("flowId");
  String userId = request.getParameter("userId") == null ? "" :  request.getParameter("userId");
  String groupFlag = request.getParameter("groupFlag") == null ? "" :  request.getParameter("groupFlag");
  String checkFlag = request.getParameter("checkFlag") == null ? "" :  request.getParameter("checkFlag");
  String seqId = "";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>在线考试</title>
<link rel="stylesheet"	href="<%=cssPath%>/style.css">
<link rel="stylesheet" href = "<%=cssPath %>/style.css">
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath %>/cmp/ExchangeSelect.css" type="text/css" />
<script type="text/Javascript"	src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/Javascript"  src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/smartclient.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/cmp/select.js"></script>
<script type="text/javascript">
//addData();

function addData(){
  var flowId = '<%=flowId%>';
  var userId = '<%=userId%>';
  var seqId = '<%=seqId%>';
  var checkFlag = '<%=checkFlag%>';
  var url= "<%=contextPath%>/yh/subsys/oa/hr/score/act/YHScoreFlowAct/addData.act?flowId=" + flowId + "&userId="+userId + "&checkFlag=" + checkFlag;
  var rtJson = getJsonRs(url); 
  if(rtJson.rtState == "1"){
	alert(rtJson.rtMsrg); 
	return ; 
  }
 // var prc = rtJson.rtData;
 // seqId = prc.seqId;
}
</script>
</head>
<frameset rows="*"  cols="180,*" frameborder="NO" border="0" framespacing="0" id="frame1">
    <frame name="file_tree" id="file_tree" scrolling="auto" src="left.jsp?flowId=<%=flowId %>&userId=<%=userId %>&checkFlag=<%=checkFlag %>"  frameborder="no">
    <frame name="hrms" id="hrms" scrolling="auto" src="scoreList.jsp?flowId=<%=flowId %>&userId=<%=userId %>&currPage=1&checkFlag=<%=checkFlag %>" frameborder="no">
</frameset>