<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>出国记录查询</title>
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

function doInit(){
  var parameters = {
      inputId:'beginDate',
      property:{isHaveTime:false}
      ,bindToBtn:'date1'
  };
  new Calendar(parameters);
  
  var parameters = {
      inputId:'endDate',
      property:{isHaveTime:false}
      ,bindToBtn:'date3'
  };
  new Calendar(parameters);

  var parameters = {
      inputId:'beginDate1',
      property:{isHaveTime:false}
      ,bindToBtn:'date2'
  };
  new Calendar(parameters);
  
  var parameters = {
      inputId:'endDate1',
      property:{isHaveTime:false}
      ,bindToBtn:'date4'
  };
  new Calendar(parameters);
}
function doSubmit(){
  var query = $("form1").serialize();
  if(checkDate()){
  	location = "<%=contextPath%>/subsys/oa/abroad/record/search.jsp?"+query;
  }
}
function checkDate(){
	if($("beginDate").value && $("beginDate1").value){
		if($("beginDate").value > $("beginDate1").value){
			alert("错误, 起始日期开始不能大于起始日期结束！");
			$("beginDate").focus(); 
			$("beginDate").select(); 
			return false;
		}
	}
	if($("endDate").value && $("endDate1").value){
		if($("endDate").value > $("endDate1").value){
			alert("错误, 终止日期开始不能大于终止日期结束！");
			$("endDate").focus(); 
			$("endDate").select(); 
			return false;
		}
	}
	var beginDate = $("beginDate").value;
	if(beginDate){
		if(!isValidDateStr(beginDate)){
			alert("错误,起始日期格式不对，应形如 1999-01-02");
			$("beginDate").focus();
			$("beginDate").select();
			return false;
		}
	}
  var beginDate1 = $("beginDate1").value;
  if(beginDate1){
    if(!isValidDateStr(beginDate1)){
      alert("错误,起始日期格式不对，应形如 1999-01-02");
      $("beginDate1").focus();
      $("beginDate1").select();
      return false;
    }
  }

  var endDate = $("endDate").value;
  if(endDate){
    if(!isValidDateStr(endDate)){
      alert("错误,终止日期格式不对，应形如 1999-01-02");
      $("endDate").focus();
      $("endDate").select();
      return false;
    }
  }
  var endDate1 = $("endDate1").value;
  if(endDate1){
    if(!isValidDateStr(endDate1)){
      alert("错误,终止日期格式不对，应形如 1999-01-02");
      $("endDate1").focus();
      $("endDate1").select();
      return false;
    }
  }
  return true;
}



</script>
</head>
<body onload="doInit()" topmargin="5">
<table border="0" width="80%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img align="absMiddle" src="<%=imgPath%>/infofind.gif"><span class="big3">&nbsp;出国记录查询</span></td>
  </tr>
</table>
<br>
<form enctype="multipart/form-data" action=""  method="post" name="form1" id="form1">
<table class="TableBlock" width="55%" align="center">
    <tr>
      <td nowrap class="TableData">出国人员：</td>
      <td class="TableData" colspan=3>
        <input type="hidden" name="abroadUserId" id="abroadUserId" value="">
        <input cols=20 name="abroadUserIdDesc" id="abroadUserIdDesc" rows=2 class="BigStatic" wrap="yes" readonly></input>
        <a href="javascript:;" class="orgAdd" onClick="selectSingleUser(['abroadUserId', 'abroadUserIdDesc'],null,null,1);">添加</a>
        <a href="javascript:;" class="orgClear" onClick="$('abroadUserId').value='';$('abroadUserIdDesc').value='';">清空</a>
        
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">到访国家名称：</td>
      <td class="TableData" colspan=3>
       <input type="text" name="abroadName" id="abroadName" size="20" class="BigInput" >
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">出国开始日期：</td>
    <td class="TableData">
      <input type="text" name="beginDate" id="beginDate" size="12" maxlength="10" class="BigInput" value="" onClick="">
      <img id="date1" align="middle" src="<%=imgPath%>/calendar.gif" align="middle" border="0" style="cursor:pointer" >
      &nbsp;至&nbsp;
       <input type="text" name="beginDate1" id="beginDate1" class="BigInput" size="15" maxlength="10" value="" readonly>&nbsp;
       <img id="date2" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor: hand"> 
    </td>
   </tr>
   <tr>
     <td nowrap class="TableData">出国结束日期：</td>
    <td class="TableData">
      <input type="text" name="endDate" id="endDate" size="12" maxlength="10" class="BigInput" value="" onClick="">
      <img id="date3" align="middle" src="<%=imgPath%>/calendar.gif" align="middle" border="0" style="cursor:pointer" >
      &nbsp;至&nbsp;
       <input type="text" name="endDate1" id="endDate1" class="BigInput" size="15" maxlength="10" value="" readonly>&nbsp;
       <img id="date4" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor: hand"> 
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
        <input type="button" value="查询" class="BigButton" onclick="doSubmit();">
      </td>
    </tr>
  </table>
</form>
</body>
</html>