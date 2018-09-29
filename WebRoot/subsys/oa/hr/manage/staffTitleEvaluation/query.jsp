<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>职称评定查询</title>
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
<script type="text/javascript"	src="<%=contextPath%>/subsys/oa/hr/manage/staffTitleEvaluation/js/staffTitleEvaluationLogic.js"></script>
<script type="text/javascript">
function doInit(){
	getSecretFlag("HR_STAFF_TITLEEVALUATION1","getMethod");
	setDate();
}

function doSubmit(){
	if(checkForm()){
		var query = $("form1").serialize(); 
		location.href = "<%=contextPath %>/subsys/oa/hr/manage/staffTitleEvaluation/search.jsp?" + query
	}
}

function checkForm(){
  return true;
}

//日期
function setDate(){
  var date1Parameters = {
    inputId:'reportTime1',
    property:{isHaveTime:false}
    ,bindToBtn:'date1'
  };
  new Calendar(date1Parameters);

  var date2Parameters = {
	  inputId:'reportTime2',
	  property:{isHaveTime:false}
	  ,bindToBtn:'date2'
  };
  new Calendar(date2Parameters);

  var date3Parameters = {
    inputId:'receiveTime1',
    property:{isHaveTime:false}
    ,bindToBtn:'date3'
  };
  new Calendar(date3Parameters);

  var date4Parameters = {
    inputId:'receiveTime2',
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
    <td class="Big"><img align="absMiddle" src="<%=imgPath %>/infofind.gif"><span class="big3"> 职称评定信息查询</span></td>
  </tr>
</table>
<br>
<form action=""  method="post" name="form1" id="form1" >
 <table class="TableBlock" width="450" align="center">
    <tr>
      <td nowrap class="TableData">评定对象：</td>
      <td class="TableData">
        <input type="hidden" name="byEvaluStaffs" id="byEvaluStaffs" value="">
         <input type="text" name="byEvaluStaffsDesc" id="byEvaluStaffsDesc" size="12" class="BigStatic" readonly value="">&nbsp;
	      <a href="javascript:;" class="orgAdd" onClick="selectSingleUser(['byEvaluStaffs', 'byEvaluStaffsDesc'],null,null,1);">选择</a>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">批准人：</td>
      <td class="TableData">
        <input type="hidden" name="approvePerson" id="approvePerson" value="">
         <input type="text" name="approvePersonDesc" id="approvePersonDesc" size="12" class="BigStatic" readonly value="">&nbsp;
        <a href="javascript:;" class="orgAdd" onClick="selectSingleUser(['approvePerson', 'approvePersonDesc'],null,null,1);">选择</a>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 获取职称：</td>
      <td class="TableData">
        <input type="text" name="postName" id="postName" size="12" class="BigInput" value="">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData" width="100"> 获取方式：</td>
      <td class="TableData" >
        <select name="getMethod" id="getMethod" title="获取方式可在“人力资源设置”->“HRMS代码设置”模块设置。">
          <option value="">获取方式&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
        </select>
      </td> 
   </tr>
    <tr>
      <td nowrap class="TableData"> 申报时间：</td>
      <td class="TableData">
        <input type="text" name="reportTime1" id="reportTime1" size="12" maxlength="10" class="BigInput" value="" readonly>
        <img id="date1" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" > 至
        <input type="text" name="reportTime2" id="reportTime2" size="12" maxlength="10" class="BigInput" value="" readonly>
        <img id="date2" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 获取时间：</td>
      <td class="TableData">
        <input type="text" name="receiveTime1" id="receiveTime1" size="12" maxlength="10" class="BigInput" value="" readonly>
        <img id="date3" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" > 至
        <input type="text" name="receiveTime2" id="receiveTime2" size="12" maxlength="10" class="BigInput" value="" readonly>
        <img id="date4" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 聘用职务：</td>
      <td class="TableData">
        <input type="text" name="employPost" id="employPost" size="12" class="BigInput" value="">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 聘用单位：</td>
      <td class="TableData">
        <input type="text" name="employCompany" id="employCompany" size="12" class="BigInput" value="">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 评定详情：</td>
      <td class="TableData">
        <input type="text" name="remark" id="remark" size="12" class="BigInput" value="">
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