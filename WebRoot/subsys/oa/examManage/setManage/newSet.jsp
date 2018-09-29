<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>新增培训记录</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript">

function doInit(){
}

function doSubmit(){
  if($("roomName").value.trim() == ""){ 
    alert("题库名称不能为空！");
    $("roomName").focus();
    $("roomName").select();
    return false;
  }

  var pars = Form.serialize($('form1'));
  var url = "<%=contextPath %>/yh/subsys/oa/examManage/act/YHExamQuizSetAct/addBank.act";
  var rtJson = getJsonRs(url,pars);
  if(rtJson.rtState == "0"){
    window.location.href = "<%=contextPath %>/subsys/oa/examManage/setManage/manage.jsp";
  }else{
    alert(rtJson.rtMsrg); 
  }
}
</script>
</head>
<body onload="doInit()" topmargin="5">
<table border="0" width="90%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/notify_new.gif" align="absmiddle"><span class="big3">新建题库</span>
    </td>
  </tr>
</table>
<br>
<form enctype="multipart/form-data" action=""  method="post" name="form1" id="form1">
  <table class="TableBlock" width="70%" align="center">
    <tr>
      <td nowrap class="TableData"> 题库编号：</td>
      <td class="TableData">
        <input type="text" name="roomCode" id="roomCode" size="20" maxlength="20" class="BigInput" value="">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 题库名称：<font style='color:red'>*</font></td>
      <td class="TableData">
        <input type="text" name="roomName" id="roomName" size="40" maxlength="40" class="BigInput" value="">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">题库说明：</td>
      <td class="TableData">
        <textarea name="roomDesc" class="BigInput" id="roomDesc" cols="50" rows="3"></textarea>
      </td>
    </tr>
   <tr align="center" class="TableControl">
      <td colspan="2" nowrap>
        <input type="hidden" name="seqId" value="">
        <input type="button" value="保存" class="BigButton" onclick="doSubmit();">&nbsp;&nbsp;
        <input type="reset" value="重填" class="BigButton">&nbsp;&nbsp;
      </td>
    </tr>
  </table>
</form>
</body>
</html>