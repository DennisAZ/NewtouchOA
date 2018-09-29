<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  String flag = request.getParameter("flag");
  String privId = request.getParameter("privId");
  String deptId = request.getParameter("deptId");
  String userId = request.getParameter("userId");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>收文权限设置 </title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/page.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/page.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script> 

var flag = <%=flag %>;
var privId = <%=privId %>;
var deptId = <%=deptId %>;
var userId = <%=userId %>;
var changeValue = 0;

function doInit(){
  if(flag == 0){
    $('dept').checked = true;
    changeType(0);
    if(deptId){
      $('deptStr').value = deptId;
      bindDesc([{cntrlId:"deptStr", dsDef:"department,SEQ_ID,DEPT_NAME"}]);
      $('userStr').value = userId;
      bindDesc([{cntrlId:"userStr", dsDef:"person,SEQ_ID,USER_NAME"}]);
    }
  }
  else{
    $('global').checked = true;
    changeType(1);
    if(privId){
       $('privStr').value = privId;
       bindDesc([{cntrlId:"privStr", dsDef:"user_priv,SEQ_ID,PRIV_NAME"}]);
     }
  }
}


function changeType(value){
  if(value == 0){
	  changeValue = 0;
	  $('set_dept').style.display = "";
	  $('set_global').style.display = "none";
  }
  else{
	  changeValue = 1;
    $('set_dept').style.display = "none";
    $('set_global').style.display = "";
  }
}

function submit(){
  if(changeValue == 0){
    if (!$('deptStr').value) {
       alert("部门不能为空！");
       return ;
    }
    if (!$('userStr').value) {
      alert("收文人员不能为空！");
      return ;
   }
    var url = "<%=contextPath %>/yh/core/funcs/doc/act/YHDocRecvPrivAct/addDeptPriv.act?deptIdStr="+$('deptStr').value+"&userIdStr="+$('userStr').value;
    var rtJson = getJsonRs(url);
    if(rtJson.rtState == "0"){
      alert("部门权限设置成功！");
      window.opener.location.reload();
      window.close();
    }
  }else{
    if (!$('privStr').value) {
      alert("角色不能为空！");
      return ;
   }
	  var url = "<%=contextPath %>/yh/core/funcs/doc/act/YHDocRecvPrivAct/addAllPriv.act?privIdStr="+$('privStr').value;
	  var rtJson = getJsonRs(url);
	  if(rtJson.rtState == "0"){
		  alert("全局权限设置成功！");
		  window.opener.location.reload();
		  window.close();
	  }
  }
}
</script>
</head>
<body topmargin="5" onload="doInit()">

<table width="100%" class="TableBlock" align="center">
  <tr>
    <td class="TableContent">权限种类</td>
    <td class="TableData">
      <input type="radio" name="priv_type"  onclick="changeType(this.value)" id="dept" value="0" checked>
      <label for="dept">按部门</label> 
      <input type="radio" name="priv_type" onclick="changeType(this.value)" id="global" value="1">
      <label for="global">全局设置</label>
    </td>
  </tr>
  <tbody id="set_dept">
    <tr>
      <td class="TableContent">选择部门</td>
      <td class="TableData">
        <input type="hidden" id="deptStr" name="deptStr" value=""> 
        <textarea cols=40 id="deptStrDesc" name="deptStrDesc" rows=3 class="BigStatic" wrap="yes" readonly></textarea> 
        <a href="javascript:;" class="orgAdd" onClick="javascript:selectDept(['deptStr','deptStrDesc'],5,null,true)">添加</a>
        <a href="javascript:;" class="orgClear" onClick="$('deptStr').value='';$('deptStrDesc').value='';">清空</a>
      </td>
    </tr>
    <tr>
      <td class="TableContent">选择人员</td>
      <td class="TableData">
        <input type="hidden" id="userStr" name="userStr" value=""> 
        <textarea cols=40 id="userStrDesc" name="userStrDesc" rows=3 class="BigStatic" wrap="yes" readonly></textarea> 
        <a href="javascript:;" class="orgAdd" onClick="selectUser(['userStr', 'userStrDesc'],5)">添加</a>
        <a href="javascript:;" class="orgClear" onClick="$('userStr').value='';$('userStrDesc').value='';">清空</a>
      </td>
    </tr>

  </tbody>
  <tbody id="set_global" style="display: none">
    <tr>
      <td class="TableContent">选择角色</td>
      <td class="TableData">
        <input type="hidden" id="privStr" name="privStr" value=""> 
        <textarea cols=40 id="privStrDesc" name="privStrDesc" rows=3 class="BigStatic" wrap="yes" readonly></textarea> 
        <a href="javascript:;" class="orgAdd" onClick="selectRole(['privStr','privStrDesc'],5)">添加</a> 
        <a href="javascript:;" class="orgClear" onClick="$('privStr').value='';$('privStrDesc').value=''">清空</a>
      </td>
    </tr>
  </tbody>
</table>
<br>
<center>
<input type="button" class="BigButton" value="确定" onclick="submit()">
<input type="button" class="BigButton" value="关闭" onclick="window.close()">
</center>
</body>
</html>