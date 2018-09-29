<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ include file="/core/funcs/doc/workflowUtility/workflowheader.jsp" %>
<%String isList = request.getParameter("isList"); 
if(isList == null ){
  isList = "";
}

%>
<%@ include file="/core/inc/header.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>设置经办权限</title>
<link rel="stylesheet" href ="<%=cssPath%>/style.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath %><%=moduleContextPath %>/workflowUtility/utility.js"></script>
<script type="text/javascript">
var isList = "<%=isList%>";
var seqId = "<%=request.getParameter("seqId")%>";

function doInit(){
  url = contextPath + "<%=moduleSrcPath %>/act/YHFlowProcessAct/getPriv.act";
  var json = getJsonRs(url , "seqId=" + seqId);
  
  if(json.rtState == "0"){
    $('user').value = json.rtData.userId;
		$('userDesc').innerHTML = json.rtData.userName;
	
		$('dept').value = json.rtData.deptId;
		$('deptDesc').innerHTML = json.rtData.deptName;

		$('role').value = json.rtData.privId;
		$('roleDesc').innerHTML = json.rtData.privName;
  }
}
function commit(){
  url = contextPath + "<%=moduleSrcPath %>/act/YHFlowProcessAct/savePriv.act";
  var json = getJsonRs(url , $('privForm').serialize());
  if(json.rtState == '0'){
    //alert(json.rtMsrg);
    if(!isList){
      try {
        opener.location.reload();
      } catch (e) {

      }
      window.close();
    }else{
      history.back();
    }
  }else{
    alert(json.rtMsrg);
  }
}
</script>
</head>
<body onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="small"><img src="<%=imgPath %>/edit.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3"> 设置经办权限</span>  <font color="red">(经办权限为“部门”、“角色”、“人员”三者的合集)</font>
    </td>
  </tr>
</table>
<form  method="post" id="privForm" name="privForm">
<table width="100%" class="TableBlock" align="center" >
  <tr>
      <td nowrap class="TableContent"" align="center">授权范围：<br>（人员）</td>
      <td class="TableData">
        <input type="hidden" id="user" name="user" value="">
        <textarea cols=40 id="userDesc" name="userDesc" rows=8 class="BigStatic" wrap="yes" readonly></textarea>
 <a  style='' href="javascript:;" class="orgAdd" onClick="selectUser();">添加</a>
        <a  style='' href="javascript:;" class="orgClear" onClick="$('user').value='';$('userDesc').value='';">清空</a>
      </td>
   </tr>
   <tr>
      <td nowrap class="TableContent" align="center">授权范围：<br>（部门）</td>
      <td class="TableData">
        <input type="hidden" id="dept" name="dept" value="">
        <textarea cols=40 id="deptDesc" name=deptDesc rows=8 class="BigStatic" wrap="yes" readonly></textarea>
      <a  style='' href="javascript:;" class="orgAdd" onClick="selectDept();">添加</a>
        <a  style='' href="javascript:;" class="orgClear" onClick="$('dept').value='';$('deptDesc').value='';">清空</a>
      </td>
   </tr>
   <tr>
      <td nowrap class="TableContent"" align="center">授权范围：<br>（角色）</td>
      <td class="TableData">
        <input type="hidden" id="role" name="role" value="">
        <textarea cols=40 id="roleDesc" name="roleDesc" rows=8 class="BigStatic" wrap="yes" readonly></textarea>
        <a  style='' href="javascript:;" class="orgAdd" onClick="selectRole();">添加</a>
        <a  style='' href="javascript:;" class="orgClear" onClick="$('role').value='';$('roleDesc').value='';">清空</a>
      </td>
   </tr>
   <tr>
    <td nowrap  class="TableControl" colspan="2" align="center">
    <input type=hidden value="<%=request.getParameter("seqId")%>" name="procId"> 
        <input type="button" onclick="commit();" value="确定" class="BigButton">&nbsp;&nbsp;
        <% if("".equals(isList)){ %>
  <input type="button"  value="关闭" class="BigButton" onclick="closeWindow();">
  <% }else{ %>
  <input type="button"  value="返回" class="BigButton" onclick="history.back();">
  <% } %>
   </td>
</table>
  </form>
</body>
</html>