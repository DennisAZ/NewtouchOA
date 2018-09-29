<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>培训计划查询</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href = "<%=cssPath%>/cmp/Calendar.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/training/js/trainingPlanllogic.js"></script>
<script type="text/javascript">
var requestURL = "<%=contextPath%>/yh/subsys/oa/training/act/YHHrTrainingPlanAct";
function doInit(){
	getSecretFlag("T_COURSE_TYPE","tCourseTypes");
	setDate();
}

//日期
function setDate(){
var date1Parameters = {
   inputId:'courseStartDate1',
   property:{isHaveTime:false}
   ,bindToBtn:'date1'
};
new Calendar(date1Parameters);
var date2Parameters = {
   inputId:'courseStartDate2',
   property:{isHaveTime:false}
   ,bindToBtn:'date2'
};
new Calendar(date2Parameters);
}

function loadWindow(){
  var url= contextPath + "/subsys/oa/training/approval/plannoinfo/index.jsp";
  loc_x = document.body.scrollLeft + event.clientX - event.offsetX + 800;
  loc_y = document.body.scrollTop + event.clientY - event.offsetY + 500;
  window.showModalDialog(url,self,"edge:raised;scroll:0;status:0;help:0;resizable:1;dialogWidth:320px;dialogHeight:245px;dialogTop:"+loc_y+"px;dialogLeft:"+loc_x+"px");
}

function doSubmit(){
	if(checkDate("courseStartDate1","courseStartDate2")){
		var query = $("form1").serialize();
		location = "<%=contextPath%>/subsys/oa/training/plan/searTrainingPlan.jsp?"+query;
	}
}

function checkDate(startDateStr,endDateStr){
	var startDate = $(startDateStr);
	var endDate = $(endDateStr);
	if(startDate.value && endDate.value){
    if($(startDate).value > $(endDate).value){
      alert(" 开课日期的结束查询时间不能小于开课日期的开始查询时间！");
      startDate.focus(); 
      startDate.select(); 
      return false;
    }
  }
  if(startDate.value){
    if(!isValidDateStr(startDate.value)){
      alert("起始日期格式不对，应形如  2010-01-02");
      startDate.focus();
      startDate.select();
      return false;
    }
  }
  if(endDate.value){
    if(!isValidDateStr(endDate.value)){
      alert("终止日期格式不对，应形如  2010-01-02");
      endDate.focus();
      endDate.select();
      return false;
    }
  }
  return true;
}


</script>
</head>
<body onload="doInit();">
<table border="0" width="80%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img align="middle" src="<%=imgPath %>/infofind.gif"><span class="big3"> 培训计划查询</span></td>
  </tr>
</table>
<br>
<form  action=""  method="post" name="form1" id="form1" onsubmit="return CheckForm();">
 <table class="TableBlock" width="450" align="center">
    <tr>
      <td nowrap class="TableData">培训计划名称：</td>
      <td class="TableData">
        <INPUT type="text"name="tPlanName" id="tPlanName" class=BigStatic size="15"  readonly>
        <INPUT type="hidden" name="tPlanNo" id="tPlanNo" value="">
        <a href="javascript:;" class="orgAdd" onClick="loadWindow()">选择</a>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">培训渠道：</td>
      <td class="TableData" >
        <select name="tChannel" id="tChannel" title="">
          <option value="">请选择</option>
          <option value="0">内部培训</option>
          <option value="1">渠道培训</option>
        </select>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData" width="100"> 培训形式：</td>
      <td class="TableData">
        <select name="tCourseTypes" id="tCourseTypes" title="培训形式可在“系统管理设置”->“分类码管理”模块设置。">
          <option value="">请选择</option>
        </select>
      </td>
   </tr>
   <tr>
    	<td nowrap class="TableData">培训地点：</td>
      <td class="TableData" >
        <INPUT type="text" name="tAddress" id="tAddress" class=BigInput size="15">
      </td>
   </tr>
       <tr>
      <td nowrap class="TableData">培训机构名称：</td>
      <td class="TableData">
        <INPUT type="text" name="tInstitutionName" id="tInstitutionName" size="38" class="BigInput" value=""></textarea>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 开课日期：</td>
      <td class="TableData">
        <input type="text" name="courseStartDate1" id="courseStartDate1" size="13" maxlength="10" class="BigInput" value="" onClick="">
      	<img id="date1" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >
        	至
        <input type="text" name="courseStartDate2" id="courseStartDate2" size="13" maxlength="10" class="BigInput" value="" onClick="">
      	<img id="date2" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >    
      </td>
    </tr>
    <tr align="center" class="TableControl">
      <td colspan="2" nowrap>
      	<input type="hidden" name="toId" id="toId" value=""> 
        <input type="button" value="查询" onclick="doSubmit();" class="BigButton">&nbsp;&nbsp;
        <input type="reset" value="重填" class="BigButton">&nbsp;&nbsp;
      </td>
    </tr>
 </table>
</form>
 





</body>
</html>