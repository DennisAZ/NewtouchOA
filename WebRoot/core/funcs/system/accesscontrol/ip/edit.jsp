<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  String seqId = request.getParameter("seqId");
  if (seqId == null){
    seqId = "";
  }
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>公告通知设置</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<link rel="stylesheet" href = "<%=cssPath%>/cmp/Calendar.css">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href = "<%=cssPath%>/cmp/tree.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/funcs/system/accesscontrol/js/ip.js"></script>
<script type="text/javascript">
var seqId = "<%=seqId%>";

function doInit(){
  var url = "<%=contextPath%>/yh/core/funcs/system/accesscontrol/act/YHIpRuleAct/getEditIpRule.act?seqId="+seqId;
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "0"){
    bindJson2Cntrl(rtJson.rtData);
    for(var i = 0; i < rtJson.rtData.length; i++){
      document.getElementById("seqId").value = rtJson.rtData[i].seqId;
      document.getElementById("beginIp").value = rtJson.rtData[i].beginIp;
      document.getElementById("endIp").value = rtJson.rtData[i].endIp;
      if(rtJson.rtData[i].type == "0"){
        document.getElementById("type").value="0";
      }else{
        document.getElementById("type").value="1";
      }
      if(rtJson.rtData[i].remark == "null" || rtJson.rtData[i].remark == ""){
        document.getElementById("remark").value = "";
      }else{
        document.getElementById("remark").value = rtJson.rtData[i].remark;
      }
    }
  }else{
  	alert(rtJson.rtMsrg); 
   }
}

function commit(){
  if(!check()){
    return;
  }
  var seqId = document.getElementById("seqId").value;
  var type = document.getElementById("type").value;
  var beginIp = document.getElementById("beginIp").value;
  var endIp = document.getElementById("endIp").value;
  var remark = document.getElementById("remark").value;
  var url = "<%=contextPath%>/yh/core/funcs/system/accesscontrol/act/YHIpRuleAct/updateIpRule.act?seqId="+seqId+"&beginIp="+beginIp+"&endIp="+endIp+"&remark="+remark+"&type="+type;
  var rtJson = getJsonRs(url, mergeQueryString($("form1")));
  parent.location = "<%=contextPath %>/core/funcs/system/accesscontrol/index.jsp";
}
</script>
</head>
<body topmargin="5" onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">

  <tr>
    <td class="Big"><img src="<%=imgPath%>/edit.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3">&nbsp;允许访问IP规则编辑</span>
    </td>
  </tr>
</table>
<form method="post" name="form1" id="form1">
  <input type="hidden" name="seqId" id="seqId" value="">
  <input type="hidden" id="dtoClass" name="dtoClass" value="yh.core.funcs.system.accesscontrol.data.YHIpRule.java"/>
  <table class="TableBlock" width="450" align="center">
   <tr>
    <td nowrap class="TableData">起始IP：</td>
    <td nowrap class="TableData">
        <input type="text" name="beginIp" id="beginIp" class="BigInput" size="25" maxlength="25" value="">&nbsp;（例如：192.168.0.10）
    </td>
   </tr>
   <tr>
    <td nowrap class="TableData">结束IP：</td>
    <td nowrap class="TableData">
        <input type="text" name="endIp" id="endIp" class="BigInput" size="25" maxlength="25" value="">&nbsp;（例如：192.168.0.10）
    </td>
   </tr>
   <tr>
    <td nowrap class="TableData">规则类型：</td>
    <td nowrap class="TableData">
        <select name="type" id="type" class="BigSelect">
           <option value="0" selected>OA登录规则</option>
           <option value="1" >考勤限制规则</option>
        </select>
    </td>
   </tr>
   <tr>
    <td nowrap class="TableData">备注：</td>
    <td nowrap class="TableData">
        <textarea name="remark" id="remark" class="BigInput" cols="50" rows="3"></textarea>
    </td>
   </tr>
   <tr>
    <td nowrap  class="TableControl" colspan="2" align="center">
        <input type="hidden" value="10" name="RULE_ID">
        <input type="submit" value="确定" class="BigButton" onclick="commit()">&nbsp;&nbsp;
        <input type="button" value="返回" class="BigButton" onclick="history.back();">
    </td>
  </table>
</form>
</body>
</html>