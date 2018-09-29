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
<script type="text/javascript" src="<%=contextPath%>/core/funcs/system/censorcheck/js/censorcheckUtil.js" ></script>
<script type="text/javascript" src="<%=contextPath%>/core/funcs/mobilesms/js/isdatetime.js" ></script>
<script  type="text/javascript">
var sign = null;

function checkForm(){
  if (!$F('user') && !$F('outUser')){ 
    alert("请添加收信人！");
    return (false);
  }
  
  if ($F('outUser')) {
    var out = $F('outUser');
    var err = false;
    out.split(",").each(function(e, i) {
      if (e && e.length > 18) {
        alert("外部收信人手机号码过长!")
        err = true;
        return false;
      }
    });
    if (err) {
      $F('outUser').focus();
      return false;
    }
  }

  if (!$F('content')){ 
    alert("短信内容不能为空！");
    $('content').focus();
    return (false);
  }
  
  if($F('sendTime') && !isValidDatetime($F('sendTime'))){
    alert("发送时间格式不对，应形如 1999-01-02 14:55:20");
    return false;
  }

  
  var censorStr = censor($F('content'), 2, 1);
  
  if(censorStr == "BANNED"){
    $('sendFlag').value = "0";
    return false;
  }else if( censorStr == "MOD"){
    $('sendFlag').value = "2";
  }else{
    $('sendFlag').value = "1";
  }
  return true;
}

function notice(){
  msg="注意：\n\n所发送的手机短信将在本系统中进行记录，\n请勿发送与工作无关的涉及个人隐私的信息，\n请提醒接收方：其直接回复的信息也可能导致隐私泄露。";
  alert(msg);
}

var cap_max=200;
 
function getLeftChars(varField){
  var i = 0;
  var counter = 0;
  var cap = cap_max;
  var leftchars = cap - varField.value.length;

  return leftchars;
}
 
function onCharsChange(varField){
  var leftChars = getLeftChars(varField);
  if ( leftChars >= 0){
    $('charsmonitor1').value = cap_max - leftChars;
    $('charsmonitor2').value = leftChars;
    return true;
  }
  else{
    $('charsmonitor1').value = cap_max;
    $('charsmonitor2').value = "0";

   window.alert("短信内容超过字数限制!");
   
   var len = $F('content').length + leftChars;
   
   $('content').value = $F('content').substring(0, len);
   leftChars = getLeftChars($('content'));
   if (leftChars >= 0){
     $('charsmonitor1').value = cap_max-leftChars;
     $('charsmonitor2').value = leftChars;
   }
    return false;
  }
}

function clearUser() {
  $('user').value='';
  $('userDesc').value='';
}

function checkSend(){
  if (window.event.keyCode == 10 && checkForm()){
    $('form1').submit();
  }
}

function isOutPriv(){
  var url = '<%=contextPath%>/yh/core/funcs/mobilesms/act/YHMobileSms2Act/queryOutPriv.act';
  var json = getJsonRs(url);
  if (json.rtState == "0"){
    sign = json.rtData.USER_NAME + '(' + json.rtData.DEPT_NAME + '):';
    $('content').innerHTML = sign;
    if (json.rtData.OUT_PRIV){
      $('outPriv').show();
    }
  }else{
  }
}

function formatDate(dateObj, format) {
  /*
   * eg:format="YYYY-MM-dd hh:mm:ss";
   */
  var o = {
    "M+" :  dateObj.getMonth()+1,  //month
    "d+" :  dateObj.getDate(),     //day
    "h+" :  dateObj.getHours(),    //hour
    "m+" :  dateObj.getMinutes(),  //minute
    "s+" :  dateObj.getSeconds(), //second
    "q+" :  Math.floor((dateObj.getMonth()+3)/3),  //quarter
    "S"  :  dateObj.getMilliseconds() //millisecond
  }
  if(/(y+)/.test(format)) {
    format = format.replace(RegExp.$1, (dateObj.getFullYear() + "").substr(4 - RegExp.$1.length));
  }
  for (var k in o) {
    if(new RegExp("("+ k +")").test(format)) {
      format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));
    }
  }
  return format;
}
function doInit(){

  isOutPriv();
  
  $('outUser').innerHTML = '${param.outUser}';
  
  onCharsChange($('content'));
  
  var sendTimePara = {
      inputId:'sendTime',
      property:{isHaveTime:true},
      bindToBtn:'sendTimeImg'
  };
  new Calendar(sendTimePara);

  /**
  var date = new Date();
  var y = date.getFullYear();
  var m = date.getMonth() + 1;
  m = (m > 9) ? m : '0' + m;
  var d = date.getDate();
  d = (d > 9) ? d : '0' + d;
  var time = date.toLocaleTimeString();
  $('sendTime').value = y + '-' + m + '-' + d + ' ' + time;
  **/

  $('sendTime').value = formatDate(new Date(), "yyyy-MM-dd hh:mm:ss");
}

function signName(){
  $('content').innerHTML = $('content').innerHTML + sign;
  onCharsChange($('content'));
}

function clearContent(){
  $('content').innerHTML = "";
}

function selectAddress(){
  var url = "/yh/core/funcs/mobilesms/new/MultiAddressSelect.jsp";
  openDialogResize(url,  520, 400)
}
</script>
</head>

<body onload="doInit()">

<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/mobile_sms.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3"> 发送手机短信</span>
    </td>
  </tr>
</table>
<br>
<form action="<%=contextPath%>/yh/core/funcs/mobilesms/act/YHMobileSms2Act/addSms.act" onsubmit="return checkForm();" method="post" name="form1" id="form1">
  <table class="TableBlock" width="650" align="center">
    <tr>
      <td nowrap class="TableData">收信人[内部用户]：</td>
      <td nowrap class="TableData">
        <input type="hidden" name="user" id="user" value="" />
          <textarea name="userDesc" id="userDesc"  rows="4" cols="60" class="BigStatic" readOnly></textarea>
          <a class="orgAdd" href="javascript:;" onClick="selectUser()">添加</a>
          <a class="orgClear" href="javascript:;" onClick="clearUser()">清空</a>
      </td>
    </tr> 
    <tr id="outPriv" style="display:none">
      <td nowrap class="TableData">收信人[外部号码]：</td>
      <td class="TableData">
        号码之间请用逗号分隔或每行一条<br>
        <textarea cols=55 name="outUser" id="outUser" rows=3 class="BigInput" wrap="yes"></textarea>
        <a href="javascript:;" class="orgAdd" onClick="selectAddress('outUser')" title="从通讯簿添加收信人">添加</a>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 短信内容：</td>
      <td class="TableData">
        已输入 <input class="SmallStatic" type=text name="charsmonitor1" id="charsmonitor1" size=3 readonly=true> 字符，剩余 <input class="SmallStatic" type="text" name="charsmonitor2 "id="charsmonitor2" size=3 readonly=true> 字符，每条70字，超出部份转第2条<br>
        <textarea cols=70 name="content" id="content" rows=5 class="BigInput" wrap="on" onpaste="return onCharsChange(this);" onKeyUp="return onCharsChange(this);" onkeypress="checkSend()"></textarea>
        <br>按Ctrl+回车键发送消息 &nbsp;<a href="javascript:notice();">隐私警示</a>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 发送时间：</td>
      <td class="TableData">
        <input type="text" id="sendTime" name="sendTime" size="19" maxlength="19" class="BigInput" value="">
        <img src="<%=imgPath%>/calendar.gif" id="sendTimeImg" align="absMiddle" border="0" style="cursor:pointer">
      </td>
    </tr>
    <tr align="center" class="TableControl">
      <td colspan="2" nowrap>
        <input type="submit" value="发送" class="BigButton">&nbsp;&nbsp;&nbsp;&nbsp;
        <input type="button" value="签名" class="BigButton" onclick="signName()">&nbsp;&nbsp;&nbsp;&nbsp;
        <input type="button" value="清空内容" class="BigButton" onclick="clearContent()">
        <input type="hidden" name="sendFlag" id="sendFlag">
      </td>
    </tr>
  </table>
</form>
</body>
</html>