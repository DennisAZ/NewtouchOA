<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>员工福利查询</title>
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
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/hr/salary/welfare_manager/js/welfaremanageLogic.js"></script>
<script type="text/javascript">
function doInit(){
	getSecretFlag("HR_WELFARE_ITEM","welfareItem");
	setDate();
}

function doSubmit(){ 
	if(checkForm()){
		var query = $('form1').serialize();    
		location.href = "<%=contextPath %>/subsys/oa/hr/salary/welfare_manager/search.jsp?" + query;
	}
}

function checkForm(){
  var start=$('startDate').value;
  var end=$('endDate').value;
  if(start>end){
    alert('发布开始时间必须大于发布结束时间！');
	return (false);
	
  }
  if($('welfareMonth').value!=""){
      var month=$('welfareMonth').value;
	 
	 if(!CheckMonth(month)){
   
      return (false);
   }
  }
  
  if($('welfarePayment').value!=""){
     var number=$('welfarePayment').value;
	 if(!isNumber(number)){
	   alert('福利金额必须为数字！');
	   return false;
	 }
  
  }

 return (true); 
}

//日期
function setDate(){
  var date1Parameters = {
    inputId:'startDate',
    property:{isHaveTime:false}
    ,bindToBtn:'date1'
  };
  new Calendar(date1Parameters);

  var date2Parameters = {
	  inputId:'endDate',
	  property:{isHaveTime:false}
	  ,bindToBtn:'date2'
  };
  new Calendar(date2Parameters);
}

function CheckMonth(month){
 var strArray = month.split("-"); 

 if (strArray.length!=2) { 
    alert('工资月份格式不对！');
	return false; 
  } 
  if(strArray[0]>2030 ||strArray[0]<2000){
    alert('年份必须在2000到2030之间！');
    return false;
  }
  if(strArray[1]>12 || strArray[1]<0){
    alert('月份必须在0到12之间！');
	return false;
  }
  
return true;
}




</script>
</head>
<body onLoad="doInit();">
<table border="0" width="80%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img align="absMiddle" src="<%=imgPath %>/infofind.gif"><span class="big3">员工福利查询</span></td>
  </tr>
</table>
<br>	

<form enctype="multipart/form-data"  method="post" action="search.jsp" name="form1" id="form1" >
 <table class="TableBlock" width="450" align="center">
  <tr>
    <td nowrap class="TableData">单位员工：</td>
    <td class="TableData">
        <input type="hidden" name="staffId" id="staffId" value="" >
        <input type="text" name="staffName" id="staffName" class="BigStatic" readonly size="15" >
        <a href="javascript:;" class="orgAdd" onClick="selectUser(['staffId', 'staffName'],null,null,1);">添加</a>
		<a href="javascript:;" class="orgClear" onClick="$('staffId').value='';$('staffName').value='';">清空</a>
    </td>
  </tr>
  <tr>
    <td nowrap class="TableData" width="100">福利项目：</td>
    <td class="TableData">
      	 <select name="welfareItem" id="welfareItem"  title="福利项目可在“人力资源设置”->“HRMS代码设置”模块设置。">
          <option value="">福利项目&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
        </select>
    </td>
  </tr>
  <tr>
    <td nowrap class="TableData">工资月份：</td>
    <td class="TableData">
      <input type="text" name="welfareMonth"  id="welfareMonth" size="15" class="BigInput" value="">
    </td>
  </tr>
  <tr>
   <td nowrap class="TableData">福利金额：</td>
    <td class="TableData">
      <input type="text" name="welfarePayment" id="welfarePayment" class=BigInput size="15" value="">&nbsp;元
    </td>
  </tr>
  <tr>
    <td nowrap class="TableData">是否纳税：</td>
    <td class="TableData">
       <select name="taxAffares"  title="">
          <option value="" >是否纳税&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
          <option value="0">否</option>
          <option value="1">是</option>
        </select>
    </td> 
  </tr>
  <tr>
    <td nowrap class="TableData"> 发放日期：</td>
    <td class="TableData">
      <input type="text" name="startDate" id="startDate" size="10" maxlength="10" class="BigInput" value="" />
	   <img id="date1" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >
      至
      <input type="text" name="endDate" id="endDate" size="10" maxlength="10" class="BigInput" value="" />   
	   <img id="date2" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >
    </td>
  </tr>
  <tr>
    <td nowrap class="TableData">发放物品：</td>
    <td class="TableData">
      <input name="freeGift" size="25" class="BigInput" value="">
    </td>
  </tr>
  <tr align="center" class="TableControl">
    <td colspan="2" nowrap>
      <input type="button" onClick="doSubmit()" value="查询" class="BigButton">&nbsp;&nbsp;
      <input type="reset" value="重填" class="BigButton">&nbsp;&nbsp;
    </td>
  </tr>
</form>
</body>
</html>