<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE  html  PUBLIC  "-//W3C//DTD  HTML  4.01  Transitional//EN"  "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta  http-equiv="Content-Type"  content="text/html;  charset=UTF-8">
<title>Insert  title  here</title>
<link  rel="stylesheet"  href  =  "<%=cssPath%>/cmp/Calendar.css">
<link  rel="stylesheet"  href  ="<%=cssPath%>/style.css">
<script  type="text/javascript"  src="<%=contextPath%>/core/js/datastructs.js"  ></script>
<script  type="text/javascript"  src="<%=contextPath%>/core/js/sys.js"  ></script>
<script  type="text/javascript"  src="<%=contextPath%>/core/js/prototype.js"  ></script>
<script  type="text/javascript"  src="<%=contextPath%>/core/js/smartclient.js"  ></script>
<script  type="text/javascript"  src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath%>/core/funcs/mobilesms/js/isdatetime.js" ></script>
<script  type="text/javascript"> 

function checkForm(){
  var beginTime = $F('beginTime');
  var endTime = $F('endTime');

  if(endTime && !isValidDatetime(endTime)){
    alert("结束时间格式不对，应形如 1999-01-02 14:55:20");
    return false;
  }

  if(beginTime && !isValidDatetime(beginTime)){
    alert("起始时间格式不对，应形如 1999-01-02 14:55:20");
    return false;
  }
  
  if (endTime && beginTime && beginTime >= endTime){ 
    alert("结束时间不能小于或等于起始时间！");
    return (false);
  }
  return true;
}

function initTime(){
  var beginTimePara = {
      inputId:'beginTime',
      property:{isHaveTime:true},
      bindToBtn:'beginTimeImg'
  };
  new Calendar(beginTimePara);
  
  var endTimePara = {
      inputId:'endTime',
      property:{isHaveTime:true},
      bindToBtn:'endTimeImg'
  };
  
  new Calendar(endTimePara);

  var date = new Date();
  var y = date.getFullYear();
  var m = date.getMonth() + 1;
  m = (m > 9) ? m : '0' + m;
  var d = date.getDate();
  d = (d > 9) ? d : '0' + d;
  $('beginTime').value = y + '-' + m + '-' + d + ' 00:00:00';
}

function doInit(){
  initTime();
}

function clearUser() {
  $('user').value='';
  $('userDesc').value='';
}

function submitForm(){
  if (checkForm()){
    $('form1').submit();
  }
}
</script> 
</head> 
<body onload="doInit()"> 

<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>

    <td class="Big"><img src="<%=imgPath%>/mobile_sms.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3"> 接收短信查询</span>
      <span class=small1>查询OA用户通过OA系统向您发送的手机短信</span>
    </td>
  </tr>
</table>
<br>

<form action="<%=contextPath%>/core/funcs/mobilesms/receiveManage/list.jsp" name="form1" id="form1" method="post">
  <table class="TableBlock" width="500" align="center">
    <tr>
      <td nowrap class="TableData">发送人：</td>
      <td nowrap class="TableData">
        <input type="hidden" name="user" id="user" value="" />
          <textarea name="userDesc" id="userDesc"  rows="4" cols="30" class="BigStatic" readOnly></textarea>
          <a href="javascript:;" onClick="selectUser()">添加</a>
          <a href="javascript:;" onClick="clearUser()">清空</a>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">内容：</td>
      <td class="TableData"><textarea cols=40 name="content" id="content" rows="3" class="BigInput" wrap="yes"></textarea>
      </td>
    </tr>
    <tr> 
      <td nowrap class="TableData">起始时间：</td> 
      <td class="TableData">
        <input type="text" id="beginTime" name="beginTime" size="19" maxlength="19" class="BigInput" value="">
        <img src="<%=imgPath%>/calendar.gif" id="beginTimeImg" align="absMiddle" border="0" style="cursor:pointer" >
      </td>
    </tr> 
    <tr> 
      <td nowrap class="TableData">截止时间：</td>
      <td class="TableData">
        <input type="text" id="endTime" name="endTime" size="19" maxlength="19" class="BigInput" value="">
        <img src="<%=imgPath%>/calendar.gif" id="endTimeImg" align="absMiddle" border="0" style="cursor:pointer" >
      </td> 
    </tr>
    <tr >
      <td nowrap class="TableControl" colspan="2" align="center">
          <input type="hidden" name="PHONE" value="3">
          <input type="button" value="查询" class="BigButton" title="进行查询" onclick="submitForm()" name="button">&nbsp;&nbsp;&nbsp;

      </td>
    </tr>
    </table>
  </form>

</body>

</html> 