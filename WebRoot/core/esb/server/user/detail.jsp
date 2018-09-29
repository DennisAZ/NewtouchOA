<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%  
  String seqId = request.getParameter("seqId");
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>用户信息</title>
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
    if(rtJson.rtData.userType == 0){
      $('userType').innerHTML = '下级单位';
    }
    else if(rtJson.rtData.userType == 1){
      $('userType').innerHTML = '总部';
    }
    if(rtJson.rtData.status == 0){
      $('status').innerHTML = '启用';
    }
    else if(rtJson.rtData.status == 1){
      $('status').innerHTML = '未启用';
    }
    if(rtJson.rtData.isOnline == 0){
      $('isOnline').innerHTML = '否';
    }
    else if(rtJson.rtData.isOnline == 1){
      $('isOnline').innerHTML = '是';
    }
    
  }else{
    alert(rtJson.rtMsrg);
  }
}

</script>
</head>
<body onload="doInit();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/meeting.gif" width="17" height="17"><span class="big3"> 用户详细信息</span><br>
    </td>
  </tr>
</table>

<br>
<table class="TableBlock" width="90%" align="center">
  <tr>
    <td align="left" width="120" class="TableContent">用户账号：</td>
    <td align="left" class="TableData" width="180"><div id="userCode"></div> </td>
    <td align="left" width="120" class="TableContent">用户名称：</td>
    <td align="left" class="TableData" width="180"><div id="userName"></div> </td>
  </tr>
  <tr style="display:none">
    <td align="left" width="120" class="TableContent" >对应的应用Id：</td>
    <td align="left" class="TableData" width="180" colspan="3"><div id="appId"></div></td>
  </tr>
  <tr>
    <td align="left" width="120" class="TableContent">用户类型：</td>
    <td align="left" class="TableData" width="180"><div id="userType"></div> </td>
    <td align="left" width="120" class="TableContent">用户状态：</td>
    <td align="left" class="TableData Content" width="180"><div id="status"></div></td>
  </tr>
  <tr>
    <td align="left" width="120" class="TableContent">是否在线：</td>
    <td align="left" class="TableData" width="180"><div id="isOnline"></div> </td>
    <td align="left" width="120" class="TableContent">在线地址：</td>
    <td align="left" class="TableData Content" width="180"><div id="onlineIp"></div></td>
  </tr>
  <tr>
    <td align="left" width="120" class="TableContent">用户描述：</td>
    <td align="left" class="TableData Content" colspan="3"><div id="description"></div></td>
  </tr>
    <tr align="center" class="TableControl">
    <td colspan="4">
      <input type="button" value="关闭" class="BigButton" onClick="window.close();" title="关闭窗口">
    </td>
  </tr>
</table>
</body>
</html>