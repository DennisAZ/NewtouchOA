<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.util.Date"%>
    <%@ include file="/core/inc/header.jsp" %>  
     <%
    Date date = new Date();
    String dateStr = YHUtility.getCurDateTimeStr();
    
    %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>系统资源管理</title>
<link rel="stylesheet" href="<%=cssPath %>/page.css">
<link rel="stylesheet" href ="<%=cssPath %>/style.css">
<link rel="stylesheet" href ="<%=cssPath %>/cmp/Calendar.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/orgselect.js"></script>
<script type="text/javascript">
function doInit() {
  var date1Parameters = {
      inputId:'BEGIN_DATE',
      property:{isHaveTime:true}
  };
  new Calendar(date1Parameters);
  var date2Parameters = {
      inputId:'END_DATE',
      property:{isHaveTime:true}
  };
  new Calendar(date2Parameters);
}
var requestUrl = contextPath + "/yh/core/funcs/system/resManage/act/YHResManageAct";
function CheckForm2()
{   
   if(document.form2.BEGIN_DATE.value==""&&document.form2.END_DATE.value=="")
   { alert("起始日期和结束日期不能同时为空！");
     return (false);
   }
   var calTime = document.getElementById("BEGIN_DATE");
   var endTime = document.getElementById("END_DATE");
   
   var calTimeArray  =calTime.value.trim().split(" ");
   var endTimeArray  = endTime.value.trim().split(" ");
   var type1 =   "^(([0-1]?)[0-9])?([2][0-3])?\:[0-5]?[0-9](\:[0-5]?[0-9])?$"　; 
   var type2 = "^\:[0-5]?[0-9](\:[0-5]?[0-9])?$"　;
   var re1 = new RegExp(type1); 
   var re2 = new RegExp(type2);
   if(calTime.value!=""&&calTimeArray.length!=2){
     alert("开始时间应为日期时间型，如：1999-01-01 10:20:33");
     calTime.focus();
     calTime.select();
     return false;
   }else{
     if(calTime.value!=""&&(!isValidDateStr(calTimeArray[0])||calTimeArray[1].match(re1) == null || calTimeArray[1].match(re2) != null)){
       alert("开始时间应为日期时间型，如：1999-01-01 10:20:33");
       calTime.focus();
       calTime.select();
       return false;
     }
   }
   if(endTime.value!=""&&endTimeArray.length!=2){
     alert("结束时间应为日期时间型，如：1999-01-01 10:20:33");
     endTime.focus();
     endTime.select();
     return false;
   }else{
     if(endTime.value!=""&&(!isValidDateStr(endTimeArray[0])||endTimeArray[1].match(re1) == null || endTimeArray[1].match(re2) != null)){
       alert("结束时间应为日期时间型，如：1999-01-01 10:20:33");
       endTime.focus();
       endTime.select();
       return false;
     }
   }
   if(calTime.value!=""&&endTime.value!=""&&calTime.value >= endTime.value){ 
     alert("结束时间要晚于开始时间！");
     endTime.focus();
     endTime.select();
     return (false);
   }
   if(document.form2.SMS.checked==false&&document.form2.EMAIL.checked==false&&document.form2.EMAIL_INBOX.checked==false)
   { alert("请选择删除项目！");
     return (false);
   }
   if(document.form2.OPERATION.item(0).checked)
      msg='确认要生成导出附件的批处理文件吗？';
   else
      msg='确认要删除所有符合指定条件的资源吗？';
   if(window.confirm(msg))
   {
      return true;
   }
   return (false);
}

function CheckForm()
{
   if(document.form1.TO_ID.value=="")
   { alert("请选择用户！");
     return (false);
   }

   if(document.form1.OPERATION.item(0).checked)
      msg='确认要生成导出附件的批处理文件吗？';
   else
      msg='确认要删除指定用户的资源吗？';
   if(window.confirm(msg))
   {
      return true;
   }

   return (false);
}

function delete_sms()
{
  msg='确认要删除所有已读内部短信吗？';
  if(window.confirm(msg))
  {
    var url = requestUrl + "/deleteSms.act";
    var json = getJsonRs(url);
    if (json.rtState == "0") {
      alert("删除成功！");
    }
  }
}

function delete_deleted_sms()
{
  msg='确认要清空收信人已删内部短信吗？';
  if(window.confirm(msg))
  {
    var url = requestUrl + "/deleteDeletedSms.act";
    var json = getJsonRs(url);
    if (json.rtState == "0") {
      alert("删除成功！");
    }
  }
}

function delete_delbox()
{
  msg='为谨慎起见，建议进行清除系统垃圾前，先进行完整的数据备份.\n确认要清空所有内部邮件废件箱邮件吗？';
  if(window.confirm(msg))
  {
    var url = requestUrl + "/deleteDelBox.act";
    var json = getJsonRs(url);
    if (json.rtState == "0") {
      alert("删除成功！");
    }
  }
}

function delete_deleted_delbox()
{
  msg='为谨慎起见，建议进行清除系统垃圾前，先进行完整的数据备份.\n确认要清空收件人已删内部邮件吗？';
  if(window.confirm(msg))
  {  
    var url = requestUrl + "/deleteDeletedDelbox.act";
    var json = getJsonRs(url);
    if (json.rtState == "0") {
      alert("删除成功！");
    }
  }
}

function delete_garbage()
{
  msg='请先进行数据备份（包括数据库和附件）\n\n确认要清除系统垃圾吗，清除后将不可恢复？';
  if(window.confirm(msg))
  {
    var url = "deleteGarbageConfirm.jsp";
    location.href = url;
  }
}
function hide_path(op,path)
{
  if(op==1)
  {
      document.getElementById(path).style.display="";
      if(path=="PATH1")
        url1 = requestUrl + "/batUserRes.act";
      else
         url2 = requestUrl + "/batRes.act";
  }
  else
  {
     document.getElementById(path).style.display="none";
      if(path=="PATH1")
        url1 = requestUrl + "/delUserRes.act";
      else
        url2 =requestUrl + "/delRes.act";
  }
}
var url2 = "";
var url1 = "";
function doBatRes() {
  var flag = CheckForm2();
  if (flag) {
    if (document.getElementById("PATH2").style.display == "none" ) {
      var param = $("form2").serialize();
      var json = getJsonRs(url2, param);
      if (json.rtState == "0") {
        alert("操作成功");
      }
      return ;
    }
    if (url2) {
      $("form2").action = url2;
    } else {
      $("form2").action = requestUrl + "/batRes.act";
    }
    $("form2").target = "_blank";
    $("form2").submit();
  }
}
function doBatRes2() {
  var flag = CheckForm();
  if (flag) {
    if (document.getElementById("PATH1").style.display == "none" ) {
      var param = $("form1").serialize();
      var json = getJsonRs(url1, param);
      if (json.rtState == "0") {
        alert("操作成功");
      }
      return ;
    }
    if (url1) {
      $("form1").action = url1;
    } else {
      $("form1").action = requestUrl + "/batUserRes.act";
    }
    $("form1").target =  "_blank";
    $("form1").submit();
  }
}
function clearUser(input, hidden){
  if($(input).tagName == 'INPUT'){
    $(input).value = "";  
  }else if($(input).tagName == 'TEXTAREA'){
    $(input).innerHTML = '';  
  }
  $(hidden).value = "";
}
</script>
</head>
<body class="bodycolor" topmargin="5" onload="doInit()">

<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
<tr>
    <td class="Big"><img src="<%=imgPath %>/sys_config.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3"> 内部短信资源回收</span><br>
    </td>
  </tr>
</table>

<br>

<div align="center">
  <input type="button"  value="删除所有已读内部短信" class="BigButtonD" onClick="delete_sms();" title="删除所有已读内部短信，有利于改进系统速度">&nbsp;&nbsp;
  <input type="button"  value="清空收信人已删内部短信" class="BigButtonD" onClick="delete_deleted_sms();" title="清空收信人已删内部短信，有利于改进系统速度">
</div>

<br>
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
<tr>
    <td class="Big"><img src="<%=imgPath %>/sys_config.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3"> 内部邮件资源回收</span><br>
    </td>
  </tr>
</table>

<br>

<div align="center">
  <input type="button"  value="清空内部邮件废件箱邮件" class="BigButtonD" onClick="delete_delbox();" title="清空内部邮件废件箱邮件">&nbsp;&nbsp;
  <input type="button"  value="清空收件人已删内部邮件" class="BigButtonD" onClick="delete_deleted_delbox();" title="清空收件人已删内部邮件">
</div>

<br>

<table width="95%" border="0" cellspacing="0" cellpadding="0" height="3">
 <tr>
   <td background="<%=imgPath %>/dian1.gif" width="100%"></td>
 </tr>
</table>

<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
<tr>
    <td class="Big"><img src="<%=imgPath %>/sys_config.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3"> 备份或删除指定时间的资源</span><br>
    </td>
  </tr>
</table>

<br>
  <form action="bat_res.php" method="post" name="form2" id="form2">
  <table class="TableBlock" width="500" align="center">
    <tr>
      <td nowrap class="TableContent">起始时间：</td>
      <td class="TableData">
      <input type="text" name="BEGIN_DATE" size="20"  id="BEGIN_DATE" maxlength="20" class="BigInput" value="" >
       
      </td>
    </tr>
    <tr>
      <td nowrap class="TableContent">截止时间：</td>
      <td class="TableData"><input type="text" name="END_DATE" id="END_DATE" size="20" maxlength="20" class="BigInput"  value="<%=dateStr %>">
        
      </td>
    </tr>
    <tr>
      <td nowrap class="TableContent">删除项目：</td>
      <td class="TableData">
          <input type="checkbox" name="SMS" id="SMS"><label for="SMS">内部短信</label>
          <input type="checkbox" name="EMAIL" id="EMAIL" onclick="if(form2.EMAIL.checked) form2.EMAIL_INBOX.checked=false;"><label for="EMAIL">所有内部邮件</label>
          <input type="checkbox" name="EMAIL_INBOX" id="EMAIL_INBOX" onclick="if(form2.EMAIL_INBOX.checked) form2.EMAIL.checked=false;"><label for="EMAIL_INBOX">内部邮件收件箱</label>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableContent">操作：</td>
      <td class="TableData">
          <input type="radio" name="OPERATION" value="1" id="EXPORT_BAT" onclick="hide_path(1,'PATH2');" checked><label for="EXPORT_BAT">生成导出附件的批处理文件</label>
          <input type="radio" name="OPERATION" value="2" id="DEL_TIME" onclick="hide_path(2,'PATH2');"><label for="DEL_TIME">删除资源</label>
      </td>
    </tr>
    <tr id="PATH2">
      <td nowrap class="TableContent">导出附件的路径：</td>
      <td class="TableData">
        <input type="text" name="EXPORT_PATH" size="30" maxlength="100" class="BigInput" value="C:\ResBak"> 如 C:\ResBak<br>
        生成导出附件的批处理文件后，请手动在OA服务器上执行
      </td>
    </tr>
    <tr>
      <td nowrap class="TableControl" colspan="2" align="center">
          <input type="button" onclick="doBatRes()"  value="确定" class="BigButton" title="确定">
      </td>
    </tr>
  </table>
    </form>

<br>

<table width="95%" border="0" cellspacing="0" cellpadding="0" height="3">
 <tr>
   <td background="<%=imgPath %>/dian1.gif" width="100%"></td>
 </tr>
</table>

<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
<tr>
    <td class="Big"><img src="<%=imgPath %>/sys_config.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3"> 删除指定用户的资源</span><br>
    </td>
  </tr>
</table>

<br>
<form action="" method="post" name="form1" id="form1" >
  <table class="TableBlock" width="550" align="center">
    <tr>
      <td nowrap class="TableContent">用户：</td>
      <td class="TableData">
          <input type="hidden" name="TO_ID" id="TO_ID" value="">
        <textarea cols=35 name="TO_NAME" id="TO_NAME" rows="3" class="BigStatic" wrap="yes" readonly></textarea>
        <a href="javascript:;" class="orgAdd" onClick="selectUser(['TO_ID','TO_NAME'])">添加</a>
        <a href="javascript:;" class="orgClear" onClick="clearUser('TO_ID', 'TO_NAME')">清空</a>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableContent">删除项目：</td>
      <td class="TableData">
          <input type="checkbox" name="EMAIL" id="EMAIL1"><label for="EMAIL1">内部邮件</label>
          <input type="checkbox" name="FOLDER" id="FOLDER"><label for="FOLDER">个人文件柜</label>
          <input type="checkbox" name="ADDRESS" id="ADDRESS"><label for="ADDRESS">个人通讯簿</label>
          <input type="checkbox" name="CALENDAR" id="CALENDAR"><label for="CALENDAR">日程安排</label><br>
          <input type="checkbox" name="DIARY" id="DIARY"><label for="DIARY">工作日志</label>
          <input type="checkbox" name="URL" id="URL"><label for="URL">个人网址</label>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableContent">操作：</td>
      <td class="TableData">
          <input type="radio" name="OPERATION" value="1" id="EXPORT_BAT1" onclick="hide_path(1,'PATH1');" checked><label for="EXPORT_BAT1">生成导出附件的批处理文件</label>
          <input type="radio" name="OPERATION" value="2" id="DEL_USER" onclick="hide_path(2,'PATH1');"><label for="DEL_USER">删除资源</label>
      </td>
    </tr>
    <tr id="PATH1">
      <td nowrap class="TableContent">导出附件的路径：</td>
      <td class="TableData">
        <input type="text" name="EXPORT_PATH" size="30" maxlength="100" class="BigInput" value="C:\ResBak"> 如 C:\ResBak<br>
        生成导出附件的批处理文件后，请手动在OA服务器上执行
      </td>
    </tr>
    <tr>
      <td nowrap class="TableControl" colspan="2" align="center">
          <input type="button"  value="确定" onclick="doBatRes2()" class="BigButton" title="确定">
      </td>
    </tr>
   
  </table>
 </form>
<br>

<table width="95%" border="0" cellspacing="0" cellpadding="0" height="3">
 <tr>
   <td background="<%=imgPath %>/dian1.gif" width="100%"></td>
 </tr>
</table>

<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
<tr>
    <td class="Big"><img src="<%=imgPath %>/sys_config.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3"> 清除系统垃圾 </span> <span class=small1>用于回收已删除用户所占用的空间，包括邮件、个人文件柜等<br>
    	<font color=red>为谨慎起见，建议进行清除系统垃圾前，先进行完整的数据备份</font></span>
    </td>
  </tr>
</table>
<br>

<div align="center">
  <input type="button"  value="清除系统垃圾" class="BigButtonC" onClick="delete_garbage();" title="清除系统垃圾，有利于改进系统速度">
</div>
<br>
<br>
</body>
</html>