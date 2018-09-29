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
    return false;
  }

  if ($F('phone') && !isNumber($F('phone'))) {
    alert('电话号码只能为数字!');
    $('phone').select();
    return false;
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

function submitForm(){

  $('form1').action = "<%=contextPath%>/core/funcs/mobilesms/sendManage/list.jsp";
  $('DELETE_FLAG').value = '0';
  if (checkForm()){
    $('form1').submit();
  }
}

function clearUser() {
  $('user').value='';
  $('userDesc').value='';
}

function deleteAll(){
  var msg='确认要删除指定范围内的短信记录吗？\n已发送成功的短信将不会被删除';
  if (confirm(msg)) {
    if (checkForm()) {
      $('form1').action = '<%=contextPath%>/yh/core/funcs/mobilesms/act/YHMobileSms2Act/deleteBatch.act';
      $('form1').submit();
    }
  }
}
</script> 
</head> 
<body onload="doInit()"> 
 
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small"> 
  <tr> 
    <td class="Big"><img src="<%=imgPath%>/mobile_sms.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3"> 短信发送管理</span> 
    </td> 
  </tr> 
</table> 
<form id="form1" action="<%=contextPath%>/core/funcs/mobilesms/sendManage/list.jsp" method="post"> 
  <table class="TableBlock" width="500" align="center"> 
    <tr> 
      <td nowrap class="TableData">短信发送状态：</td> 
      <td class="TableData"> 
        <select name="sendFlag" id="sendFlag" class="BigSelect"> 
          <option value="ALL">所有</option> 
          <option value="0">待发送</option> 
          <option value="3">发送中...</option> 
          <option value="1">发送成功</option>
          <option value="2">发送超时</option>
        </select> 
      </td> 
    </tr> 
    <tr> 
      <td nowrap class="TableData">收信人号码：</td> 
      <td class="TableData"><input type="text" name="phone" id="phone" size="20" class="BigInput" value=""> 
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
    <tr> 
      <td nowrap class="TableControl" colspan="2" align="center"> 
          <input type="hidden" name="DELETE_FLAG" id="DELETE_FLAG" value="0"> 
          <input type="button" value="查询" class="BigButton" title="进行查询" onclick="submitForm()">&nbsp;&nbsp;&nbsp;
          <input type="button" value="删除" class="BigButton" title="删除指定范围内的短信记录" onclick="deleteAll()"> 
      </td> 
    </tr>
  </table> 
</form> 
</body> 
</html> 