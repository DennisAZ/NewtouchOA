<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>员工关怀查询</title>
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
<script type="text/javascript"	src="<%=contextPath%>/subsys/oa/hr/manage/staffCare/js/staffCareLogic.js"></script>
<script type="text/javascript">
function doInit(){
	getSecretFlag("HR_STAFF_CARE1","careType");
	setDate();
}

function doSubmit(){
	if(checkForm()){
		var query = $("form1").serialize(); 
		location.href = "<%=contextPath %>/subsys/oa/hr/manage/staffCare/search.jsp?" + query
	}
}

function checkForm(){
  var careDate1 = $("careDate1").value;
  if(careDate1){
    if(!isValidDateStr(careDate1)){
      alert("调动日期格式不对，应形如 2010-01-02");
      $("careDate1").focus();
      $("careDate1").select();
      return false;
    }
  }

  var careDate2 = $("careDate2").value;
  if(careDate2){
    if(!isValidDateStr(careDate2)){
      alert("调动日期格式不对，应形如 2010-01-02");
      $("careDate2").focus();
      $("careDate2").select();
      return false;
    }
  }

  var careFees1 = $("careFees1").value;
  if(careFees1){
    if(!checkRate(careFees1)){
        alert("您填写的关怀开支费用格式错误，请输入正整数");
        $("careFees1").focus();
        $("careFees1").select();
      return (false);
    }
  }

  var careFees2 = $("careFees2").value;
  if(careFees2){
    if(!checkRate(careFees2)){
        alert("您填写的关怀开支费用格式错误，请输入正整数");
        $("careFees2").focus();
        $("careFees2").select();
      return (false);
    }
  }
  return true;
}

//判断正整数  
function checkRate(input){ 
  var re = /^[1-9]+[0-9]*]*$/;
  if(!re.test(input)) {  
    return false;  
  }  
  return true;
}  

//日期
function setDate(){
  var date1Parameters = {
    inputId:'careDate1',
    property:{isHaveTime:false}
    ,bindToBtn:'date1'
  };
  new Calendar(date1Parameters);

  var date2Parameters = {
	  inputId:'careDate2',
	  property:{isHaveTime:false}
	  ,bindToBtn:'date2'
  };
  new Calendar(date2Parameters);
}
</script>
</head>
<body onload="doInit();">
<table border="0" width="80%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img align="absMiddle" src="<%=imgPath %>/infofind.gif"><span class="big3"> 员工关怀信息查询</span></td>
  </tr>
</table>
<br>
<form action=""  method="post" name="form1" id="form1" >
  <table class="TableBlock" width="450" align="center">
	  <tr>
      <td nowrap class="TableData" width="100"> 关怀类型：</td>
      <td class="TableData" >
        <select name="careType" id="careType" title="关怀类型可在“人力资源设置”->“HRMS代码设置”模块设置。">
          <option value="">关怀类型&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
        </select>
      </td> 
   </tr>
    <tr>
      <td nowrap class="TableData">被关怀员工：</td>
      <td class="TableData">
        <input type="hidden" name="byCareStaffs" id="byCareStaffs" value="">
         <input type="text" name="byCareStaffsDesc" id="byCareStaffsDesc" size="12" class="BigStatic" readonly value="">&nbsp;
        <a href="javascript:;" class="orgAdd" onClick="selectSingleUser(['byCareStaffs', 'byCareStaffsDesc'],null,null,1);">选择</a>
      </td>
    </tr>
    <tr>
    <tr>
      <td nowrap class="TableData"> 关怀日期：</td>
      <td class="TableData">
        <input type="text" name="careDate1" id="careDate1" size="12" maxlength="10" class="BigInput" value="" readonly>
        <img id="date1" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" > 至
        <input type="text" name="careDate2" id="careDate2" size="12" maxlength="10" class="BigInput" value="" readonly>
        <img id="date2" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 关怀开支费用：</td>
      <td class="TableData">
        <input type="text" name="careFees1" id="careFees1" size="12" maxlength="10" class="BigInput" value="" >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;至
        <input type="text" name="careFees2" id="careFees2" size="12" maxlength="10" class="BigInput" value="" >
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">参与人：</td>
      <td class="TableData">
        <input type="hidden" name="participants" id="participants" value="">
         <input type="text" name="participantsDesc" id="participantsDesc" size="12" class="BigStatic" readonly value="">&nbsp;
        <a href="javascript:;" class="orgAdd" onClick="selectSingleUser(['participants', 'participantsDesc'],null,null,1);">选择</a>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 关怀内容：</td>
      <td class="TableData">
        <input type="text" name="careContent" id="careContent" size="12" class="BigInput" value="">
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