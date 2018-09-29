<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>数据交换平台设置</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript">
var requestUrl = "<%=contextPath%>/yh/core/esb/client/act/YHEsbConfigAct";
function doInit(){
  var url = requestUrl + "/getClientConfig.act";
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "0"){
    var data = rtJson.rtData;
    bindJson2Cntrl(data);
    $('isLocal').value = data.local;
    if (data.local == '1') {
      $('esbClient').hide();
    }else {
      $('esbClient').show();
    }
  }
  isOnline();
  setInterval(isOnline, 60000);
}
function setConfig() {
  var url =  requestUrl + "/updateClientConfig.act";
  var rtJson = getJsonRs(url , $('form1').serialize());
  if(rtJson.rtState == "0"){
    alert(rtJson.rtMsrg);
  }
  isOnline();
}

function isOnline() {
  var url =  requestUrl + "/isOnline.act";
  var rtJson = getJsonRsAsyn(url , null , updateState);
}
function updateState(rtJson) {
  var isline = false;
  if(rtJson.rtState == "0"){
    if (rtJson.rtData) {
      isline = true;
    } 
  } else {
    isline = false;
  }
  if (isline) {
    $('state').update("<img  src=\"./img/a1.gif\" align=\"absmiddle\">已连接");
  } else {
    $('state').update("<img  src=\"./img/a0.gif\" align=\"absmiddle\">未连接");
  }
}
function selectChange(value){
  if (value == '1') {
    $('esbClient').hide();
  }else {
    $('esbClient').show();
  }
}
</script>
</head>
<body class="bodycolor" topmargin="5" onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/theme.gif" align="absmiddle"><span class="big3"> 数据交换平台设置</span>
    </td>
  </tr>
</table>
<table class="TableBlock" width="550" align="center">
  <form action="" method="post" name="form1" id="form1" >
   <tr class="TableData">
    <td nowrap class="TableData" align="right">数据交换平台连接状态：</td>
    <td>
        <span id="state"><img  src="./img/a0.gif" align="absmiddle">未连接</span>
    </td>
   </tr>
   <tr class="TableData">
    <td nowrap class="TableData" align="right">ESB服务器地址、端口：</td>
    <td>
        <input type="text" name="ESBSERVER" id="ESBSERVER" class="BigInput" value="" size="15" maxlength="30">
        ：<input type="text" name="ESBSERVERPORT" id="ESBSERVERPORT" class="BigInput" value="8089" size="4" maxlength="5">
    </td>
   </tr>
   <tr class="TableData">
    <td nowrap class="TableData" align="right">登录用户名：</td>
    <td>
        <input type="text" name="userId" id="userId" class="BigInput" value="main" size="20" maxlength="20">
    </td>
   </tr>
   <tr class="TableData">
    <td nowrap class="TableData" align="right">登录密码：</td>
    <td>
        <input type="password" name="password" id="password" class="BigInput" value="" size="20">&nbsp;
    </td>
   </tr>
   <tr class="TableData">
    <td nowrap class="TableData" align="right">本地接口：</td>
    <td>
    <select name="isLocal" id="isLocal" onchange="selectChange(this.value)">
    <option value="1" select>本地接口</option>
    <option value="0" >远程接口</option>
    </select>
    </td>
   </tr>
   <tbody id="esbClient" style="display:none">
   <tr class="TableData">
    <td nowrap class="TableData" align="right">ESB客户端服务地址、端口：</td>
    <td>
      <input type="text" name="ESBHOST" id="ESBHOST" class="BigInput" value="" size="15">
        ：<input type="text" name="ESBPORT" ID="ESBPORT" class="BigInput" value="8089" size="4" maxlength="5">
    </td>
   </tr>
    <tr class="TableData">
    <td nowrap class="TableData" align="right">令牌：</td>
    <td>
        <input type="text" name="token" id="token" value="" class="BigInput" size="40">&nbsp;
    </td>
   </tr>
   <tr class="TableData">
    <td nowrap class="TableData" align="right">OA服务地址、端口：</td>
    <td>
        <input type="text" name="OAHOST" ID="OAHOST" class="BigInput" value="" size="15">
        ：<input type="text" name="OAPORT" id="OAPORT" class="BigInput" value="8080" size="4" maxlength="5">
    </td>
   </tr>
   </tbody>
   <tr class="TableData">
    <td nowrap class="TableData" align="right">接收数据包目录：</td>
    <td>
        <input type="text" name="cachePath" id="cachePath" value="" class="BigInput" size="40">&nbsp;
    </td>
   </tr>
  
   <tr>
    <td nowrap  class="TableControl" colspan="2" align="center">
        <input type="hidden" name="UID" id="UID" value="">
        <input type="button" value="保存" class="BigButton" name="button" onclick="setConfig()">&nbsp;&nbsp;
    </td>
  </form>
</table>

</body>
</html>