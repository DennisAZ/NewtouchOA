<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %> 
<%
	String seqId=request.getParameter("seqId");
	String flag=request.getParameter("fileName");
	if(seqId==null){
	  seqId="";
	}	
  
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
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
//alert("seqId:"+'<%=seqId%>'+"  flag:"+'<%=flag%>');
var flag='<%=flag%>';
var requestURL="<%=contextPath%>/yh/core/funcs/system/picture/act/YHPictureAct";

function sendForm(){
	//alert("aa");
	if(flag=="UPLOAD_USER"){
		var idStr=getInputInfo();
		var url =requestURL + "/setPrivateById.act?idStr=" + idStr + "&seqId=<%=seqId%>&action=UPLOAD_USER";
		var rtJson = getJsonRs(url);
		if(rtJson.rtState == "0"){
		  alert('设置权限成功！');
		  window.location.reload();	
		}else{
			alert(rtJson.rtMsrg);
		}	
	}else if(flag=="MANAGE_USER"){
		var idStr=getInputInfo();
		var url =requestURL + "/setPrivateById.act?idStr=" + idStr + "&seqId=<%=seqId%>&action=MANAGE_USER";
		var rtJson = getJsonRs(url);
		if(rtJson.rtState == "0"){
		  alert('设置权限成功！');
		  window.location.reload();	
		}else{
			alert(rtJson.rtMsrg);
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

function doInit(){
	if(flag=="UPLOAD_USER"){
		getUser(flag);
		getRole(flag);
		getDept(flag);
	}
	if(flag=="MANAGE_USER"){
		getUser(flag);
		getRole(flag);	
		getDept(flag);			
	}
}

function getUser(action){	
	var url =requestURL+"/getPersonNameStr.act?seqId=<%=seqId%>" + "&action="+action;		
	//alert("url>>"+url);  
	var json = getJsonRs(url);
	//alert(rsText);
  if(json.rtState == '1'){ 
    alert(json.rtMsrg); 
    return ; 
  }
  var prcsJson = json.rtData;
  if(prcsJson.user != "null"){
    document.getElementById("user").value = prcsJson.user;
    document.getElementById("userDesc").value = prcsJson.userDesc;
  }
}
function getRole(action){	
	var url =requestURL+"/getRoleIdStr.act?seqId=<%=seqId%>"+"&action="+action;		
	//alert("url>>"+url);  
	var json = getJsonRs(url);
	//alert(rsText);
  if(json.rtState == '1'){ 
    alert(json.rtMsrg); 
    return ; 
  }
  var prcsJson = json.rtData;
  if(prcsJson.user != "null"){
    document.getElementById("role").value = prcsJson.role;
    document.getElementById("roleDesc").value = prcsJson.roleDesc;
  }
}
function getDept(action){
	var prcsJson; 
	var url =requestURL+"/getDeptIdStr.act?seqId=<%=seqId%>"+"&action="+action;		
	var json = getJsonRs(url);
	//alert(rsText);
  if(json.rtState == '1'){ 
    alert(json.rtMsrg); 
    return ; 
  }
  prcsJson = json.rtData;
  if(prcsJson.user != "null"){
    document.getElementById("dept").value = prcsJson.dept;
    var deptDesc = prcsJson.deptDesc; 
    if(prcsJson.dept=="0" || prcsJson.dept=="ALL_DEPT"){ 
    	deptDesc = "全体部门"; 
    } 
    document.getElementById("deptDesc").value = deptDesc;
  }
}

</script>
</head>
<body onload="doInit();"> 
<table border="0" width="80%" cellspacing="0" cellpadding="3" class="small">

<% 
	if("UPLOAD_USER".equals(flag)){	  	
%>
  <tr>  
    <td class="small"><img src="<%=contextPath %>/core/funcs/system/picture/images/edit.gif" WIDTH="22" HEIGHT="20" align="middle"><span class="big3"> 设置上传权限</span>
    </td>    
  </tr>
<%
	}else if("MANAGE_USER".equals(flag)){
	  
	
%>
	<tr>  
    <td class="small"><img src="<%=contextPath %>/core/funcs/system/picture/images/edit.gif" WIDTH="22" HEIGHT="20" align="middle"><span class="big3"> 设置管理权限</span>
    </td>    
  </tr>
<%
	}
%>
</table>
<br>

<form action=""  method="post" name="form1">
<table class="TableBlock" width="80%" align="center" >
   <tr>
      <td nowrap class="TableContent"" align="center">授权范围：<br>（部门）</td>
      <td class="TableData">
        <input type="hidden" name="dept" id="dept" value="">
        <textarea cols=50 name="deptDesc" id="deptDesc" rows=4 class="BigStatic" wrap="yes" readonly></textarea>
        <a href="javascript:;" class="orgAdd" onClick="selectDept()">添加</a>
      	<a href="javascript:;" class="orgClear" onClick="$('dept').value='';$('deptDesc').value='';">清空</a>
      </td>
   </tr>
   <tr>
      <td nowrap class="TableContent"" align="center">授权范围：<br>（角色）</td>
      <td class="TableData">
        <input type="hidden" name="role" id="role" value="">
        <textarea cols=50 name="roleDesc" id="roleDesc" rows=4 class="BigStatic" wrap="yes" readonly></textarea>
        <a href="javascript:;" class="orgAdd" onClick="selectRole();">添加</a>
     	  <a href="javascript:;" class="orgClear" onClick="$('role').value='';$('roleDesc').value='';">清空</a>
      </td>
   </tr>
   <tr>
      <td nowrap class="TableContent"" align="center">授权范围：<br>（人员）</td>
      <td class="TableData">
        <input type="hidden" name="user" id="user" value="">
        <textarea cols=50 name="userDesc" id="userDesc" rows=4 class="BigStatic" wrap="yes" readonly></textarea>
        <a href="javascript:;" class="orgAdd" onClick="selectUser();">添加</a>
      	<a href="javascript:;" class="orgClear" onClick="$('user').value='';$('userDesc').value='';">清空</a>
    	</td>
   </tr>
   <tr>
    <td nowrap  class="TableControl" colspan="2" align="center">
        <input type="button" value="确定" onclick="sendForm();" class="BigButton">&nbsp;&nbsp;
        <input type="button" value="返回" class="BigButton" onClick="parent.location='../index.jsp';">
    </td>
   </tr>
  </table>
</form>



</body>
</html>