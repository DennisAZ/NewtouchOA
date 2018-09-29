<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page import="java.text.*" %>
<%@ page import="java.util.*,yh.core.funcs.system.accesscontrol.data.YHIpRule" %>
<%@ page import="yh.core.data.YHRequestDbConn, yh.core.global.YHBeanKeys" %>
<%
  ArrayList<YHIpRule> ruleList = (ArrayList<YHIpRule>)request.getAttribute("ruleList");
  String seqIdStr = "";
  for(int i = 0; i < ruleList.size(); i++) {
    YHIpRule ruleIp = ruleList.get(i);
    seqIdStr += ruleIp.getSeqId()+",";
}
  //if(ruleList == null){return;}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>IP规则设置</title>
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
var strId = "<%=seqIdStr%>";
function doInit(){
  //var url = "<%=contextPath%>/yh/core/funcs/system/accesscontrol/act/YHIpRuleAct/getIpRule.act";
  //var rtJson = getJsonRs(url);
}

function confirmDel() {
  if(confirm("确认删除所有规则吗？")) {
    return true;
  }else {
    return false;
  }
}

function deleteRule(seqId){
  if(!confirm("确认删除此规则吗？")) {
  	return ;
  } 
  var url = "<%=contextPath%>/yh/core/funcs/system/accesscontrol/act/YHIpRuleAct/deleteIpRule.act?seqId="+seqId;
  var rtJson = getJsonRs(url);
  if (rtJson.rtState == "0") {
    location.reload();
  }else {
    alert(rtJson.rtMsrg); 
  } 
}

function deleteAll(){
  if(!confirmDel()) {
  	return ;
  } 
  var url = "<%=contextPath%>/yh/core/funcs/system/accesscontrol/act/YHIpRuleAct/deleteAllIpRule.act?seqId="+strId;
  var rtJson = getJsonRs(url);
  location.reload();
}

function commit(){
  if(!check()){
    return;
  }
  var secOcMark = "";
  var typea = document.getElementById("type");
  var option = typea.getElementsByTagName("option");
  for(var i = 0; i < option.length; i++){
    if(option[i].selected){
      secOcMark = option[i].value;
    }
  }
  var beginIp = document.getElementById("beginIp").value;
  var endIp = document.getElementById("endIp").value;
  var remark = document.getElementById("remark").value;
  var url = "<%=contextPath%>/yh/core/funcs/system/accesscontrol/act/YHIpRuleAct/addIpRule.act?secOcMark=" + secOcMark;
  var rtJson = getJsonRs(url, Form.serialize($("form1")));
  location.reload();
  //location = "<%=contextPath %>/core/funcs/system/accesscontrol/ip/index.jsp";
}
</script>
</head>
<body onload="doInit()" topmargin="5">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/notify_new.gif" align="absmiddle"><span class="big3">&nbsp;添加允许访问IP规则</span>
    </td>
  </tr>
</table>
<form method="post" name="form1" id="form1">
  <input type="hidden" name="seqId" id="seqId" value="">
  <input type="hidden" id="dtoClass" name="dtoClass" value="yh.core.funcs.system.accesscontrol.data.YHIpRule.java"/>
  <table class="TableBlock" width="450" align="center" >
   <tr>
    <td nowrap class="TableData">起始IP：</td>
    <td nowrap class="TableData">
        <input type="text" name="beginIp" id="beginIp" class="BigInput" size="25" maxlength="25">&nbsp;（例如：192.168.0.10）
    </td>
   </tr>
   <tr>
    <td nowrap class="TableData">结束IP：</td>
    <td nowrap class="TableData">
        <input type="text" name="endIp" id="endIp" class="BigInput" size="25" maxlength="25">&nbsp;（例如：192.168.0.10）
    </td>
   </tr>
   <tr>
    <td nowrap class="TableData">规则类型：</td>
    <td nowrap class="TableData">
        <select name="type" id="type" class="BigSelect">
           <option value="0" selected>OA登录规则</option>
           <option value="1">考勤限制规则</option>
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
        <input type="button" value="添加" class="BigButton" title="添加允许访问IP段" name="button" onclick="commit()">
    </td>
  </table>
</form>
<br>
<table width="95%" border="0" cellspacing="0" cellpadding="0" height="3">
 <tr>
   <td background="<%=imgPath%>/dian1.gif" width="100%"></td>
 </tr>
</table>
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/notify_open.gif" align="absmiddle"><span class="big3">&nbsp;管理IP访问规则</span>
    </td>
  </tr>
</table>
<br>
<div align="center">
<table class="TableList" width="90%">
<% 
  String remarkStr = "";
  for(int i = 0; i < ruleList.size(); i++) {
    YHIpRule ruleIp = ruleList.get(i);
    String type = "";
    if(ruleIp.getType().equals("0")){
      type = "OA登录规则";
    }else{
      type = "考勤限制规则"; 
    }
    if(ruleIp.getRemark() == null || ruleIp.getRemark() ==""){
      remarkStr = "";
    }
    else{
      remarkStr = ruleIp.getRemark();
    }
%>
    <tr class="<%= (i % 2 == 0) ? "TableLine1" : "TableLine2" %>">
      <td nowrap align="center"><%=ruleIp.getBeginIp()%></td>
      <td nowrap align="center"><%=ruleIp.getEndIp()%></td>
      <td nowrap align="center"><%=type%></td>
      <td><%=remarkStr%></td>
      <td nowrap align="center" width="80">
      <a href="<%=contextPath%>/core/funcs/system/accesscontrol/ip/edit.jsp?seqId=<%=ruleIp.getSeqId()%>" >编辑</a>
      <a href="javascript:deleteRule('<%=ruleIp.getSeqId()%>');"> 删除</a>
      </td>
    </tr>
<%
  } 
  if(ruleList.size()>0){ 
%>
<thead class="TableHeader">
  <td nowrap align="center" width="100">起始IP</td>
  <td nowrap align="center" width="100">结束IP</td>
  <td nowrap align="center" width="100">规则类型</td>
  <td nowrap align="center">备注</td>
  <td nowrap align="center" width="80">操作</td>
</thead>
<thead class="TableControl">
  <td nowrap align="center" colspan="5">
    <input type="button" class="BigButton" OnClick="javascript:deleteAll();" value="全部删除">
  </td>
</thead>
<%
  }else{
%>
<table class="MessageBox" align="center" width="300">
  <tr>
    <td class="msg info">
      <div class="content" style="font-size:12pt">尚未定义访问规则</div>
    </td>
  </tr>
</table>
<% 
  }
%>
   
 </table>
</div>
</body>
</html>