<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>新增出国记录</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css" type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/abroad/js/util.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/abroad/js/abroadRecordLogic.js"></script>
<script type="text/javascript">
var flag = 0;
function doInit(){
  var parameters = {
      inputId:'beginDate',
      property:{isHaveTime:false}
      ,bindToBtn:'date1'
      ,callbackFun:attendDuty
  };
  new Calendar(parameters);
  
  var parameters = {
      inputId:'endDate',
      property:{isHaveTime:false}
      ,bindToBtn:'date2'
      ,callbackFun:attendDuty
  };
  new Calendar(parameters);
  
}

function doSubmit(){
  if($("abroadUserId").value.trim() == ""){ 
    alert("出国人员不能为空！");
    $("abroadUserIdDesc").focus();
    $("abroadUserIdDesc").select();
    return false;
  }
  if($("abroadName").value.trim() == ""){ 
    alert("到访国家名称不能为空！");
    $("abroadName").focus();
    $("abroadName").select();
    return false;
  }
  if($("beginDate").value.trim() == ""){ 
    alert("出国开始日期不能为空！");
    $("beginDate").focus();
    $("beginDate").select();
    return false;
  }
  if($("endDate").value.trim() == ""){ 
    alert("出国结束日期不能为空！");
    $("endDate").focus();
    $("endDate").select();
    return false;
  }
  if($("beginDate").value){
    if(!isValidDateStr(document.getElementById("beginDate").value)){
      alert("出国开始日期格式不对,应形如 2010-02-01");
      $("beginDate").focus();
      $("beginDate").select();
      return false;
    }
  }
  if($("endDate").value){
    if(!isValidDateStr(document.getElementById("endDate").value)){
      alert("出国结束日期格式不对,应形如 2010-02-01");
      $("endDate").focus();
      $("endDate").select();
      return false;
    }
  }

  if($("beginDate").value.substring(0,4) != $("endDate").value.substring(0,4)){
    alert("出国开始年份和结束年份必须是同一年份！");
    $("endDate").focus();
    $("endDate").select();
    return false;
  }
  
  if ($("beginDate").value != "" && $("endDate").value != "" && ($("beginDate").value >= $("endDate").value)) {
    alert("出国开始日期不能大于等于出国结束日期！");
    $("endDate").focus();
    $("endDate").select();
    return false;
  }
  var pars = Form.serialize($('form1'));
  var url = "<%=contextPath %>/yh/subsys/oa/abroad/act/YHHrAbroadRecordAct/addRecord.act";
  var rtJson = getJsonRs(url,pars);
  if(rtJson.rtState == "0"){
    window.location.href = contextPath + "/subsys/oa/abroad/record/submit.jsp";
  }else{
    alert(rtJson.rtMsrg); 
  }
}
</script>
</head>
<body onload="doInit()" topmargin="5">
<table border="0" width="90%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td><img src="<%=imgPath%>/notify_new.gif" align="absmiddle"><span class="big3">&nbsp;新增出国记录</span>&nbsp;&nbsp;
    </td>
  </tr>
</table>
<br>
<form enctype="multipart/form-data" action=""  method="post" name="form1" id="form1">
<table class="TableBlock" width="55%" align="center">
    <tr>
      <td nowrap class="TableData">出国人员：<font style='color:red'>*</font></td>
      <td class="TableData" colspan=3>
        <input type="hidden" name="abroadUserId" id="abroadUserId" value="">
        <textarea cols=40 name="abroadUserIdDesc" id="abroadUserIdDesc" rows=2 class="BigStatic" wrap="yes" readonly></textarea>
        <a href="javascript:;" class="orgAdd" onClick="selectUser(['abroadUserId', 'abroadUserIdDesc'],null,null,1);">添加</a>
        <a href="javascript:;" class="orgClear" onClick="$('abroadUserId').value='';$('abroadUserIdDesc').value='';">清空</a>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">到访国家名称：<font style='color:red'>*</font></td>
      <td class="TableData" colspan=3>
       <input type="text" name="abroadName" id="abroadName" size="20" class="BigInput" >
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">出国开始日期：<font style='color:red'>*</font></td>
    <td class="TableData">
     	<input type="text" name="beginDate" id="beginDate" size="12" maxlength="10" class="BigInput" value="" onClick="">
      <img id="date1" align="middle" src="/yh/core/styles/style1/img/calendar.gif" align="middle" border="0" style="cursor:pointer" >
    </td>
</tr>
 <tr>
     <td nowrap class="TableData">出国结束日期：<font style='color:red'>*</font></td>
    <td class="TableData">
     	<input type="text" name="endDate" id="endDate" size="12" maxlength="10" class="BigInput" value="" onClick="">
      <img id="date2" align="middle" src="/yh/core/styles/style1/img/calendar.gif" align="middle" border="0" style="cursor:pointer" >
    </td>
    </tr>
    <tr nowrap id="attendDuty" style="display:none;">
    <td class="TableData" colspan=5>
      <div id="listDiv"></div>
       </td>
    </tr>
     <tr>
    <td nowrap class="TableData">备注：</td>
    <td class="TableData" colspan=3>
      <textarea name="remark" id="remark" cols="62" rows="3" class="BigInput" value=""></textarea>
    </td>
  </tr>
    
    <tr align="center" class="TableControl">
      <td colspan=4 nowrap>
      <input type="hidden" name="toId" id="toId" value=""> 
      <input type="hidden" name="userIdStr" id="userIdStr" value=""> 
      <input type="hidden" name="seqId" id="seqId" value=""> 
        <input type="button" value="保存" class="BigButton" onclick="doSubmit();">
      </td>
    </tr>
  </table>
</form>
</body>
</html>