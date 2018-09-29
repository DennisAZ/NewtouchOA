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
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/hr/recruit/planApproval/js/util.js"></script>
<script type="text/javascript" src="<%=contextPath%>/subsys/oa/hr/recruit/planApproval/js/recruitPlanApprovalLogic.js"></script>
<script type="text/javascript">
function doInit(){
	setDate();
}

function doSubmit(){
	if(checkForm()){
		var query = $("form1").serialize(); 
		location.href = "<%=contextPath %>/subsys/oa/hr/recruit/planApproval/search.jsp?" + query;
	}
}

function checkForm(){
  return true;
}

//日期
function setDate(){
  var date1Parameters = {
    inputId:'startDate1',
    property:{isHaveTime:false}
    ,bindToBtn:'date1'
  };
  new Calendar(date1Parameters);

  var date2Parameters = {
	  inputId:'startDate2',
	  property:{isHaveTime:false}
	  ,bindToBtn:'date2'
  };
  new Calendar(date2Parameters);

  var date3Parameters = {
    inputId:'endDate1',
    property:{isHaveTime:false}
    ,bindToBtn:'date3'
  };
  new Calendar(date3Parameters);

  var date4Parameters = {
    inputId:'endDate2',
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
    <td class="Big"><img align="absMiddle" src="<%=imgPath %>/infofind.gif"><span class="big3"> 招聘计划查询</span></td>
  </tr>
</table>
<br>
<form action=""  method="post" name="form1" id="form1" >
 <table class="TableBlock" width="500" align="center">
    <tr>
      <td nowrap class="TableData" >计划编号：</td>
      <td class="TableData" width="180px">
        <input type="text" name="planNo" id="planNo" size="12" class="BigInput" value="">
      </td> 
      <td nowrap class="TableData">名称：</td>
      <td class="TableData" width="300px">
        <input type="text" name="planName" id="planName" size="12" class="BigInput" value="">
      </td>
   </tr>
    <tr>
      <td nowrap class="TableData">计划状态：</td>
      <td class="TableData">
        <select name="planStatus" id="planStatus">
          <option ></option>
          <option value="0">待审批</option>
          <option value="1">已批准</option>
          <option value="2">未批准</option>
        </select>
      </td>
      <td nowrap class="TableData" >审批人：</td>
      <td class="TableData" >
        <input type="hidden" name="approvePerson" id="approvePerson" value="">
        <input type="text" name="approvePersonDesc" id="approvePersonDesc" size="12" class="BigStatic" readonly value="">&nbsp;
        <a href="javascript:;" class="orgAdd" onClick="selectSingleUser(['approvePerson', 'approvePersonDesc']);">选择</a>
      </td> 
   </tr>
    <tr>
      <td nowrap class="TableData">招聘说明：</td>
      <td class="TableData">
        <input type="text" name="recruitDirection" id="recruitDirection" size="12" class="BigInput" value="">
      </td>
      <td nowrap class="TableData" >招聘备注：</td>
      <td class="TableData" >
        <input type="text" name="recruitRemark" id="recruitRemark" size="12" class="BigInput" value="">
      </td> 
   </tr>
    <tr>
      <td nowrap class="TableData">招聘开始日期：</td>
      <td class="TableData" colspan="3">
        <input type="text" name="startDate1" id="startDate1" size="12" maxlength="10" class="BigInput" value="" readonly>
        <img id="date1" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" > 至        <input type="text" name="startDate2" id="startDate2" size="12" maxlength="10" class="BigInput" value="" readonly>
        <img id="date2" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">招聘结束日期：</td>
      <td class="TableData" colspan="3">
        <input type="text" name="endDate1" id="endDate1" size="12" maxlength="10" class="BigInput" value="" readonly>
        <img id="date3" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" > 至        <input type="text" name="endDate2" id="endDate2" size="12" maxlength="10" class="BigInput" value="" readonly>
        <img id="date4" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >
      </td>
    </tr>
    <tr align="center" class="TableControl">
      <td colspan="4" nowrap>
        <input type="button" value="查询" onclick="doSubmit();" class="BigButton">&nbsp;&nbsp;
        <input type="reset" value="重填" class="BigButton">&nbsp;&nbsp;
      </td>
    </tr>
 </table>
</form>
</body>
</html>