<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>新建个人补登记</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css" type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/fillRegister/js/util.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/fillRegister/js/attendScoreLogic.js"></script>
<script type="text/javascript">
var flag = 0;
function doInit(){
  //moblieSmsRemind('sms2Remind3','sms2Check');
  //getSysRemind();
  setDate();
}

//日期
function setDate(){
  var date1Parameters = {
      inputId:'beginDate',
      property:{isHaveTime:false}
      ,bindToBtn:'date1'
      ,callbackFun:attendDuty
    };
    new Calendar(date1Parameters);
    var date2Parameters = {
      inputId:'endDate',
      property:{isHaveTime:false}
      ,bindToBtn:'date2'
      ,callbackFun:attendDuty
    };
    new Calendar(date2Parameters);
}

function doSubmit(){
  if($("proposer").value == ""){ 
    alert("申请人不能为空！");
    $("proposerDesc").focus();
    return false;
  }
  if($("beginDate").value == ""){ 
    alert("补登记开始日期不能为空！");
    $("beginDate").focus();
    return false;
  }
  if($("endDate").value == ""){ 
    alert("补登记结束日期不能为空！");
    $("endDate").focus();
    return false;
  }
  if ($("beginDate").value != "" && $("endDate").value != "" && ($("beginDate").value > $("endDate").value)) {
    alert("错误 补登记起始日期大于结束日期！");
    $("endDate").focus();
    $("endDate").select();
    return false;
  }
  if($("beginDate").value){
    if(!isValidDateStr(document.getElementById("beginDate").value)){
      alert("补登记开始日期格式不对,应形如 2010-02-01");
      $("beginDate").focus();
      $("beginDate").select();
      return false;
    }
  }
  if($("endDate").value){
    if(!isValidDateStr(document.getElementById("endDate").value)){
      alert("补登记结束日期格式不对,应形如 2010-02-01");
      $("endDate").focus();
      $("endDate").select();
      return false;
    }
  }
  var idStrs = checkMags('deleteFlag');
  
  var pars = Form.serialize($('form1'));
  var url = "<%=contextPath %>/yh/subsys/oa/fillRegister/act/YHAttendFillAct/addVolume.act?idStrs="+idStrs;
  var rtJson = getJsonRs(url,pars);
  if(rtJson.rtState == "0"){
    window.location.href = contextPath + "/subsys/oa/fillRegister/attendScore/submit.jsp";
  }else{
    alert(rtJson.rtMsrg); 
  }
}

</script>
</head>
<body onload="doInit();">
<table border="0" width="90%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td><img src="<%=imgPath %>/notify_new.gif" align="middle"><span class="big3"> 新建批量补登记</span>&nbsp;&nbsp;
    </td>
  </tr>
</table>
<br>
<form enctype="multipart/form-data" action=""  method="post" name="form1" id="form1" onsubmit="return CheckForm();">
<table class="TableBlock" width="60%" align="center">
    <tr>
      <td nowrap class="TableData">申请人：<font style='color:red'>*</font></td>
      <td class="TableData" colspan=3>
        <input type="hidden" name="proposer" id="proposer" value="">
        <textarea cols=40 name="proposerDesc" id="proposerDesc" rows=2 class="BigStatic" wrap="yes" readonly></textarea>
        <a href="javascript:;" class="orgAdd" onClick="selectUser(['proposer', 'proposerDesc']);">添加</a>
        <a href="javascript:;" class="orgClear" onClick="ClearUser('proposer', 'proposerDesc')">清空</a>
      </td>
    </tr>
  <tr>
     <td nowrap class="TableData">补登记日期：<font color="red">*</font></td>
    <td class="TableData">
     	<input type="text" name="beginDate" id="beginDate" size="10" maxlength="10" class="BigInput" value="" onClick="">
    	<img id="date1" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >
      至
      <input type="text" name="endDate" id="endDate" size="10" maxlength="10" class="BigInput" value="" onClick="">
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
    	<input type="hidden" id="tContent" name="tContent" value="">
      <input type="button" value="保存" onclick="doSubmit();" class="BigButton">
    </td>
  </tr>
</table>
</form>

</body>
</html>