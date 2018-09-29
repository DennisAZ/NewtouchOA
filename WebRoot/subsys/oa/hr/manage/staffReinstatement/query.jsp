<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>员工复职查询</title>
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
<script type="text/javascript"	src="<%=contextPath%>/subsys/oa/hr/manage/staffReinstatement/js/staffReinstatementLogic.js"></script>
<script type="text/javascript">
function doInit(){
	getSecretFlag("HR_STAFF_REINSTATEMENT1","reappointmentType");
	setDate();
}

function doSubmit(){
	var query = $("form1").serialize(); 
	location.href = "<%=contextPath %>/subsys/oa/hr/manage/staffReinstatement/search.jsp?" + query
}

//日期
function setDate(){
  var date1Parameters = {
    inputId:'applicationDate1',
    property:{isHaveTime:false}
    ,bindToBtn:'date1'
  };
  new Calendar(date1Parameters);

  var date2Parameters = {
	  inputId:'applicationDate2',
	  property:{isHaveTime:false}
	  ,bindToBtn:'date2'
  };
  new Calendar(date2Parameters);

  var date3Parameters = {
    inputId:'reappointmentTimeFact1',
    property:{isHaveTime:false}
    ,bindToBtn:'date3'
  };
  new Calendar(date3Parameters);

  var date4Parameters = {
    inputId:'reappointmentTimeFact2',
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
    <td class="Big"><img align="absMiddle" src="<%=imgPath %>/infofind.gif"><span class="big3"> 员工复职信息查询</span></td>
  </tr>
</table>
<br>
<form action=""  method="post" name="form1" id="form1" >
 <table class="TableBlock" width="450" align="center">
    <tr>
      <td nowrap class="TableData">复职人员：</td>
      <td class="TableData">
        <input type="hidden" name="reinstatementPerson" id="reinstatementPerson" value="">
         <input type="text" name="reinstatementPersonDesc" id="reinstatementPersonDesc" size="12" class="BigStatic" readonly value="">&nbsp;
	      <a href="javascript:;" class="orgAdd" onClick="selectSingleUser(['reinstatementPerson', 'reinstatementPersonDesc'],null,null,1);">选择</a>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData" width="100"> 复职类型：</td>
      <td class="TableData" >
        <select name="reappointmentType" id="reappointmentType" title="复职类型可在“人力资源设置”->“HRMS代码设置”模块设置。">
          <option value="">复动类型&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
        </select>
      </td> 
   </tr>
    <tr>
      <td nowrap class="TableData"> 申请日期：</td>
      <td class="TableData">
        <input type="text" name="applicationDate1" id="applicationDate1" size="12" maxlength="10" class="BigInput" value="" readonly>
        <img id="date1" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" > 至
        <input type="text" name="applicationDate2" id="applicationDate2" size="12" maxlength="10" class="BigInput" value="" readonly>
        <img id="date2" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 实际复职日期：</td>
      <td class="TableData">
        <input type="text" name="reappointmentTimeFact1" id="reappointmentTimeFact1" size="12" maxlength="10" class="BigInput" value="" readonly>
        <img id="date3" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" > 至
        <input type="text" name="reappointmentTimeFact2" id="reappointmentTimeFact2" size="12" maxlength="10" class="BigInput" value="" readonly>
        <img id="date4" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 复职说明：</td>
      <td class="TableData">
        <input type="text" name="reappointmentState" id="reappointmentState" size="12" class="BigInput" value="">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 复职手续办理：</td>
      <td class="TableData">
        <input type="text" name="materialsCondition" id="materialsCondition" size="12" class="BigInput" value="">
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