<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %> 

<%
	String seqId=request.getParameter("seqId");
	if(seqId==null){
	  seqId="";
	}
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>指定可访问人员</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href = "<%=cssPath%>/cmp/ExchangeSelect.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/DTree1.0.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript">
//alert('setUser.jsP:<%=seqId%>');
var requestURL = "<%=contextPath%>/yh/subsys/oa/confidentialFile/act/YHSetConfidentialSortAct";
function checkForm(){
	if($("user").value=="" && $("role").value=="" && $("dept").value==""){
		alert("请指定授权范围!");
		return false;
	}
	if(!$("USER_ID").checked && !$("MANAGE_USER").checked && !$("NEW_USER").checked && !$("DOWN_USER").checked && !$("OWNER").checked){
		alert("请指定设置选项!");
		return false
	}
	return true;
}

function sendForm(){
	var idStr = getInputInfo();
	var check = getChecked();
	var opt = getOpt();
	if(opt=="addPriv"){
		if(checkForm()){
			var url = requestURL + "/setBatchPriv.act?seqId=<%=seqId%>&&idStr=" + idStr + "&&check=" + check + "&&opt=" + opt ;
			//alert(url);
		  var rtJson = getJsonRs(url);
		  if(rtJson.rtState == "0"){
		    alert('设置权限成功！');
		    location.reload();
		  }else{
				alert(rtJson.rtMsrg);
		  } 
			
		}		
	}
	if(opt == "delPriv"){
		if(checkForm()){
			msg="确定要删除权限吗？该操作作用到它下面的所有子文件夹。";
			if(window.confirm(msg)){
				var url=requestURL + "/setBatchPriv.act?seqId=<%=seqId%>&&idStr=" + idStr + "&&check=" + check + "&&opt=" + opt ;
				var rtJson = getJsonRs(url);
				if(rtJson.rtState == "0"){
					 alert('设置权限成功！');
					 location.reload();
				 }else{
					 alert(rtJson.rtMsrg);
				 } 
			}
		}
	}
}
function getInputInfo(){
	var user = document.getElementById("user").value;
	var dept = document.getElementById("dept").value;
	var role = document.getElementById("role").value;
	idStr=dept +"|"+role+"|"+ user;
	return idStr;
}

function getChecked(){
	var seles = document.getElementsByName("check");
	var idStr = "";
	for(var i=0;i<seles.length;i++){		
		if(seles[i].checked){
			idStr += seles[i].value + "," ;			
		}
	}
	return idStr; 
}
function getOpt(){
	var opts=document.getElementsByName("set_priv");
	var opt="";
	for(var i=0;i<opts.length;i++){
		if(opts[i].checked){
			return opt = opts[i].value ;			
		}
	}
}
</script>
</head>
<body class="" topmargin="5">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="small"><img src="<%=contextPath %>/core/funcs/system/filefolder/images/edit.gif" WIDTH="22" HEIGHT="20" align="middle"><span class="big3"> 批量设置</span>
                作用于本文件夹和下级所有子文件夹    </td>
  </tr>
</table>

<form action=""  method="post" name="form1" >
<table class="TableBlock" width="100%" align="center">
  <tr>
    <td nowrap class="TableContent" align="center">授权范围：<br>（人员）</td>
    <td class="TableData">
      <input type="hidden" name="user" id="user" value="">
      <textarea cols=40 name="userDesc" id="userDesc" rows=2 class="BigStatic" wrap="yes" readonly></textarea>
        <a href="javascript:;" class="orgAdd" onClick="selectUser();">添加</a>
      	<a href="javascript:;" class="orgClear" onClick="$('user').value='';$('userDesc').value='';">清空</a>
      </td>
  </tr>
  <tr>
    <td nowrap class="TableContent" align="center">授权范围：<br>（角色）</td>
    <td class="TableData">
      <input type="hidden" name="role" id="role" value="">
      <textarea cols=40 name="roleDesc" id="roleDesc" rows=2 class="BigStatic" wrap="yes" readonly></textarea>
        <a href="javascript:;" class="orgAdd" onClick="selectRole();">添加</a>
      	<a href="javascript:;" class="orgClear" onClick="$('role').value='';$('roleDesc').value='';">清空</a>
    </td>
  </tr>
  <tr>
    <td nowrap class="TableContent" align="center">授权范围：<br>（部门）</td>
    <td class="TableData">
       <input type="hidden" name="dept" id="dept" value="">
       <textarea cols=40 name="deptDesc" id="deptDesc" rows=2 class="BigStatic" wrap="yes" readonly></textarea>
        <a href="javascript:;" class="orgAdd" onClick="selectDept()">添加</a>
      	<a href="javascript:;" class="orgClear" onClick="$('dept').value='';$('deptDesc').value='';">清空</a>
    </td>
  </tr>
  <tr>
    <td nowrap class="TableContent" align="center">设置选项：</td>
    <td class="TableData">
      <input type="checkbox" name="check" id="USER_ID" value="USER_ID" checked><label for="USER_ID">访问权限</label><br>
      <input type="checkbox" name="check" id="MANAGE_USER" value="MANAGE_USER" checked><label for="MANAGE_USER">管理权限</label><br>
      <input type="checkbox" name="check" id="NEW_USER" checked value="NEW_USER"><label for="NEW_USER">新建权限</label><br>
      <input type="checkbox" name="check" id="DOWN_USER" checked value="DOWN_USER"><label for="DOWN_USER">下载/打印权限</label><br>
      <input type="checkbox" name="check" id="OWNER" checked value="OWNER"><label for="OWNER">所有者</label><br>
    </td>
  </tr>
  <tr>
    <td nowrap class="TableContent" align="center">操作：</td>
    <td class="TableData">
      <input type="radio" name="set_priv" id="add_priv" value="addPriv" checked><label for="add_priv">添加权限</label>&nbsp;&nbsp;
      <input type="radio" name="set_priv" id="remove_priv" value="delPriv" ><label for="remove_priv">移除权限</label>
    </td>
  </tr>
  <tr>
    <td nowrap  class="TableControl" colspan="2" align="center">
      <input type="hidden" name="SORT_ID" value="8">
      <input type="button" value="确定" onclick="sendForm();" class="BigButton">&nbsp;&nbsp;
      <input type="button" value="返回" class="BigButton" onclick="parent.parent.parent.location='../index.jsp'">
    </td>
  </tr>
</table>
</form>

</body>

</html>