<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page import="java.text.*" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>新建卷库</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript">

function doInit(){
  deptFunc();
}

function deptFunc(){
  var url = "<%=contextPath%>/yh/subsys/oa/rollmanage/act/YHRmsRollRoomAct/selectDeptToAttendance.act";
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "1"){
    alert(rtJson.rtMsrg); 
    return ;
  }
  var userId = rtJson.rtMsrg;
  var prcs = rtJson.rtData;
  var selects = document.getElementById("deptId");
  for(var i=0;i<prcs.length;i++){
    var prc = prcs[i];
    var option = document.createElement("option"); 
    option.value = prc.value; 
    option.innerHTML = prc.text; 
    selects.appendChild(option);
  }
  return userId;
}

function doSubmit(){
  if($("roomCode").value.trim() == ""){ 
    alert("案卷号不能为空！");
    $("roomCode").focus();
    $("roomCode").select();
	return false;
  }

  if($("roomName").value.trim() == ""){ 
    alert("案卷名称不能为空！");
    $("roomName").focus();
    $("roomName").select();
	return false;
  }
 var roomCode= document.getElementById("roomCode").value;
 var pars = $("form1").serialize();
  
  var url = "<%=contextPath %>/yh/subsys/oa/rollmanage/act/YHRmsRollRoomAct/add.act";

  var json = getJsonRs(url,pars);
  if(json.rtState == "0"){
    window.location.href = "<%=contextPath %>/subsys/oa/rollmanage/rollromm/add.jsp";
  }else{
    alert("修改失败");
  }
}
</script>
</head>
<body topmargin="5" onload="doInit()">
<table border="0" width="80%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/notify_new.gif" align="absmiddle"><span class="big3"> 新建卷库</span>
    </td>
  </tr>
</table>
 <form action=""  method="post" name="form1" id="form1">
<table class="TableBlock" width="60%" align="center">
    <tr>
      <td nowrap class="TableData"> 卷库号：<font style='color:red'>*</font></td>
      <td class="TableData"> 
       <INPUT name="roomCode" id="roomCode" size=20 maxlength="100" class="BigInput" value="">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 卷库名称：<font style='color:red'>*</font></td>
      <td class="TableData"> 
       <INPUT name="roomName" id="roomName" size=30 maxlength="100" class="BigInput" value="">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 所属部门：</td>
      <td class="TableData"> 
 <select name="deptId" id="deptId" class="inputSelect">
   <option value="" ></option>
 </select>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 备注：</td>
      <td class="TableData"> 
        <input type="text" name="remark" id="remark" value="" size="40" maxlength="100" class="BigInput" value="">
      </td>
    </tr>
    <tr align="center" class="TableControl">
      <td colspan="2" nowrap>
        <input type="hidden" name="OP" value="">
        <input type="button" value="新建" class="BigButton" onclick="doSubmit();">&nbsp;&nbsp;
        <input type="reset" value="重置" class="BigButton">&nbsp;&nbsp;
      </td>
    </tr>
  </table>
</form>
</body>
</html>