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
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>分组管理</title>
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

function doInit(){
  var url = "<%=contextPath%>/yh/core/funcs/system/address/act/YHAddressAct/getEditGroup.act?seqId="+seqId;
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "0"){
    for(var i = 0; i < rtJson.rtData.length; i++){
  	  var privDept = rtJson.rtData[i].privDept;
  	  var privRole = rtJson.rtData[i].privRole;
  	  var privUser = rtJson.rtData[i].privUser;
  	  var orderNo = rtJson.rtData[i].orderNo;
  	  var groupName = rtJson.rtData[i].groupName;
  	  var deptName = rtJson.rtData[i].deptName;
  	  var roleName = rtJson.rtData[i].roleName;
  	  var userName = rtJson.rtData[i].userName;
  	  document.getElementById("orderNo").value = orderNo;
  	  document.getElementById("dept").value = privDept;
  	  document.getElementById("role").value = privRole;
  	  document.getElementById("user").value = privUser;
  	  document.getElementById("groupName").value = groupName;
  	  document.getElementById("groupNameOld").value = groupName;

  	  document.getElementById("deptDesc").value = deptName;
  	  document.getElementById("roleDesc").value = roleName;
  	  document.getElementById("userDesc").value = userName;
    }
  }else{
    alert(rtJson.rtMsrg); 
  }
}

function commit(){
  var orderNo = document.getElementById("orderNo");
  var groupName = document.getElementById("groupName");
  var reg = /^[0-9]*$/;
  if(!reg.test(orderNo.value)){
  	alert("排序号只能输入数字！");
  	orderNo.focus();
  	orderNo.select();
  	return false;
  }
  if(groupName.value == ""){ 
    alert("组名不能为空！");
    groupName.select();
    groupName.focus();
	return false;
  }
  reg = /['"]/g;
  if (groupName.value.match(reg)) {
    alert("组名不能有\"'\"和\"\"\"字符！");
    $('groupName').focus();
  	return false;
  }
  var privUser = document.getElementById("user").value;
  var privDept = document.getElementById("dept").value;
  var privRole = document.getElementById("role").value;
  var groupName = document.getElementById("groupName").value;
  var groupNameOld = document.getElementById("groupNameOld").value
  var orderNo = document.getElementById("orderNo").value;
  var url = "<%=contextPath%>/yh/core/funcs/system/address/act/YHAddressAct/updateGroup.act?seqId="+seqId+"&groupNameOld="+encodeURIComponent(groupNameOld);
  var rtJson = getJsonRs(url, "privUser="+privUser+"&privDept="+privDept+"&privRole="+privRole+"&groupName="+encodeURIComponent(groupName)+"&orderNo="+orderNo);
  if(rtJson.rtState == "0"){
    location = "<%=contextPath %>/core/funcs/system/address/manage/index.jsp";
  }else{
    location = "<%=contextPath %>/core/funcs/system/address/manage/insertGroup.jsp";
  }
}

</script>
</head>
<body topmargin="5" onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/notify_open.gif" align="absmiddle"><span class="big3">&nbsp;编辑分组 </span>
    </td>
  </tr>
</table>

<br>

<form name="form1" id="form1" method="post" enctype="multipart/form-data">
<table class="TableBlock"  width="570" align="center">
    <tr>
      <td nowrap class="TableData">排序号：</td>
      <td class="TableData"><input type="text" name="orderNo" id="orderNo" size="8" class="BigInput"></td>
    </tr>
    <tr>
      <td nowrap class="TableData">分组名称：</td>
      <input type="hidden" name="groupNameOld" id="groupNameOld" value="">
      <td class="TableData"><input type="text" name="groupName" id="groupName" size="25" class="BigInput">&nbsp;&nbsp;<font style='color:red'>*</font></td>
    </tr>
    <tr>
      <td nowrap class="TableData">公布范围（部门）</td>
      <td class="TableData">
        <input type="hidden" name="dept" id="dept" value="">
        <textarea cols=28 name="deptDesc" id="deptDesc" rows=2 class="BigStatic" wrap="yes" readonly></textarea>
        <a href="javascript:;" class="orgAdd" onClick="selectDept()">添加</a>
        <a href="javascript:;" class="orgClear" onClick="$('dept').value='';$('deptDesc').value='';">清空</a>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">公布范围（角色）</td>
      <td class="TableData">
        <input type="hidden" name="role" id="role" value="">
        <textarea cols=28 name="roleDesc" id="roleDesc" rows=2 class="BigStatic" wrap="yes" readonly></textarea>
        <a href="javascript:;" class="orgAdd" onClick="selectRole();">添加</a>
        <a href="javascript:;" class="orgClear" onClick="$('role').value='';$('roleDesc').value='';">清空</a>
      </td>
   </tr>
   <tr>
      <td nowrap class="TableData">公布范围（人员)</td>
      <td class="TableData">
        <input type="hidden" name="user" id="user" value="">
        <textarea cols=28 name="userDesc" id="userDesc" rows=2 class="BigStatic" wrap="yes" readonly></textarea>
        <a href="javascript:;" class="orgAdd" onClick="selectUser();">添加</a>
        <a href="javascript:;" class="orgClear" onClick="$('user').value='';$('userDesc').value='';">清空</a>
      </td>
   </tr>
    <tr>
      <td nowrap class="TableControl" colspan="2" align="center">
          <input type="button" value="提交" class="BigButton" title="提交数据" name="button1" OnClick="commit()">&nbsp&nbsp&nbsp&nbsp
          <input type="button" value="返回" class="BigButton" title="返回" name="button2" OnClick="location='<%=contextPath%>/core/funcs/system/address/manage/index.jsp'">
      </td>
    </tr>
    </form>
</table>
</body>
</html>