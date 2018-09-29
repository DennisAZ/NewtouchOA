<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>招聘需求查询</title>
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
	
	setDate();
}

function doSubmit(){
	if(checkForm()){
		var query = $("form1").serialize(); 
		location.href = "<%=contextPath %>/subsys/oa/hr/recruit/requirements/search.jsp?" + query;
	}
}

//导出Excel文件
function doSubmit1(){
   	if(checkForm()){
	  document.form1.action="<%=contextPath%>/yh/subsys/oa/hr/recruit/requirements/act/YHHrRecruitRequirementsAct/queryToExcel.act";
      $("form1").submit();
	}
}

function checkForm(){
   if($('requNum').value!=''){
     var num=$('requNum').value;
   if(!isNumber(num)){
     alert('招聘人数必须为数字！');
	 return false;
      }
   }
   
   
  return true;
}

//日期
function setDate(){
  var date1Parameters = {
    inputId:'startTime',
    property:{isHaveTime:false}
    ,bindToBtn:'date1'
  };
  new Calendar(date1Parameters);

  var date2Parameters = {
	  inputId:'endTime',
	  property:{isHaveTime:false}
	  ,bindToBtn:'date2'
  };
  new Calendar(date2Parameters);

 
}
</script>
</head>
<body onLoad="doInit();">
<table border="0" width="80%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img align="absMiddle" src="<%=imgPath %>/infofind.gif"><span class="big3"> 招聘需求信息查询</span></td>
  </tr>
</table>
<br>
<form action=""  method="post" name="form1" id="form1" >
 <table class="TableBlock" width="450" align="center">
 <tr>
   <td class="TableData">需求编号：</td>
   <td class="TableData" colspan="3"><input class="BigInput"  type="text" name="requNo" id="requNo" size="20"></td>
 </tr>
 <tr>
 <td class="TableData">需求岗位：</td>
 <td class="TableData" colspan="3"><input class="BigInput"  type="text" name="requJob" id="requJob" size="20"></td>
 </tr>
  <tr>
 <td class="TableData">需求人数：</td>
 <td class="TableData" colspan="3"><input class="BigInput"  type="text" name="requNum" id="requNum" size="20"> 人</td>
 </tr>
    <tr>
      <td nowrap class="TableData">需求部门：</td>
      <td class="TableData" colspan=3>
        <input type="hidden" name="requDeptId" id="requDeptId" value="" >
        <textarea name="requDept" id="requDept" cols="20"  rows="2" class="BigStatic" value=""></textarea>
        <a href="javascript:;" class="orgAdd" onClick="selectDept(['requDeptId', 'requDept']);">添加</a>
        <a href="javascript:;" class="orgClear" onClick="$('requDept').value='';$('requDeptId').value='';">清空</a>
      </td>
    </tr> 
 <tr>
       <td nowrap class="TableData">用工日期： </td>
	    <td class="TableData" colspan="3">
	      <input type="text" name="startTime" id="startTime" size="11" maxlength="10"  class="BigInput" value="" readonly>
          <img id="date1" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >
		  至：	    
	      <input type="text" name="endTime" id="endTime" size="11" maxlength="10"  class="BigInput" value="" readonly>
          <img id="date2" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >
	    </td>
 </tr>
 
    <tr align="center" class="TableControl">
      <td colspan="4" nowrap>
	   
        <input type="button" value="查询" onClick="doSubmit();" class="BigButton">&nbsp;&nbsp;
        <input type="reset" value="导出" onClick="doSubmit1()" class="BigButton">&nbsp;&nbsp;
      </td>
    </tr>
 </table>
</form>
</body>
</html>