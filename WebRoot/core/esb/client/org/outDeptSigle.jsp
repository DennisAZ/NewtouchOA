<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
String deptId = request.getParameter("deptId");
if(deptId == null){
  deptId = "";
}
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>指定发送范围</title>
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

var deptId = '<%=deptId%>';
function doInit(){
  if(deptId){
	  var url = "<%=contextPath%>/yh/core/esb/client/act/YHDeptTreeAct/dept.act";
	  var rtJson = getJsonRs(url, "deptId="+deptId);
	  if(rtJson.rtState == "0"){
	    var data = rtJson.rtData;
	    $('dept').value = deptId;
	    $('deptDesc').innerHTML = data.deptName;
	    if(data.deptPermissions == 'a'){
	      
	    }
	    else{
	      $('sendDept').value = data.deptPermissions;
		    $('sendDeptDesc').innerHTML = data.deptPermissionsDesc;
		    $('assignation').checked = true;
		    $('permisDept').style.display = '';
	    }
	  }
	  else{
	    alert(rtJson.rtMsrg);
	  }
  }
}

function clickPermis(flag){
  if(flag){
    $('permisDept').style.display = '';
  }
  else{
    $('permisDept').style.display = 'none';
  }
}

function sendForm(){
  if(checkForm()){
    var url = "<%=contextPath%>/yh/core/esb/client/act/YHDeptTreeAct/setPermissions.act";
    var rtJson = getJsonRs(url, $('form1').serialize());
    if(rtJson.rtState == "0"){
      alert("权限设置成功！");
    }
    else{
      alert(rtJson.rtMsrg);
    }
  }
}

function checkForm(){
  return true;
}
</script>
</head>
<body class="" topmargin="5" onload="doInit();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="small"><img src="<%=contextPath %>/core/funcs/system/filefolder/images/edit.gif" WIDTH="22" HEIGHT="20" align="absmiddle">
    	<span class="big3"> 分发权限设置</span>
    </td>
  </tr>
</table>

<form action=""  method="post" name="form1" id="form1">
<input type="hidden" name="userId" id="userId" value="">
<table class="TableBlock" width="80%" height="100%" align="center">
  <tr>
    <td nowrap class="TableContent" align="center">授权部门：</td>
    <td class="TableData">
      <input type="hidden" name="dept" id="dept" value="">
      <textarea cols=40 name="deptDesc" id="deptDesc" rows=3 class="BigStatic" wrap="yes" readonly></textarea>
      <a href="javascript:;" class="orgAdd" onClick="selectOutDept2(['dept','deptDesc'])">添加</a>
      <a href="javascript:;" class="orgClear" onClick="$('dept').value='';$('deptDesc').value='';">清空</a>
    </td>
  </tr>

  <tr>
    <td nowrap class="TableContent" align="center" width="150px;">权限类型：</td>
    <td class="TableData">
      <label><input type="radio" id="original" name="permissions" onclick="clickPermis(0)" value="0" checked>默认权限</label>&nbsp;&nbsp;
      <label><input type="radio" id="assignation" name="permissions" onclick="clickPermis(1)" value="1">指定范围</label>
      <div style="color: red">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;默认范围：指直属上级单位及所有下级单位</div>
    </td>
  </tr>

  <tr id="permisDept" style="display:none;">
    <td nowrap class="TableContent" align="center">授权范围：</td>
    <td class="TableData">
      <input type="hidden" name="sendDept" id="sendDept" value="">
      <textarea cols=40 name="sendDeptDesc" id="sendDeptDesc" rows=3 class="BigStatic" wrap="yes" readonly></textarea>
      <a href="javascript:;" class="orgAdd" onClick="selectOutDept2(['sendDept','sendDeptDesc'])">添加</a>
      <a href="javascript:;" class="orgClear" onClick="$('sendDept').value='';$('sendDeptDesc').value='';">清空</a>
    </td>
  </tr>
  
  <tr>
    <td nowrap  class="TableControl" colspan="2" align="center">
      <input type="button" value="确定" onclick="sendForm()" class="BigButton">&nbsp;&nbsp;
    </td>
</table>
</form>
</body>
</html>