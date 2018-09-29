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
<title>网络硬权限设置</title>
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
var flag='<%=flag%>';
var seqId='<%=seqId%>';
//alert('<%=seqId%>'+"  "+'<%=flag%>');
var requestURL="<%=contextPath%>/yh/core/funcs/system/netdisk/act/YHNetdiskAct";	

function getInputInfo(){
	var user = document.getElementById("user").value;
	var dept = document.getElementById("dept").value;
	var role = document.getElementById("role").value;
	idStr=dept +"|"+role+"|"+ user;
	return idStr;
}

function sendForm(){
	if(flag=="USER_ID"){
		//alert("sentForm:"+flag);
	  var idStr=getInputInfo();
	  //alert('seqId:'+seqId+">>>>>userId:"+idStr);
	  var url =requestURL + "/setPrivateById.act?idStr=" + idStr + "&seqId=<%=seqId%>&action=USER_ID";
	  var rtJson = getJsonRs(url);
	  if(rtJson.rtState == "0"){
	    alert('设置权限成功！');
	    window.location.reload();	
	  }else{
			alert(rtJson.rtMsrg);
	  }		
	}else if(flag=="MANAGE_USER"){
		//alert("sentForm:"+flag);
	  var idStr=getInputInfo();
	  //alert('seqId:'+seqId+">>>>>userId:"+idStr);
	  var url =requestURL + "/setPrivateById.act?idStr=" + idStr + "&seqId=<%=seqId%>&action=MANAGE_USER";
	  var rtJson = getJsonRs(url);
	  if(rtJson.rtState == "0"){
	    alert('设置权限成功！');
	    window.location.reload();	
	  }else{
			alert(rtJson.rtMsrg);
	  }	
	}else if(flag=="NEW_USER"){
		//alert("sentForm:"+flag);
	  var idStr=getInputInfo();
	  //alert('seqId:'+seqId+">>>>>userId:"+idStr);
	  var url =requestURL + "/setPrivateById.act?idStr=" + idStr + "&seqId=<%=seqId%>&action=NEW_USER";
	  var rtJson = getJsonRs(url);
	  if(rtJson.rtState == "0"){
	    alert('设置权限成功！');
	    window.location.reload();	
	  }else{
			alert(rtJson.rtMsrg);
	  }	
	}else if(flag=="DOWN_USER"){
		//alert("sentForm:"+flag);
	  var idStr=getInputInfo();
	  //alert('seqId:'+seqId+">>>>>userId:"+idStr);
	  var url =requestURL + "/setPrivateById.act?idStr=" + idStr + "&seqId=<%=seqId%>&action=DOWN_USER";
	  var rtJson = getJsonRs(url);
	  if(rtJson.rtState == "0"){
	    alert('设置权限成功！');
	    window.location.reload();	
	  }else{
			alert(rtJson.rtMsrg);
	  }	
	}	
}

function doInit(){
	if(flag=="USER_ID"){
		getUser(flag);
		getRole(flag);
		getDept(flag);
	}
	if(flag=="MANAGE_USER"){
		getUser(flag);
		getRole(flag);	
		getDept(flag);			
	}
	if(flag=="NEW_USER"){
		getUser(flag);
		getRole(flag);	
		getDept(flag);			
	}
	if(flag=="DOWN_USER"){
		getUser(flag);
		getRole(flag);	
		getDept(flag);			
	}
}
function getUser(action){	
	var url =requestURL+"/getPersonIdStr.act?seqId=<%=seqId%>" + "&action="+action;		
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
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <%
  	if(flag.equals("USER_ID")){ 	 
  	  //System.out.println("flag>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>+++++++++++"+flag);
  	
  %>
  <tr>
    <td class="small"><img src="<%=contextPath %>/core/funcs/system/filefolder/images/edit.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3"> 指定权限</span>
指定访问权限，有权限的人员可以访问该文件夹、子文件夹和文件     </td>
	</tr>
	<%
		}
	%>
	 <%
  	if(flag.equals("MANAGE_USER")){ 	  
  	
  %>	
  <tr>
  <td class="small"><img src="<%=contextPath %>/core/funcs/system/filefolder/images/edit.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3"> 指定权限</span>
指定管理权限，有权限的人员可以编辑和删除该文件夹下的子文件夹和文件，也可在该文件夹和子文件夹下创建新文件夹    </td>
	</tr>
	<%
  	}
	%>
	
	<%
  	if(flag.equals("NEW_USER")){ 	  
  	
  %>	
	 <tr>
    <td class="small"><img src="<%=contextPath %>/core/funcs/system/filefolder/images/edit.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3"> 指定权限</span>
指定新建权限，有权限的人员可以在该文件夹和子文件夹下新建文件     </td>
  </tr>
	<%
  	}
	%>	
	
		<%
  	if(flag.equals("DOWN_USER")){ 	  
  	
  %>
	  <tr>
    <td class="small"><img src="<%=contextPath %>/core/funcs/system/filefolder/images/edit.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3"> 指定权限</span>
指定Office文档的下载权限，有权限的人员可以下载该文件夹和子文件夹下的Office附件文档     </td>
  </tr>
	<%
  	}
	%>  
</table>
<form action=""  method="post" name="form1">
<input type="hidden" name="userId" id="userId" value="">
<table class="TableBlock" width="100%" height="100%" align="center">
  <tr>
    <td nowrap class="TableContent"" align="center">授权范围：<br>（部门）</td>
    <td  class="TableData">
      <input type="hidden" name="dept" id="dept" value="">
      <textarea cols=40 name="deptDesc" id="deptDesc" rows=8 class="BigStatic" wrap="yes" readonly></textarea>
      <a href="javascript:;" class="orgAdd" onClick="selectDept()">添加</a>
      <a href="javascript:;" class="orgClear" onClick="$('dept').value='';$('deptDesc').value='';">清空</a>
    </td>
  </tr>
  <tr>
    <td nowrap class="TableContent"" align="center">授权范围：<br>（角色）</td>
    <td class="TableData">
      <input type="hidden" name="role" id="role" value="">
      <textarea cols=40 name="roleDesc" id="roleDesc" rows=8 class="BigStatic" wrap="yes" readonly></textarea>
      <a href="javascript:;" class="orgAdd" onClick="selectRole();">添加</a>
      <a href="javascript:;" class="orgClear" onClick="$('role').value='';$('roleDesc').value='';">清空</a>
    </td>
  </tr>
  <tr>
    <td nowrap class="TableContent"" align="center">授权范围：<br>（人员）</td>
    <td class="TableData">
      <input type="hidden" name="user" id="user" value="">
      <textarea cols=40 name="userDesc" id="userDesc" rows=8 class="BigStatic" wrap="yes" readonly></textarea>
      <a href="javascript:;" class="orgAdd" onClick="selectUser();">添加</a>
      <a href="javascript:;" class="orgClear" onClick="$('user').value='';$('userDesc').value='';">清空</a>
    </td>
  </tr>

  <tr>
    <td nowrap  class="TableControl" colspan="2" align="center">
      <input type="button" value="确定" onclick="sendForm()" class="BigButton">&nbsp;&nbsp;
      <input type="button" value="返回" class="BigButton" onclick="parent.location='../index.jsp'">
    </td>
</table>
</form>


</body>
</html>