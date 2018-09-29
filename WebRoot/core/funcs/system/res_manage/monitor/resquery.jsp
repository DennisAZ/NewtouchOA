<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.util.Date"%>
    <%@ include file="/core/inc/header.jsp" %>
    <%
    Date date = new Date();
    String dateStr = YHUtility.getCurDateTimeStr();
    String dbms = YHSysProps.getProp("db.jdbc.dbms");
    %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>系统资源监控查询</title>
<link rel="stylesheet" href="<%=cssPath %>/page.css">
<link rel="stylesheet" href ="<%=cssPath %>/style.css">
<link rel="stylesheet" href ="<%=cssPath %>/cmp/Calendar.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/orgselect.js"></script>
<script type="text/javascript">
function doInit() {
  var date1Parameters = {
      inputId:'beginDate',
      property:{isHaveTime:true}
  };
  new Calendar(date1Parameters);
  var date2Parameters = {
      inputId:'endDate',
      property:{isHaveTime:true}
  };
  new Calendar(date2Parameters);
}

function change_type(value)
{
   if(value=="EMAIL")
      $("TR_SUBJECT").style.display="";
   else
      $("TR_SUBJECT").style.display="none";
}
function clearUser(input, hidden){
  if($(input).tagName == 'INPUT'){
    $(input).value = "";  
  }else if($(input).tagName == 'TEXTAREA'){
    $(input).innerHTML = '';  
  }
  $(hidden).value = "";
}
function checkTime() {
  var calTime = document.getElementById("beginDate");
  var endTime = document.getElementById("endDate");
  
  if ($("TYPE").value == "EMAIL") {
     $("form1").action = "email.jsp";
  } else {
    $("form1").action = "sms.jsp";
  }
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
}
</script>
</head>
<body onload="doInit()">
<form action="<%=contextPath%>/yh/core/funcs/calendar/act/YHCalendarAct/addCalendar.act"  id="form1" name="form1"  method="post" onsubmit="return checkTime();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
<tr>
    <td class="Big"><img src="<%=imgPath %>/sys_config.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3"> 系统资源监控查询 </span><br>
    </td>
  </tr>
</table>
<br>
  <table class="TableBlock" width="600" align="center">
    <tr>
      <td nowrap class="TableContent">发 送 人：</td>
      <td class="TableData">
        <input type="hidden" id="toId" name="toId" value="">
        <textarea cols=45 name="toName" id="toName" rows="3" class="BigStatic" wrap="yes" readonly></textarea>
        <a href="javascript:;" class="orgAdd" onClick="selectUser(['toId','toName'])">添加</a>
        <a href="javascript:;" class="orgClear" onClick="clearUser('toName', 'toId')">清空</a>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableContent">接 收 人：</td>
      <td class="TableData">
        <input type="hidden" name="copyToId" id="copyToId" value="">
        <textarea cols=45 name="copyToName" id="copyToName" rows="3" class="BigStatic" wrap="yes" readonly></textarea>
        <a href="javascript:;" class="orgAdd" onClick="selectUser(['copyToId','copyToName'])">添加</a>
        <a href="javascript:;" class="orgClear" onClick="clearUser('copyToName', 'copyToId')">清空</a>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableContent">监控内容：</td>
      <td class="TableData">
          <select name="TYPE" id="TYPE" class="BigSelect" onchange="change_type(this.value);">
            <option value="EMAIL">内部邮件</option>
            <option value="SMS">内部短信</option>
          </select>
      </td>
    </tr>
    <tbody id="TR_SUBJECT">
    <tr>
      <td nowrap class="TableContent">主　　题：</td>
      <td class="TableData">
          <input type="text" name="subject" id="subject" class="BigInput" size="40">
      </td>
    </tr>
    <% if (dbms.equals("mysql")) { %>
    <tr>
      <td nowrap class="TableContent">附件名称：</td>
      <td class="TableData">
          <input type="text" id="attachmentName" name="attachmentName" class="BigInput" size="40">
      </td>
    </tr>
    <% } %>
   </tbody>
   <% if (dbms.equals("mysql")) { %>
    <tr>
      <td nowrap class="TableContent">正　　文：</td>
      <td class="TableData">
          <input type="text" name="content" id="content" class="BigInput" size="40">
      </td>
    </tr>
    <% } %>
    <tr>
      <td nowrap class="TableContent">起始时间：</td>
      <td class="TableData"><input type="text" id="beginDate" name="beginDate" size="20" maxlength="20" class="BigInput" value="">
         
      </td>
    </tr>
    <tr>
      <td nowrap class="TableContent">结束时间：</td>
      <td class="TableData"><input type="text" id="endDate" name="endDate" size="20" maxlength="20" class="BigInput" value="<%=dateStr %>">
        
      </td>
    </tr>
    <tr class="TableControl">
      <td nowrap colspan="2" align="center">
          <input type="submit"  value="查询" class="BigButton" title="查询">&nbsp;
          <input type="button"  value="返回" class="BigButton" onclick="location='index.jsp'" title="返回到资源占用状况">&nbsp;
      </td>
    </tr>
  </table>
</form>
</body>
</html>