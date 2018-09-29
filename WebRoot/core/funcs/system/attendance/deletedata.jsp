<%@ page language="java" import="java.util.*,yh.core.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  String curDateStr = YHUtility.getCurDateTimeStr();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>删除考勤数据</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript">
function CheckForm(){
  var beginTime = document.getElementById("minTime");
  var endTime = document.getElementById("maxTime");
  var beginTimeArray  = beginTime.value.trim().split(" ");
  var endTimeArray  = endTime.value.trim().split(" ");
  var type1 =   "^(([0-1]?)[0-9])?([2][0-3])?\:[0-5]?[0-9]\:[0-5]?[0-9]$"　; 
  var type2 = "^\:[0-5]?[0-9]\:[0-5]?[0-9]$"　;
  var re1 = new RegExp(type1); 
  var re2 = new RegExp(type2);
  if(beginTime.value!=""&&beginTimeArray.length!=2){
    alert("开始时间应为日期时间型，如：1999-01-01 10:20:10");
    beginTime.focus();
    beginTime.select();
    return false;
  }else{
    if(beginTime.value!=""&&(!isValidDateStr(beginTimeArray[0])||beginTimeArray[1].match(re1) == null || beginTimeArray[1].match(re2) != null)){
      alert("开始时间应为日期时间型，如：1999-01-01 10:20:10");
      beginTime.focus();
      beginTime.select();
      return false;
    }
  }
  if(endTime.value!=""&&endTimeArray.length!=2){
    alert("结束时间应为日期时间型，如：1999-01-01 10:20:10");
    endTime.focus();
    endTime.select();
    return false;
  }else{
    if(endTime.value!=""&&(!isValidDateStr(endTimeArray[0])||endTimeArray[1].match(re1) == null || endTimeArray[1].match(re2) != null)){
      alert("结束时间应为日期时间型，如：1999-01-01 10:20:10");
      endTime.focus();
      endTime.select();
      return false;
    }
  }
  if(beginTime.value!=""&&endTime.value!=""&&beginTime.value >= endTime.value){ 
    alert("结束时间要晚于开始时间！");
    endTime.focus();
    endTime.select();
    return (false);
  }
  if(window.confirm("删除后数据将不可恢复，确定要删除吗？")){
    return true;
  }
  return false;
}
function doOnload(){
  //初始化时间
  var date1Parameters = {
      inputId:'minTime',
      property:{isHaveTime:true}
      ,bindToBtn:'Imgdate1'
  };
  new Calendar(date1Parameters);
  var date2Parameters = {
      inputId:'maxTime',
      property:{isHaveTime:true}
      ,bindToBtn:'Imgdate2'
  };
  new Calendar(date2Parameters);
}
function Init(){
  if(CheckForm()){
    var requestUrl = "<%=contextPath%>/yh/core/funcs/system/attendance/act/YHManageDataAct/deleteData.act";
    var rtJson = getJsonRs(requestUrl,mergeQueryString($("form1")));
    if(rtJson.rtState == "1"){
      alert(rtJson.rtMsrg); 
      return ;
    }
    document.getElementById("listDiv").style.display="none";
    document.getElementById("returnDiv").style.display="";
  }
}
</script>
</head>
<body class="" topmargin="5" onload="doOnload();">
<div id="listDiv">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/views/attendance.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3"> &nbsp;删除考勤数据</span><br>
    </td>
  </tr>
</table>
<br>
<form action="#" method="post" name="form1" id="form1" onsubmit="return CheckForm();">
  <table class="TableBlock"  width="500" align="center">
    <tr>
      <td nowrap class="TableContent">用户：</td>
      <td class="TableData">
      <input type="hidden" name="user" id="user" value=""  />
      <textarea name="userDesc" id="userDesc"  rows="3" cols="30"  class="BigStatic" readonly="readonly" ></textarea>
       <a href="javascript:;" class="orgAdd" onClick="selectUser();">添加</a>
       <a href="javascript:;" class="orgClear" onClick="$('user').value='';$('userDesc').value='';">清空</a>    
        <br>提示：如果不选择用户，则删除所有用户的考勤记录。
      </td>
    </tr>    
    <tr>
      <td nowrap class="TableContent">起始时间：</td>
      <td class="TableData">
          <input type="text" id="minTime" name="minTime" size="20" maxlength="20" class="BigInput" value="">
          <img id="Imgdate1" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer" >
     </td>
    </tr>
    <tr>
      <td nowrap class="TableContent">截止时间：</td>
      <td class="TableData">
      <input type="text" id="maxTime" name="maxTime" size="20" maxlength="20" class="BigInput" value="<%=curDateStr %>">
          <img id="Imgdate2" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer" >
      </td>
    </tr>
    <tr>
      <td nowrap class="TableContent">删除项目：</td>
      <td class="TableData">
          <input type="checkbox" name="duty" id="duty"><label for="DUTY">上下班登记</label>&nbsp;
          <input type="checkbox" name="out" id="out"><label for="OUT">外出登记</label>&nbsp;
          <input type="checkbox" name="leave" id="leave"><label for="LEAVE">请假登记</label>&nbsp;
          <input type="checkbox" name="evection" id="evection"><label for="EVECTION">出差登记</label>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableControl" colspan="2" align="center">
          <input type="button"  value="确定" class="BigButton" title="确定" onclick="Init();">&nbsp;&nbsp;
          <input type="button"  value="返回" class="BigButton" onClick="history.go(-1);">
      </td>
    </tr>
  </table>
</form>
</div>
<br>
<div id="returnDiv" style="display:none">
<table class="MessageBox" align="center" width="260">
  <tr>
    <td class="msg info">
      <h4 class="title">提示</h4>
      <div class="content" style="font-size:12pt">记录已删除！</div>
    </td>
  </tr>
</table>
<br><center><input type="button" class="BigButton" value="返回" onclick="window.location.reload();"></center>
</div>
</body>
</html>