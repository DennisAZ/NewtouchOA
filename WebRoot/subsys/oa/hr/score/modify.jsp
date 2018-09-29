<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>考核指标集</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/hr/score/js/util.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/hr/score/js/scoreLogic.js"></script>
<script type="text/javascript">

function doInit(){
  var url = "<%=contextPath%>/yh/subsys/oa/hr/score/act/YHScoreGroupAct/getScoreGroupDetail.act?seqId=${param.seqId}";
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "0"){
    var data = rtJson.rtData;
    bindJson2Cntrl(rtJson.rtData);
    var groupRefer = data.groupRefer;
    $("role").value = data.userPriv;
    if($("role") && $("role").value.trim()){
      bindDesc([{cntrlId:"role", dsDef:"USER_PRIV,SEQ_ID,PRIV_NAME"}]);
    }
    
    checkedBox(groupRefer);

  }else{
    alert(rtJson.rtMsrg); 
  }
}

function checkedBox(groupRefer){
  var groupReferStr = groupRefer.split(',');
  for(var i = 0; i < groupReferStr.length - 1; i++){
    if(groupReferStr[i] == "DIARY"){
      $("diaryId").checked = true;
    }
    if(groupReferStr[i] == "CALENDAR"){
      $("calendarId").checked = true;
    }
  }
}

function doSubmit(){
  if($("groupName").value.trim() == ""){ 
    alert("考核指标集名称不能空！！！");
    $("groupName").focus();
    $("groupName").select();
    return false;
  }
  if($("role").value.trim() == ""){ 
    alert("范围(角色)不能空！！！");
    $("roleDesc").focus();
    $("roleDesc").select();
    return false;
  }
  checkDiary();
  checkCalendar();
  var pars = Form.serialize($('form1'));
  var url = "<%=contextPath %>/yh/subsys/oa/hr/score/act/YHScoreGroupAct/updateScoreGroup.act";
  var rtJson = getJsonRs(url,pars);
  if(rtJson.rtState == "0"){
    window.location.href = "<%=contextPath %>/subsys/oa/hr/score/index.jsp";
  }else{
    alert(rtJson.rtMsrg); 
  }
}
</script>
</head>
<body onload="doInit()" topmargin="5">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/score.gif" WIDTH="22" HEIGHT="20" align="absmiddle"> <span class="big3">考核指标集管理</span>
    </td>
  </tr>
</table>

<br>
 <table width="450" align="center" class="TableBlock">
  <form action=""  method="post" name="form1" id="form1">
    <tr>
      <td nowrap class="TableData">考核指标集名称：<font style='color:red'>*</font></td>
      <td class="TableData">
         <INPUT type="text"name="groupName" id="groupName" class=BigInput size="20" value="">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">范围(角色)：<font style='color:red'>*</font></td>
      <td class="TableData">
        <input type="hidden" name="role" id="role" value="">
        <textarea cols="30" name="roleDesc" id="roleDesc" rows=2 class="BigStatic" wrap="yes" readonly></textarea>
        <a href="javascript:;" class="orgAdd" onClick="selectRole();">添加</a>
        <a href="javascript:;" class="orgClear" onClick="$('role').value='';$('roleDesc').value='';">清空</a>
      </td>
   </tr>
    <tr>
      <td nowrap class="TableData">考核指标集描述：</td>
      <td class="TableData">
        <textarea name="groupDesc" id="groupDesc" cols="45" rows="5" class="BigInput"></textarea>
      </td>
    </tr>
     <tr>
      <td nowrap class="TableData">设定考核依据模块：</td>
      <td class="TableData">
       <input type="checkbox" name="diaryId" id="diaryId"><label for="diaryId">个人工作日志</label>&nbsp;&nbsp;<input type="checkbox" name="calendarId" id="calendarId"></input><label for="calendarId">个人日程安排</label>    
      </td>
    </tr>
    <tfoot align="center" class="TableFooter">
      <td colspan="2" nowrap>
      <input type="hidden" name="seqId" id="seqId">  
        <input type="button" value="确定" class="BigButton" onclick="doSubmit();">&nbsp;&nbsp;
        <input type="button" value="返回" class="BigButton" onClick="location='<%=contextPath%>/subsys/oa/hr/score/index.jsp';">
      </td>
    </tfoot>
  </table>
</form>
</body>
</html>