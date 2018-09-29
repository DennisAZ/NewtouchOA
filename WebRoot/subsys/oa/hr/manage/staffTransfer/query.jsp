<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>人事调动查询</title>
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
<script type="text/javascript"	src="<%=contextPath%>/subsys/oa/hr/manage/staffTransfer/js/staffTransferLogic.js"></script>
<script type="text/javascript">
function doInit(){
	getSecretFlag("HR_STAFF_TRANSFER1","transferType");
	setDate();
}

function doSubmit(){
	if(checkForm()){
		var query = $("form1").serialize(); 
		location.href = "<%=contextPath %>/subsys/oa/hr/manage/staffTransfer/search.jsp?" + query;
	}
}

function checkForm(){
  var transferDate1 = $("transferDate1").value;
  if(transferDate1){
    if(!isValidDateStr(transferDate1)){
      alert("调动日期格式不对，应形如 2010-01-02");
      $("transferDate1").focus();
      $("transferDate1").select();
      return false;
    }
  }

  var transferDate2 = $("transferDate2").value;
  if(transferDate2){
    if(!isValidDateStr(transferDate2)){
      alert("调动日期格式不对，应形如 2010-01-02");
      $("transferDate2").focus();
      $("transferDate2").select();
      return false;
    }
  }
  
  var transferEffectiveDate1 = $("transferEffectiveDate1").value;
  if(transferEffectiveDate1){
    if(!isValidDateStr(transferEffectiveDate1)){
      alert("调动生效日期格式不对，应形如 2010-01-02");
      $("transferEffectiveDate1").focus();
      $("transferEffectiveDate1").select();
      return false;
    }
  }

  var transferEffectiveDate2 = $("transferEffectiveDate2").value;
  if(transferEffectiveDate2){
    if(!isValidDateStr(transferEffectiveDate2)){
      alert("调动生效日期格式不对，应形如 2010-01-02");
      $("transferEffectiveDate2").focus();
      $("transferEffectiveDate2").select();
      return false;
    }
  }
  return true;
}

//日期
function setDate(){
  var date1Parameters = {
    inputId:'transferDate1',
    property:{isHaveTime:false}
    ,bindToBtn:'date1'
  };
  new Calendar(date1Parameters);

  var date2Parameters = {
	  inputId:'transferDate2',
	  property:{isHaveTime:false}
	  ,bindToBtn:'date2'
  };
  new Calendar(date2Parameters);

  var date3Parameters = {
    inputId:'transferEffectiveDate1',
    property:{isHaveTime:false}
    ,bindToBtn:'date3'
  };
  new Calendar(date3Parameters);

  var date4Parameters = {
    inputId:'transferEffectiveDate2',
    property:{isHaveTime:false}
    ,bindToBtn:'date4'
  };
  new Calendar(date4Parameters);
}
</script>
</head>
<body onload="doInit();">
<table border="0" width="80%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img align="absMiddle" src="<%=imgPath %>/infofind.gif"><span class="big3"> 人事调动信息查询</span></td>
  </tr>
</table>
<br>
<form action=""  method="post" name="form1" id="form1" >
 <table class="TableBlock" width="450" align="center">
    <tr>
      <td nowrap class="TableData">调动员工：</td>
      <td class="TableData">
        <input type="hidden" name="transferPerson" id="transferPerson" value="">
         <input type="text" name="transferPersonDesc" id="transferPersonDesc" size="12" class="BigStatic" readonly value="">&nbsp;
	      <a href="javascript:;" class="orgAdd" onClick="selectSingleUser(['transferPerson', 'transferPersonDesc'],null,null,1);">选择</a>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData" width="100"> 调动类型：</td>
      <td class="TableData" >
        <select name="transferType" id="transferType" title="调动类型可在“人力资源设置”->“HRMS代码设置”模块设置。">
          <option value="">调动类型&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
        </select>
      </td> 
   </tr>
    <tr>
      <td nowrap class="TableData"> 调动日期：</td>
      <td class="TableData">
        <input type="text" name="transferDate1" id="transferDate1" size="12" maxlength="10" class="BigInput" value="" readonly>
        <img id="date1" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" > 至
        <input type="text" name="transferDate2" id="transferDate2" size="12" maxlength="10" class="BigInput" value="" readonly>
        <img id="date2" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 调动生效日期：</td>
      <td class="TableData">
        <input type="text" name="transferEffectiveDate1" id="transferEffectiveDate1" size="12" maxlength="10" class="BigInput" value="" readonly>
        <img id="date3" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" > 至
        <input type="text" name="transferEffectiveDate2" id="transferEffectiveDate2" size="12" maxlength="10" class="BigInput" value="" readonly>
        <img id="date4" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 调动原因：</td>
      <td class="TableData">
        <input type="text" name="tranReason" id="tranReason" size="12" class="BigInput" value="">
      </td>
    </tr>
    <tr align="center" class="TableControl">
      <td colspan="2" nowrap>
        <input type="button" value="查询" onclick="doSubmit();" class="BigButton">&nbsp;&nbsp;
        <input type="reset" value="重填" class="BigButton">&nbsp;&nbsp;
      </td>
    </tr>
 </table>
</form>
</body>
</html>