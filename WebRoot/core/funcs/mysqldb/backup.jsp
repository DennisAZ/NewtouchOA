<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href = "<%=cssPath%>/cmp/Calendar.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>

<title>数据库热备份</title>
</head>
<script Language="JavaScript">
String.prototype.trim= function(){
    return this.replace(/(^\s*)|(\s*$)/g, "");
}
function CheckForm(){
  if(document.form1.lastTime.value==""){ 
    alert("开始日期不能为空！");
    return (false);
  }
  if(checkDate("lastTime") == false){
    $("lastTime").focus();
    $("lastTime").select();
    alert("日期格式不对，请输入形如：2010-10-10");
    return;
  }
  if(document.form1.interval.value==""){
    alert("间隔天数不能为空！");
    return (false);
  }
  if(checkNum($('interval').value)){ 
    alert("间隔天数必须为数值！");
    $('interval').focus();
    $('interval').select();
    return false;
  }
  if(document.form1.execTime.value=="") { 
    alert("备份时间不能为空！");
    return (false);
  }
  if(checkDateTime("execTime") == false){
    $("execTime").focus();
    $("execTime").select();
    alert("日期格式不对，请输入形如：08:08:08");
    return;
  }
  return true;
}
function checkNum(str){
   var re=/\D/;
   return str.match(re);
}
function isValidDateStr(str) { 
  if (!str) { 
    return; 
   } 
   var r = str.match(/^(\d{1,4})(-|\/)(\d{1,2})\2(\d{1,2})$/); 
   if (r == null) { 
     return false; 
    } 
   if (parseInt(r[1]) > 9999 || parseInt(r[1]) < 1753) { 
     return false; 
    } 
    var d = new Date(r[1], r[3]-1, r[4]); 
    return (d.getFullYear() == r[1] && (d.getMonth() + 1) == r[3] && d.getDate() == r[4]); 
}

function checkDate(cntrlId){
  var dateStr = $(cntrlId).value;
  return isValidDateStr(dateStr) ;
}

function isValidDateStrTime(str) { 
  var type1 = "^(([0-1]?)[0-9])?([2][0-3])?\:[0-5]?[0-9]\:[0-5]?[0-9]$"; 
  var type2 = "^\:[0-5]?[0-9]\:[0-5]?[0-9]$"; 
  var re1 = new RegExp(type1); 
  var re2 = new RegExp(type2); 
  var strArray = str.trim(); 
  if(strArray.match(re1) == null || strArray.match(re2) != null){ 
    return false; 
  } 
}

function checkDateTime(cntrlId){
  var dateStr = $(cntrlId).value;
  return isValidDateStrTime(dateStr) ;
}

function doInit(){
  showCalendar("lastTime", false, "startTimeImg");
  var url1 = contextPath + "/yh/core/funcs/mysqldb/act/YHMySqldbAct/getBackUpTask.act";
  var rtJson2 = getJsonRs(url1);
  if(rtJson2.rtState == '0') {
    bindJson2Cntrl(rtJson2.rtData);
  }
  var backNames = rtJson2.rtData.backUpDataBaseName;
  var url = contextPath + "/yh/core/funcs/mysqldb/act/YHMySqldbAct/getDbNames.act";
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == '0'){
    var data = rtJson.rtData;
    var html = "";
    for(var i = 0 ; i < data.length ; i ++){
      if(checkSetIn(backNames,data[i].dbName)){
        html += "<input type=\"checkbox\" name=\"dbName\" value=\"" + data[i].dbName + "\" checked>" + data[i].dbName + "<BR>";
      }else{
        html += "<input type=\"checkbox\" name=\"dbName\" value=\"" + data[i].dbName + "\">" + data[i].dbName + "<BR>";
      }
    }
    $('dbNameAuto').insert(html,'center');
    $('dbNameSelf').insert(html,'center');
  }
}
function showExecTime(){
  showTime('execTime',false);
}

function checkSetIn(backName,dbName){
  var backNames = backName.split(",");
  for(var j = 0 ;  j < backNames.length ; j ++){
    if(dbName == backNames[j]){
      return true;
     }
  }
  return false;
}
function showCalendar(cntrlId, isHaveTime, imgId) {
  var beginParameters = {
    inputId : cntrlId,
    property : {
      isHaveTime : isHaveTime
    },
    bindToBtn : imgId
  };
  new Calendar(beginParameters);
}
function backUp(){
  var url = contextPath + "/yh/core/funcs/mysqldb/act/YHMySqldbAct/backUp.act";
  var rtJson = getJsonRs(url,$('backForm').serialize());
  if(rtJson.rtState == '0'){
     location = contextPath + "/core/funcs/mysqldb/backupSm.jsp?type=" + rtJson.rtData.type + "&msrg=" + encodeURIComponent(rtJson.rtData.data) ;
   }
}

function saveSet(){
  if(!CheckForm()){
    return;
  }
  var url = contextPath + "/yh/core/funcs/mysqldb/act/YHMySqldbAct/updateOfficeTask.act";
  var rtJson = getJsonRs(url,$('form1').serialize());
  if(rtJson.rtState == '0'){
    location = contextPath + "/core/funcs/mysqldb/backupSet.jsp?type=1&msrg=" + encodeURIComponent(rtJson.rtMsrg) ;
  }else{
    location = contextPath + "/core/funcs/mysqldb/backupSet.jsp?type=0&msrg=" + encodeURIComponent(rtJson.rtMsrg) ;
  }
}
</script>
<body topmargin="5" onload="doInit()">

<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/sys_config.gif" align="absmiddle"><span class="big3"> 自动定时热备份设置</span>
    </td>
  </tr>
</table>
  <form method="post" name="form1" id="form1">
<table class="TableBlock" width="500" align="center">
    <tr>
      <td nowrap class="TableData" width="70">开始日期：</td>
      <td class="TableData">
          <input type="text" name="lastTime" id="lastTime" size="20" maxlength="19" class="BigInput" value="">
          <img id="startTimeImg" align="absMiddle" src="<%=imgPath%>/cmp/email/calendar.gif" align="absMiddle" border="0" style="cursor:pointer" >
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">间隔天数：</td>
      <td class="TableData">
        <input type="text" id="interval" name="interval" class="BigInput" value="" size="5" maxlength="3"> 天
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">备份时间：</td>
        
      <td class="TableData">
        <input type="text" id="execTime" name="execTime" class="BigInput" value="" size="11" maxlength="8">
        <img src="<%=imgPath %>/clock.gif" align="absMiddle" border="0" style="cursor:pointer" onclick="showExecTime()">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">状态：</td>
      <td class="TableData">
        <input type="radio"  name="taskUse" value="1" id="USE_FLAG_1"><label for="USE_FLAG_1">已启用</label>
        <input type="radio"  name="taskUse" value="0" id="USE_FLAG_0"><label for="USE_FLAG_0">已停用</label>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">说明：</td>
      <td class="TableData">
        <div id="taskDesc"></div>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">选择要备份的数据库：</td>
      <td class="TableData" id="dbNameAuto">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableControl" colspan="2" align="center">
          <input type="hidden" value="" id="seqId" name="seqId">
          <input type="button" value="保存设置" class="BigButton"  onclick="saveSet()">
      </td>
    </tr>
</table>
  </form>

<br>
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/sys_config.gif" align="absmiddle"><span class="big3"> 手动热备份数据库</span><br>
    </td>
  </tr>
</table>

<br>
<br>

<div align="center" class="Big1">
<form method="post" id="backForm" name="backForm">
  <table class="TableBlock" width="500" align="center">
    <tr>
      <td nowrap class="TableData">选择要备份的数据库：</td>
      <td class="TableData" id="dbNameSelf" align="left">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">数据库热备份的保存路径：</td>
      <td class="TableData">
        <input type="text" name="backUpDir" size="50" class="BigInput" value="D:/yh/bak">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableControl" colspan="2" align="center">
        <input type="button" value="立即备份" class="BigButton" onclick="backUp();">
      </td>
    </tr>
  </table>
</form>
</div>
</body>
</html>