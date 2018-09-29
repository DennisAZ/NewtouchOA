<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
String seqId = request.getParameter("seqId");
%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>编辑用户信息</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript">

function doInit(){
  var url = "<%=contextPath%>/yh/core/esb/server/user/act/TdUserAct/getUserDetail.act?seqId=<%=seqId%>";
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "0"){
    var data = rtJson.rtData;
    bindJson2Cntrl(rtJson.rtData);
  }else{
    alert(rtJson.rtMsrg); 
  }
}

</script>
</head>
<body onload="doInit();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td><img src="<%=imgPath %>/notify_new.gif" align="middle"><span class="big3"> 编辑用户信息</span>&nbsp;&nbsp;
    </td>
  </tr>
</table>
<br>
<form action="<%=contextPath%>/yh/core/esb/server/user/act/TdUserAct/updateUser.act" id="" method="post">
<table class="TableBlock" width="80%" align="center">
    <tr>
  	  <td  nowrap class="TableData">用户账号：</td>
  	  <td  nowrap class="TableData"><input type="text" id="userCode" name="userCode" style="width:120px;" disabled></td>
  	  <td  nowrap class="TableData">用户名称：</td>
  	  <td  nowrap class="TableData"><input type="text" id="userName" name="userName" style="width:120px;"></td>
    </tr>
    <tr style="display:none">
  	  <td  nowrap class="TableData">对应的应用：</td>
  	  <td  nowrap class="TableData" colspan="3">
  	    <select id="appId" name="appId" style="width:120px;">
  	      <option value="1">1</option>
  	      <option value="2">2</option>
  	    </select>
  	  </td>
    </tr>
    <tr>
  	  <td  nowrap class="TableData">用户类型：</td>
  	  <td  nowrap class="TableData">
  	    <input type="radio" id="userType1" name="userType" value="0" checked>下级单位
  	    <input type="radio" id="userType2" name="userType" value="1">总部
  	  </td>
  	  <td  nowrap class="TableData">用户状态：</td>
  	  <td  nowrap class="TableData">
  	    <input type="radio" id="status1" name="status" value="0" checked>启用
  	    <input type="radio" id="status2" name="status" value="1">未启用
  	  </td>
    </tr>
    <tr>
  	  <td  nowrap class="TableData">用户描述：</td>
  	  <td  nowrap class="TableData" colspan="3"><textarea id="description" name="description" rows="3" style="width:98%;"></textarea></td>
    </tr>
    <tr>
  	  <td  nowrap class="TableData" colspan="4" align="center">
  	    <input type="hidden" id="seqId" name="seqId">
  	    <input type="submit" id="" name="" value="提交">
  	    <input type="button" id="" name="" value="返回" onclick="window.parent.location.href('<%=contextPath%>/core/esb/server/user/index.jsp')">
  	  </td>
    </tr>
</table>
</form>
</body>
</html>