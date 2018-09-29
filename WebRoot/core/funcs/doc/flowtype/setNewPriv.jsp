<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ include file="/core/funcs/doc/workflowUtility/workflowheader.jsp" %>
<%
String flowId = request.getParameter("flowId");
String sortId = request.getParameter("sortId");
String isList = request.getParameter("isList");
if (isList == null || "".equals(isList)) {
  isList = "false";
}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>新建权限</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript"><!--
var flowId = '<%=flowId%>';
var sortId = '<%=sortId%>';
var isList = <%=isList%>;
var action = contextPath + "<%=moduleSrcPath %>/act/YHFreeFlowTypeAct";
function doInit(){
  var url  = action + "/getNewPriv.act";
  var json = getJsonRs(url , "flowId=" + flowId);
  if (json.rtState == '0') {
    $('user').value = json.rtData.userId;
	$('userDesc').update(json.rtData.userDesc);
	$('dept').value = json.rtData.dept;
	$('deptDesc').update(json.rtData.deptDesc);
	$('role').value = json.rtData.role;
	$('roleDesc').update(json.rtData.roleDesc);
  }
}
function savePriv(){
  var url  = action + "/updateNewPriv.act";
  var json = getJsonRs(url , $('newPrivForm').serialize());
  if (json.rtState == '0') {
    history.back();
    /*url = "list.jsp?sortId=" + sortId;
    if (isList) {
      location = url;
    } else {
	  parent.location = url ;
    }*/
  }
}
--></script>
</head>
<body onload="doInit()" style="margin:0px">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="small"><img src="<%=imgPath %>/edit.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3"> 指定自由流程新建人员</span>  <font color="red">(新建权限为“部门”、“角色”、“人员”三者的合集)</font>
    </td>
  </tr>
</table>
  <form action=""  method="post" id="newPrivForm" name="newPrivForm">
<table  width="600px" class="TableList" align="center" >
  <tr>
      <td nowrap class="TableContent" align="center">授权范围：<br>（人员）</td>
      <td class="TableData">
        <input type="hidden"  name="user" id="user" value="">
        <textarea cols=40 name="userDesc" id="userDesc" rows=8 class="BigStatic" wrap="yes" readonly></textarea>
        <a  style='' href="javascript:;" class="orgAdd" onClick="selectUser();">添加</a>
        <a  style='' href="javascript:;" class="orgClear" onClick="$('user').value='';$('userDesc').value='';">清空</a>
      </td>
   </tr>
   <tr>
      <td nowrap class="TableContent" align="center">授权范围：<br>（部门）</td>
      <td class="TableData" style="vertical-align: bottom">
        <input type="hidden"  name="dept" id="dept" value="">
        <textarea cols=40  name="deptDesc" id="deptDesc" rows=8 class="BigStatic" wrap="yes" readonly></textarea>
        <a  style='' href="javascript:;" class="orgAdd" onClick="selectDept();">添加</a>
        <a  style='' href="javascript:;" class="orgClear" onClick="$('dept').value='';$('deptDesc').value='';">清空</a>
      </td>
   </tr>
   <tr>
      <td nowrap class="TableContent" align="center">授权范围：<br>（角色）</td>
      <td class="TableData">
        <input type="hidden"  name="role" id="role" value="">
        <textarea cols=40  name="roleDesc" id="roleDesc"  rows=8 class="BigStatic" wrap="yes" readonly></textarea>
        <a  style='' href="javascript:;" class="orgAdd" onClick="selectRole();">添加</a>
        <a  style='' href="javascript:;" class="orgClear" onClick="$('role').value='';$('roleDesc').value='';">清空</a>
      </td>
   </tr>
   <tr>
     <td nowrap  class="TableControl" colspan="2" align="center">
      <input type="hidden" name="flowId" value="<%=flowId %>">
        <input type="button" value="保存" class="SmallButtonW" onclick='savePriv()'>&nbsp;&nbsp;
        <input type="button" value="返回" class="SmallButtonW" onclick="history.back()">
     </td>
     </tr>
    </table>
  </form>
</body>
</html>