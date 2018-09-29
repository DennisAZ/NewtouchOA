<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>会议室设置</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/fck/fckeditor/fckeditor.js"></script> 
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/meeting/js/util.js"></script>
<script type="text/javascript">

function doInit(){
 
}
function checkForm(){
	if($("mrName").value.trim() == ""){ 
		alert("会议室名称不能为空！");
		$("mrName").focus();
		$("mrName").select();
		return false;
	}
	if($("mrCapacity").value.trim() && !isNumber($("mrCapacity").value.trim())){
		alert("可容纳人数应为整数");
		$("mrCapacity").focus();
		$("mrCapacity").select();
		return false;
	}
	return true;
}

function doSubmit(){
	if(checkForm()){
		var pars = Form.serialize($('form1'));
	  var url = "<%=contextPath %>/yh/subsys/oa/meeting/act/YHMeetingRoomAct/addMeetingRoom.act";
	  var json = getJsonRs(url,pars);
	  if(json.rtState == "0"){
	    $("listContainer").style.display = 'none';
	    WarningMsrg('保存成功', 'msrg');
	    $("innputDiv").style.display = '';
	  }else{
	  	alert(json.rtMsrg); 
	  }
	}
  
}

</script>
</head>
<body onload="doInit()" topmargin="5">
<div id="listContainer" style="display:'';width:100;">

<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
<tr>
  <td class="Big"><img src="<%=imgPath%>/notify_new.gif" align="absmiddle"><span class="big3"> 新建会议室</span>
  </td>
</tr>
</table><br>

<form enctype="multipart/form-data" action="" method="post" name="form1" id="form1">
<table class="TableBlock" align="center" width="580">
<tr>
 <td nowrap class="TableData" width="120">会议室名称：<font style='color:red'>*</font></td>
 <td class="TableData" colspan="3">
  <input type="text" name="mrName" id="mrName" size="30" maxlength="100" class="BigInput" value="">
 </td>
</tr>
<tr>
 <td nowrap class="TableData">会议室描述：</td>
 <td class="TableData" colspan="3">
  <textarea name="mrDesc" id="mrDesc" class="BigInput" cols="40" rows="3"></textarea>
 </td>
</tr>
<tr bgcolor="#CCCCCC">
    <td class="TableData">会议室管理员：</td>
    <td class="TableData">
    <input type="hidden" name="operator" id="operator" value="">
    <textarea cols=35 name="operatorDesc" id="operatorDesc" rows=3 class="BigStatic" wrap="yes" readonly></textarea>
    <a href="javascript:;" class="orgAdd" onClick="selectUser(['operator', 'operatorDesc']);">添加</a>
     <a href="javascript:;" class="orgClear" onClick="$('operator').value='';$('operatorDesc').value='';">清空</a>
    </td>
  </tr>
<tr>	
<tr bgcolor="#CCCCCC">
    <td class="TableData">申请权限(部门)：</td>
    <td class="TableData">
    <input type="hidden" name="toId" id="toId" value="">
    <textarea cols=35 name="toIdDesc" id="toIdDesc" rows=3 class="BigStatic" wrap="yes" readonly></textarea>
    <a href="javascript:;" class="orgAdd" onClick="selectDept(['toId', 'toIdDesc']);">添加</a>
     <a href="javascript:;" class="orgClear" onClick="$('toId').value='';$('toIdDesc').value='';">清空</a>
    </td>
  </tr>
<tr>	
<tr bgcolor="#CCCCCC">
    <td class="TableData">申请权限(人员)：</td>
    <td class="TableData">
    <input type="hidden" name="secretToId" id="secretToId" value="">
    <textarea cols=35 name="secretToIdDesc" id="secretToIdDesc" rows=3 class="BigStatic" wrap="yes" readonly></textarea>
    <a href="javascript:;" class="orgAdd" onClick="selectUser(['secretToId', 'secretToIdDesc']);">添加</a>
     <a href="javascript:;" class="orgClear" onClick="$('secretToId').value='';$('secretToIdDesc').value='';">清空</a>
    </td>
  </tr>
<tr>
 <td nowrap class="TableData">可容纳人数：</td>
 <td class="TableData" colspan="3">
  <input type="text" name="mrCapacity" id="mrCapacity" size="5" maxlength="10" class="BigInput" value="">
 </td>
</tr>
<tr>
 <td nowrap class="TableData">设备情况：</td>
 <td class="TableData" colspan="3">
  <textarea name="mrDevice" id="mrDevice" class="BigInput" cols="40" rows="3"></textarea>
 </td>
</tr>
<tr>
 <td nowrap class="TableData">地址：</td>
 <td class="TableData" colspan="3">
  <input type="text" name="mrPlace" id="mrPlace" size="40" maxlength="100" class="BigInput" value="">
 </td>
</tr>
<tr class="TableControl">
 <td nowrap colspan="4" align="center">
 	<input type="hidden" value="" name="mrId" id="mrId">
  <input type="button" value="保存" class="BigButton" onclick="doSubmit();">&nbsp;&nbsp;
   <input type="button" class="BigButton" value="返回" onclick="location='<%=contextPath%>/subsys/oa/meeting/meetingroom/index.jsp';">
 </td>
</tr>
</table>
</form>
</div>
<div id="msrg">
</div>
<br>
<div id="innputDiv" style="display:none" align="center">
  <input type="button" class="BigButton" value="返回" onclick="location='<%=contextPath%>/subsys/oa/meeting/meetingroom/index.jsp';">
</div>
</body>
</html>