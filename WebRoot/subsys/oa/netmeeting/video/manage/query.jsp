<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>视频会议查询</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css" type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript">
function doInit(){
	setDate();
}

function doSubmit(){
	if(checkForm()){
		var query = $("form1").serialize(); 
		location.href = "<%=contextPath %>/subsys/oa/netmeeting/video/manage/search.jsp?" + query
	}
}

function checkForm(){
  if($("startDate").value == ""){
    alert("开始时间不能为空！");
    $("startDate").focus();
    return (false);
  }

  if($("endDate").value == ""){
    alert("结束时间不能为空！");
    $("endDate").focus();
    return (false);
  }

  if($("startDate").value > $("endDate").value){
    alert("开始时间不能小于结束时间！");
    $("startDate").focus();
    return (false);
  }
  return true;
}

//日期
function setDate(){
  var date1Parameters = {
    inputId:'startDate'
    ,property:{isHaveTime:true,format:'yyyy-MM-dd hh:mm'}
    ,bindToBtn:'date1'
  };
  new Calendar(date1Parameters);

  var date2Parameters = {
    inputId:'endDate'
    ,property:{isHaveTime:true,format:'yyyy-MM-dd hh:mm'}
    ,bindToBtn:'date2'
  };
  new Calendar(date2Parameters);
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
    <!--tr>
      <td nowrap class="TableData" width="100"> 调动类型：</td>
      <td class="TableData" >
        <select name="transferType" id="transferType" title="调动类型可在“人力资源设置”->“HRMS代码设置”模块设置。">
          <option value="">调动类型&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
        </select>
      </td> 
   </tr-->
    <tr>
      <td nowrap class="TableData"> 开始时间：</td>
      <td class="TableData">
        <input type="text" name="startDate" id="startDate" size="20" maxlength="20" class="BigInput" value="" readonly>
        <img id="date1" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 结束时间：</td>
      <td class="TableData">
        <input type="text" name="endDate" id="endDate" size="20" maxlength="20" class="BigInput" value="" readonly>
        <img id="date2" align="middle" src="<%=imgPath %>/calendar.gif" align="middle" border="0" style="cursor:pointer" >
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