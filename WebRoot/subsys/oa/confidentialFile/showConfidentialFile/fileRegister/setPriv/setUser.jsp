<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %> 

<%
	String seqId=request.getParameter("seqId");
	String flag=request.getParameter("fileName");
	if(seqId==null){
	  seqId="";
	}	
	 String topSeqId = request.getParameter("topSeqId");
	 if(topSeqId==null){
	   topSeqId = seqId;
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
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/confidentialFile/setConfidentialFile/js/setConfidentialFilelogic.js"></script>
<script type="text/javascript">
var requestURL = "<%=contextPath%>/yh/subsys/oa/confidentialFile/act/YHSetConfidentialSortAct";
var flag="<%=flag%>";
function sendForm(){
	//alert("ccc");
	if(flag=="USER_ID"){
		var seqId =('<%=seqId%>');
	  var user = document.getElementById("user").value;
	  var dept = document.getElementById("dept").value;
	  var role = document.getElementById("role").value;
	  var userId = dept +"|"+role+"|"+ user;
	  var override = checkOverride();
	 // alert("override:"+override);
	  var url = requestURL + "/setVisitById.act?userId=" + userId + "&seqId=" + seqId + "&override=" + override;
		var rtJson = getJsonRs(url);
	  if(rtJson.rtState == "0"){
	    alert('设置权限成功！');
	    window.location.reload();	
	  }else{
			alert(rtJson.rtMsrg);
	  }		
	}else if(flag=="MANAGE_USER") {		
		var seqId =('<%=seqId%>');
	  var dept = document.getElementById("dept").value;
	  var role = document.getElementById("role").value;
	  var user = document.getElementById("user").value;
	  var manageUser=dept+"|"+role+"|"+user;
	  var override=checkOverride();

	  var url = requestURL + "/setManageUserById.act?manageUser=" + manageUser + "&seqId=" + seqId + "&override=" + override;
	  var rtJson = getJsonRs(url);
	  if(rtJson.rtState == "0"){
	    alert('设置权限成功！');
	    //formDiv.style.display = "none";
	    //show.style.display = "";
	    //alert(rtJson.rtMsrg);
	    window.location.reload();
	  }else{
			alert(rtJson.rtMsrg);
	  }
		
	}else if(flag=="NEW_USER"){
		var seqId =('<%=seqId%>');
	  var dept = document.getElementById("dept").value;
	  var role = document.getElementById("role").value;
	  var user = document.getElementById("user").value;
	  var createId=dept+"|"+role+"|"+user;
	  //alert('seqId:'+seqId+">>>>>createId:"+createId);
	  var override=checkOverride();
	  var url = requestURL + "/setNewUserById.act?createId=" + createId + "&seqId=" + seqId + "&override=" + override;
	  var rtJson = getJsonRs(url);
	  if(rtJson.rtState == "0"){
	    alert('设置权限成功！');
	    //formDiv.style.display = "none";
	    //show.style.display = "";
	    //alert(rtJson.rtMsrg);
	    window.location.reload();
	  }else{
			alert(rtJson.rtMsrg);
	  }
	}else if(flag=="DOWN_USER"){
		var seqId =('<%=seqId%>');
	  var dept = document.getElementById("dept").value;
	  var role = document.getElementById("role").value;
	  var user = document.getElementById("user").value;
	  var downLoadId=dept+"|"+role+"|"+user;
	  //alert('seqId:'+seqId+">>>>>downLoadId:"+downLoadId);
	  var override=checkOverride();
	  var url =  requestURL + "/setDownLoadById.act?downLoadId=" + downLoadId + "&seqId=" + seqId + "&override=" + override;
	  var rtJson = getJsonRs(url);
	  if(rtJson.rtState == "0"){
	    alert('设置权限成功！');
	    //formDiv.style.display = "none";
	    //show.style.display = "";
	    //alert(rtJson.rtMsrg);
	    window.location.reload();
	  }else{
		alert(rtJson.rtMsrg);
	  }
	}
}

function doInit(){
	if(flag=="USER_ID"){
		getDept("<%=seqId%>",flag);			
		getRole("<%=seqId%>",flag);
		getUser("<%=seqId%>",flag);
	}
	if(flag=="MANAGE_USER"){
		getDept("<%=seqId%>",flag);			
		getRole("<%=seqId%>",flag);
		getUser("<%=seqId%>",flag);
	}
	if(flag=="NEW_USER"){
		getDept("<%=seqId%>",flag);			
		getRole("<%=seqId%>",flag);
		getUser("<%=seqId%>",flag);
	}
	if(flag=="DOWN_USER"){
		getDept("<%=seqId%>",flag);			
		getRole("<%=seqId%>",flag);
		getUser("<%=seqId%>",flag);
	}
}
function checkOverride(){
	var overStr=document.getElementsByName("override");
	var override="";
	if(overStr[0].checked){
		override= overStr[0].value;	
	}
	return override;
}
</script>
</head>
<body class="" topmargin="5" onload="doInit()">
<table border="0" width="1000" cellspacing="0" cellpadding="3" class="small">
  <%
  	if(flag.equals("USER_ID")){ 	 
  %>
  <tr>
    <td class="small"><img src="<%=imgPath %>/edit.gif" WIDTH="22" HEIGHT="20" align="middle"><span class="big3"> 指定权限</span>
指定访问权限，有权限的人员可以访问该文件夹、子文件夹和文件    </td>
	</tr>
	<%
		}
	%>
	 <%
  	if(flag.equals("MANAGE_USER")){ 	  
  %>	
  <tr>
  <td class="small"><img src="<%=imgPath %>/edit.gif" WIDTH="22" HEIGHT="20" align="middle"><span class="big3"> 指定权限</span>
指定管理权限，有权限的人员可以编辑和删除该文件夹下的子文件夹和文件，同时也具有Office文档的下载权限    </td>
	</tr>
	<%
  	}
	%>
	
	<%
  	if(flag.equals("NEW_USER")){ 	  
  %>	
	 <tr>
    <td class="small"><img src="<%=imgPath %>/edit.gif" WIDTH="22" HEIGHT="20" align="middle"><span class="big3"> 指定权限</span>
指定新建权限，有权限的人员可以在该文件夹和子文件夹下新建文件和文件夹     </td>
  </tr>
	<%
  	}
	%>	
		<%
  	if(flag.equals("DOWN_USER")){ 	  
  %>
	  <tr>
    <td class="small"><img src="<%=imgPath %>/edit.gif" WIDTH="22" HEIGHT="20" align="middle"><span class="big3"> 指定权限</span>
指定Office文档的下载权限，有权限的人员可以下载该文件夹和子文件夹下的Office附件文档     </td>
  </tr>
	<%
  	}
	%>
</table>
<form action=""  method="post" name="form1">
<input type="hidden" name="userId" id="userId" value="">
<table class="TableBlock" width="1000" height="100%" align="center">
  <tr>
    <td nowrap class="TableContent"" align="center">授权范围：<br>（部门）</td>
    <td class="TableData">
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
    <td nowrap class="TableContent"" align="center">选项：</td>
    <td class="TableData">
      <input type="checkbox" name="override" id="override" value="override"><label for="override">重置所有下级子文件夹的权限</label><br>
    </td>
  </tr>
  <tr>
    <td nowrap  class="TableControl" colspan="2" align="center">
      <input type="hidden" name="SORT_ID" value="5">
      <input type="hidden" name="FIELD_NAME" value="USER_ID">
      <input type="button" value="确定" onclick="sendForm()" class="BigButton">&nbsp;&nbsp;
      <input type="button" value="返回" class="BigButton" onclick="parent.parent.parent.location='../folder.jsp?seqId=<%=seqId %>'">
    </td>
</table>
</form>
</body>
</html>