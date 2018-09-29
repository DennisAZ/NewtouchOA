<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page import="java.text.*" %>
<%@ page import="java.util.*,yh.core.funcs.system.censorwords.data.YHCensorWords" %>
<%@ page import="yh.core.data.YHRequestDbConn, yh.core.global.YHBeanKeys" %>
<%
  String seqId = request.getParameter("seqId");
  if (seqId == null){
    seqId = "";
  }
  
  String groupName = request.getParameter("groupName");
  if (groupName == null){
    groupName = "";
  }
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<base target="_self">
<title>设置维护权限</title>
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
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript">
var seqId = "<%=seqId%>";
var groupName = "<%=groupName%>";

function doInit(){
  var url = "<%=contextPath%>/yh/core/funcs/system/address/act/YHAddressAct/getEditPrivGroup.act?seqId="+seqId;
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "0"){
    for(var i = 0; i < rtJson.rtData.length; i++){
  	  var supportDept = rtJson.rtData[i].supportDept;
  	  var supportUser = rtJson.rtData[i].supportUser;
  	  var groupName = rtJson.rtData[i].groupName;
  	  document.getElementById("dept").value = supportDept;
  	  document.getElementById("user").value = supportUser;
  	  document.getElementById("groupName").value = groupName;
  	  document.getElementById("groupNameOld").value = groupName;
  	  $("deptDesc").value = rtJson.rtData[i].supportDeptName;
  	  $("userDesc").value = rtJson.rtData[i].supportUserName;
    }
  }else{
    alert(rtJson.rtMsrg); 
  }
}

function commit(){
  var groupName = document.getElementById("groupName");
  if(groupName.value == ""){ 
    alert("组名不能为空！");
    groupName.focus();
    groupName.select();
	return false;
  }
  var supportUser = document.getElementById("user").value;
  var supportDept = document.getElementById("dept").value;
  var groupName = document.getElementById("groupName").value;
  var groupNameOld = document.getElementById("groupNameOld").value
  var url = "<%=contextPath%>/yh/core/funcs/system/address/act/YHAddressAct/updatePrivGroup.act?seqId="+seqId+"&groupNameOld="+encodeURIComponent(groupNameOld);
  var rtJson = getJsonRs(url, "supportUser="+supportUser+"&supportDept="+supportDept+"&groupName="+encodeURIComponent(groupName));
  if(rtJson.rtState == "0"){
    location = "<%=contextPath %>/core/funcs/system/address/manage/submit.jsp";
  }else{
    location = "<%=contextPath %>/core/funcs/system/address/manage/insertGroup.jsp";
  }
}


</script>
</head>
<body topmargin="5" onload="doInit()" style="text-align:left">
<form method="post" name="form1">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big" align=left><img src="<%=imgPath%>/edit.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3">&nbsp;设置维护权限</span></td>
  </tr>
</table>
<br>
<div align="center">
<table class="TableBlock" width="470" align="center" >
   <tr>
    <td nowrap class="TableContent" width="90">分组名称：</td>
    <td class="TableData" align=left>
      <input type="text" name="groupName" id="groupName" size="27" readonly maxlength="100" class="BigStatic" value="">
      <input type="hidden" name="groupNameOld" id="groupNameOld" value="">
   </td>
   </tr>
   <tr>
    <td nowrap class="TableData">维护部门：</td>
      <td class="TableData" align=left>
        <input type="hidden" name="dept" id="dept" value="">
        <textarea cols=30 name="deptDesc" id="deptDesc" rows=4 class="BigStatic" wrap="yes" readonly></textarea>
        <a href="javascript:;" class="orgAdd" onClick="selectDept()">添加</a>
        <a href="javascript:;" class="orgClear" onClick="$('dept').value='';$('deptDesc').value='';">清空</a>
      </td>
   </tr>
   <tr>
    <td nowrap class="TableData">维护人员：</td>
      <td class="TableData" align=left>
        <input type="hidden" name="user" id="user" value="">
        <textarea cols=30 name="userDesc" id="userDesc" rows=4 class="BigStatic" wrap="yes" readonly></textarea>
        <a href="javascript:;" class="orgAdd" onClick="selectUser();">添加</a>
        <a href="javascript:;" class="orgClear" onClick="$('user').value='';$('userDesc').value='';">清空</a>
      </td>
   </tr>   
</table>
</div>
<br>
<div align="center">
	<input type="hidden" name="GROUP_ID" value="<?=$GROUP_ID?>">
  <input type="button" value="确定" class="BigButton" OnClick="commit()">&nbsp;&nbsp;	
	<input type="button" value="关闭" class="BigButton" onClick="window.close();" title="关闭窗口">
</div>
  </form>
</body>
</html>