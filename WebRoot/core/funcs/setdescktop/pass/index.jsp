<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page import="yh.core.funcs.person.data.YHPerson" %>
<!DOCTYPE  html  PUBLIC  "-//W3C//DTD  HTML  4.01  Transitional//EN"  "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>修改密码</title>
<meta  http-equiv="Content-Type"  content="text/html;  charset=UTF-8">
<link  rel="stylesheet"  href  ="<%=cssPath%>/style.css">
<script  type="text/Javascript"  src="<%=contextPath%>/core/js/datastructs.js"  ></script>
<script  type="text/Javascript"  src="<%=contextPath%>/core/js/sys.js"></script>
<script  type="text/Javascript"  src="<%=contextPath%>/core/js/prototype.js"  ></script>
<script  type="text/Javascript"  src="<%=contextPath%>/core/js/smartclient.js"  ></script>
<script type="text/javascript" src = "<%=contextPath  %>/core/js/cmp/grid.js">
</script>
<style>
table.TableList {
  width: 100%;
}
</style>
<link rel="stylesheet" type="text/css" href="<%=cssPath %>/cmp/grid.css"/>
<script  type="text/Javascript">
var min;
var max;
var safe;
var safeSc;
function doInit(){
  var rtJson = getJsonRs("<%=contextPath %>/yh/core/funcs/setdescktop/pass/act/YHPassAct/getSysPara.act");
  if (rtJson.rtState == "0") {
    if (rtJson.rtData.isDemo == "1") {
      $('demoMsg').show();
      $('mainPart').hide();
      return;
    }
    min = rtJson.rtData.SEC_PASS_MIN || 8;
    max = rtJson.rtData.SEC_PASS_MAX || 20;
    safe = rtJson.rtData.SEC_PASS_SAFE || 0;
    if (rtJson.rtData.SEC_PASS_SAFE_SC == 'null') {
      rtJson.rtData.SEC_PASS_SAFE_SC = '0';
    }
    safeSc = rtJson.rtData.SEC_PASS_SAFE_SC || 0;
    var content = '&nbsp;' + min + '-' + max + '位'
    + (safe == '1'?' 必须同时包含字母和数字' : '')
    + (safeSc == '1'?' 必须包含特殊字符' : '');
    $('PASS1_LABEL').innerHTML = content;
    $('PASS2_LABEL').innerHTML = content;
    $('LAST_PASS_TIME').innerHTML = rtJson.rtData.LAST_PASS_TIME;
    if(rtJson.rtData.SEC_PASS_FLAG == '1'){
      $('SEC_PASS_TIME').innerHTML = '您的密码将于<font color="red"> ' + rtJson.rtData.SEC_PASS_TIME + '</font> 天后过期。';
    }
    else{
      $('SEC_PASS_TIME').innerHTML = '密码永不过期 ';
    }
  }else {
    alert('获取属性失败');
  }
  initGrid();
}

function initGrid(){
  var hd =[
           [
             {header:"用户",name:"USER_ID",width:"15%"},
             {header:"时间",name:"TIME",width:"25%"},
             {header:"IP地址",name:"IP",width:"15%"},
             {header:"类型",name:"TYPE",width:"25%"},
             {header:"备注",name:"REMARK",width:"15%"}
           ],
           {
             header:' '
           }
  ];
  var url = "<%=contextPath %>/yh/core/funcs/setdescktop/pass/act/YHPassAct/getSysLog.act?";
  
  var grid = new YHGrid(hd,url,null);
    grid.rendTo('log_grid');
  if(!grid || !grid.records || !grid.records.length){
    $('log_grid').hide();  
  }
}

function checkPassWord(){
  var rtJson = getJsonRs("<%=contextPath %>/yh/core/funcs/setdescktop/pass/act/YHPassAct/checkPassWord.act?PASSWORD=" + $F('PASS0'));
  if (rtJson.rtState == "0") {
    if(rtJson.rtMsrg != ''){
      alert(rtJson.rtMsrg);
      $('PASS0').select();
      return false;
    }
  }
  return true;
}

function checkPassWord2(){
  if($F('PASS1') != $F('PASS2')){
    alert('两次输入密码不一致');
    $('PASS2').select();
    return false;
  }
  return true;
}

function checkPassWord1(){
  var s = $F('PASS1');
  
  if (min < 1 && s.length < 2) {
    return true;
  }

  //var patrn = new RegExp("(\w){" + min + "," + max + "}");
  var patrnSc = /[-`=\\\[\];',./~!@#$%^&*()_+|{}:"<>?]+/;
  var patrnD = new RegExp("[0-9]+");
  var patrnL = new RegExp("[a-zA-Z]+");

  if(s.length < min){
    alert('新密码长度不能小于' + min + '!');
    return false;
  }
  if(s.length > max){
    alert('新密码长度不能大于' + max + '!');
    return false;
  }
  
  if (!!safe) {
    if (safeSc != 1) {
      if (s.length >= 2) {
        if(patrnD.exec(s) && patrnL.exec(s)){
          return true;
        }
        else{
          alert('密码必须同时包含字母和数字');
          return false;
        }
      }
    }
    else {
      if (s.length >= 3) {
        if(patrnD.test(s) && patrnL.test(s) && patrnSc.test(s)){
          return true;
        }
        else{
          alert('密码必须同时包含字母和数字,必须包含特殊字符');
          return false;
        }
      }
    }
  }
  else {
    if (safeSc != 1) {
      return true;
    }
    else {
      if (s.length >= 1) {
        if(patrnSc.test(s)){
          return true;
        }
        else{
          alert('密码必须包含特殊字符');
          return false;
        }
      }
    }
  }
  
  if(s.length >= 3 && safeSc == 1 && !patrnSc.exec(s)){
    alert('密码只能是数字和字母的组合!');
    return false;
  }
  
  if (s.length >= 2 && safe == 1) {
    
    if(patrnD.exec(s) && patrnL.exec(s)){
      return true;
    }
    else{
      alert('密码必须同时包含字母和数字');
      return false;
    }
  }else {
    return true;
  }
}

function submitForm(){
  if($F('PASS1') == $F('PASS0')){
    alert('新密码不能与原密码相同!');
    return;
  }
  if(!checkPassWord1()){
    $('PASS1').select();
    return;
  }
  if(!checkPassWord2()){
    $('PASS2').select();
    return;
  }
  
  if(!checkPassWord()){
    $('PASS0').select();
    return;
  }
  
  var pars = Form.serialize($('form1'));
  var url = "<%=contextPath %>/yh/core/funcs/setdescktop/pass/act/YHPassAct/updatePassWord.act";
  var json = getJsonRs(url,pars);
  if(json.rtState == "0"){
    window.location = "success.jsp";
  }else{
    alert("密码修改失败");
  }
}
</script>
<%YHPerson user = (YHPerson)request.getSession().getAttribute("LOGIN_USER"); %>
</head>

<body onload="doInit()">
<div class="PageHeader">
<table border="0" width="90%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/login.gif" align="absmiddle"><span class="big3"> 修改密码</span><br>
    </td>
  </tr>
</table>
</div>
<table id="demoMsg" style="display:none;" class="MessageBox" align="center" width="240">
  <tr>
    <td class="msg info">
      <div class="content" style="font-size:12pt">在线演示版不能修改密码</div>
    </td>
  </tr>
</table>
<form method="post" action="" name="form1" id="form1" >
<table id="mainPart" class="TableBlock" width="600" align="center">
<tr class="Big">
  <td class="TableData" width="120"><b>用户名：</b></td>
  <td class="TableData"><b><%=user.getUserId() %></b></td>
</tr>
<tr>
  <td class="TableData" >原密码：</td>
  <td class="TableData" >
    <input type="password" name="PASS0"  id="PASS0"  class="BigInput" size="20">
  </td>
</tr>

<tr>
  <td class="TableData" >新密码：</td>
  <td class="TableData" id="REPARSS1">
    <input type="password" name="PASS1"  class="BigInput" size="20" maxlength="" id="PASS1"><label for="PASS1" id="PASS1_LABEL"></label>
    
  </td>
</tr>

<tr>
  <td class="TableData" >确认新密码：</td>
  <td class="TableData" >
    <input type="password" name="PASS2"  class="BigInput" size="20" maxlength="" id="PASS2"><label for="PASS2" id="PASS2_LABEL"></label>
  </td>
</tr>

<tr>
  <td class="TableData" >上次修改时间：</td>
  <td class="TableData" id="LAST_PASS_TIME">
  </td>
</tr>


<tr>
  <td class="TableData" >密码过期：</td>
  <td class="TableData" id="SEC_PASS_TIME">
    
  </td>
</tr>

<tr align="center" >
    <td class="TableControl" colspan="2" >
      <input type="button" value="保存修改" class='BigButton' onclick="submitForm()">
    </td>
</tr>

</table>
</form>


<table border="0" width="90%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/green_arrow.gif" align="absmiddle"><span class="big3"> 最近10次修改密码日志</span><br>
    </td>
  </tr>
</table>
<div id = "log_grid" style = "margin:0 auto;width:90%;border:none;text-align:center"></div>
</body>
</html>
