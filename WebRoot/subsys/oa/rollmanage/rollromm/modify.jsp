<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ include file="/core/inc/header.jsp" %>
<%@ page import="java.text.*" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>编辑卷库</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript">

function doInit(){
  var url = "<%=contextPath%>/yh/subsys/oa/rollmanage/act/YHRmsRollRoomAct/getRmsRollRoomDetail.act?seqId=${param.seqId}";
  var json = getJsonRs(url);
  if(json.rtState == "0"){
    var data = json.rtData;
    var data = json.rtData;
    $('seqId').value = data.seqId;
    $('roomCode').value = data.roomCode;
    $('roomName').value = data.roomName;
    $('remark').value = data.remark;
    deptFunc(data.deptId);
  }else{
    alert(rtJson.rtMsrg); 
  }
  
}

function deptFunc(deptId){
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
    if(deptId && (deptId == prc.value)){
      option.selected = true;
    }
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
  var pars = Form.serialize($('form1'));
  
  var url = "<%=contextPath %>/yh/subsys/oa/rollmanage/act/YHRmsRollRoomAct/update.act";
  var json = getJsonRs(url,pars);
  if(json.rtState == "0"){
    location = "<%=contextPath %>/subsys/oa/rollmanage/rollromm/manage.jsp";
  }else{
    alert("新建卷库失败！");
  }
}
</script>
</head>
<body topmargin="5" onload="doInit()">
<table border="0" width="80%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/notify_new.gif" align="absmiddle"><span class="big3"> 编辑卷库</span>
    </td>
  </tr>
</table>

<table class="TableBlock" width="60%" align="center">
  <form action=""  method="post" name="form1" id="form1">
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
        <input type="hidden" id="seqId" name="seqId" value=""/>
        <input type="button" value="保存" class="BigButton" onclick="doSubmit();">&nbsp;&nbsp;
        <input type="button" value="返回" class="BigButton" onclick="window.location.href='<%=contextPath %>/subsys/oa/rollmanage/rollromm/manage.jsp';">&nbsp;&nbsp;
      </td>
    </tr>
  </table>
</form>
</body>
</html>