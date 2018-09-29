<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  String seqId = request.getParameter("seqId");
  if(seqId == null){
    seqId = "";
  }
  //String oaSsoPort  = YHSysProps.getProp("OA_SSO_PORT");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/sso.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/email/js/emaillogic.js"></script>
<script type="text/javascript">
var oaSsoPort = "";
var mails={};
mails['163.com']=new Array('pop.163.com',110,0,'smtp.163.com',25,0,0,0);
mails['vip.163.com']=new Array('pop.vip.163.com',110,0,'smtp.vip.163.com',25,0,0,0);
mails['188.com']=new Array('pop.188.com',110,0,'smtp.188.com',25,0,0,0);
mails['126.com']=new Array('pop.126.com',110,0,'smtp.126.com',25,0,0,0);
mails['yeah.net']=new Array('pop.yeah.net',110,0,'smtp.yeah.net',25,0,0,0);
mails['qq.com']=new Array('pop.qq.com',110,0,'smtp.qq.com',25,0,0,0);
mails['vip.qq.com']=new Array('pop.qq.com',110,0,'smtp.qq.com',25,0,1,0);
mails['sina.com']=new Array('pop.sina.com',110,0,'smtp.sina.com',25,0,0,0);
mails['vip.sina.com']=new Array('pop3.vip.sina.com',110,0,'smtp.vip.sina.com',25,0,0,0);
mails['sohu.com']=new Array('pop.sohu.com',110,0,'smtp.sohu.com',25,0,0,0);
mails['tom.com']=new Array('pop.tom.com',110,0,'smtp.tom.com',25,0,0,0);
mails['gmail.com']=new Array('pop.gmail.com',995,1,'smtp.gmail.com',465,1,1,0);
mails['yahoo.com.cn']=new Array('pop.mail.yahoo.com.cn',995,1,'smtp.mail.yahoo.com.cn',465,1,1,0);
mails['yahoo.cn']=new Array('pop.mail.yahoo.cn',995,1,'smtp.mail.yahoo.cn',465,1,1,0);
mails['21cn.com']=new Array('pop.21cn.com',110,0,'smtp.21cn.com',25,0,0,0);
mails['21cn.net']=new Array('pop.21cn.net',110,0,'smtp.21cn.net',25,0,0,0);
mails['263.net']=new Array('263.net',110,0,'smtp.263.net',25,0,0,0);
mails['x263.net']=new Array('pop.x263.net',110,0,'smtp.x263.net',25,0,0,0);
mails['263.net.cn']=new Array('263.net.cn',110,0,'263.net.cn',25,0,0,0);
mails['263xmail.com']=new Array('pop.263xmail.com',110,0,'smtp.263xmail.com',25,0,0,0);
mails['foxmail.com']=new Array('pop.foxmail.com',110,0,'smtp.foxmail.com',25,0,0,0);
mails['hotmail.com']=new Array('pop3.live.com',995,1,'smtp.live.com',25,1,1,0);
mails['live.com']=new Array('pop3.live.com',995,1,'smtp.live.com',25,1,1,0);

function FillSettings(email){
   $('emailUser').value = email;
   if(email.trim()=="" || email.indexOf("@")<0) {
     return;
   }
   var email = email.substr(email.indexOf("@")+1).trim();
   if(!mails[email]) {
     return;
   }
  
   $('popServer').value = mails[email][0];
   $('pop3Port').value = mails[email][1];
   //$('pop3Pass').checked = mails[email][2];
   $('smtpServer').value = mails[email][3];
   $('smtpPort').value = mails[email][4];
   $('smtpSsl').checked = mails[email][5];
//   $('LOGIN_TYPE').selectedIndex=mails[email][6];
   $('smtpPass').selectedIndex = mails[email][7];
   //$('emailPass').focus();
}
function CheckForm(){
  if($("email").value == "" || $('popServer').value == "" || $('smtpServer').value == ""){ 
    alert("邮件账户信息均不能为空！");
    return false;
  }
  if(!checkEmail($("email").value)){
    alert("非法邮箱地址！");
    $("email").select();
    return false;
  }
  if(!checkNum($('pop3Port').value) || ($('pop3Port').value).trim() == "0" ){
    alert("接收服务器端口号必须为大于0的整数！");
    $("pop3Port").select();
    return false;
   }
  if(!checkNum($('smtpPort').value) || ($('smtpPort').value).trim() == "0"){
    alert("发送服务器端口号必须为大于0的整数！");
    $("smtpPort").select();
    return false;
   }
  if($("isDefault").checked){
    if($("emailPass").value == ""){
       alert("默认邮箱必须设置密码!");
       $("emailPass").select();
       return false;
    }
  }
  return true;
}
function checkNum(value){
  if(value.trim() == "" ){
    return false;
  }
  var reg = /^\d*$/; 
  return reg.test(value);
}
/**
 * 
 */
function doSubmit(){
  if(!CheckForm()){
    return;
  }
  var param = $('form1').serialize();
  var url = contextPath + "/yh/core/funcs/email/act/YHWebmailAct/updateWebmailInfo.act";
  var rtJson = getJsonRs(url,param);
  if(rtJson.rtState == "0"){
	  if(oaSsoPort == ''){
		  window.location.href = contextPath + "/core/funcs/email/webbox/manager/index.jsp";
	  }
	  else{
	    var url = location.href;
	    var httpIp = url.split(contextPath)[0];
	    rsSSO(httpIp+':'+oaSsoPort+'/general/crm/apps/crm/modules/SendEmail/aysnWebMail.php?seqId='+$('seqId').value+'&flag=0');
	  }
    alert("设置保存成功!");
  }else{
     alert("设置保存失败!");
  }
}
var seqId = "<%=seqId%>";
function doInit(){
  loadWebMailInfo(seqId);
}
function loadWebMailInfo(seqId){
  var url = contextPath + "/yh/core/funcs/email/act/YHWebmailAct/loadWebmailInfo.act";
  var rtJson = getJsonRs(url,"seqId=" + seqId);
  if(rtJson.rtState == "0"){
    bindJson2Cntrl(rtJson.rtData);
    if (rtJson.rtData.pop3Ssl == '1') {
      $('pop3Ssl').checked = true;
    }else {
      $('pop3Ssl').checked = false;
    }
    if (rtJson.rtData.smtpSsl == '1') {
      $('smtpSsl').checked = true;
    }else {
      $('smtpSsl').checked = false;
    }
  }else{
    alert(rtJson.rtMsrg);
  }
}

function locationTo(){
	window.location.href = contextPath + "/core/funcs/email/webbox/manager/index.jsp";
}
</script>
<title>外部邮箱管理</title>
</head>
<body class="bodycolor" onload="doInit()" >
<div class="PageHeader"></div>
<table class="TableTop" width="650">
   <tr>
      <td class="left"></td>
      <td class="center subject">配置邮件账户</td>
      <td class="right"></td>
   </tr>
</table>
<form  name="form1" id="form1">
<input id="seqId" name="seqId" value="" type="hidden">
<table class="TableBlock no-top-border" width="650">
    <tr>
      <td nowrap class="TableData"> 电子邮件地址：</td>
      <td class="TableData">
        <input type="text" id="email" name="email" size="30" maxlength="200" value="" class='BigInput' onkeyup='FillSettings(this.value);'>
        <br> 例如 abc@263.net
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 接收服务器(POP3)：</td>
      <td class="TableData">
        <input type="text" id="popServer" name="popServer" size="20" maxlength="100" class="BigInput" value="">&nbsp;
        端口 <input type="text" id="pop3Port" name="pop3Port" size="4" class="BigInput" value="110">&nbsp;<br>
        <input type="checkbox" id="pop3Ssl" name="pop3Ssl" value="1" ><label for="POP3_SSL">此服务器要求安全连接(SSL)</label>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 发送服务器(SMTP)：</td>
      <td class="TableData">
        <input type="text" id="smtpServer" name="smtpServer" size="20" maxlength="100" class="BigInput" value="">&nbsp;
        端口 <input type="text" id="smtpPort" name="smtpPort" size="4" class="BigInput" value="25">&nbsp;<br>
        <input type="checkbox" id="smtpSsl" name="smtpSsl" value="1"><label for="SMTP_SSL">此服务器要求安全连接(SSL)</label>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 登录帐户：</td>
      <td class="TableData">
        <input type="text" name="emailUser" id="emailUser" size="20" maxlength="100" class="BigInput" value="">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 登录密码：</td>
      <td class="TableData">
        <input type="password" id="emailPass" name="emailPass" size="20" maxlength="100" class="BigInput" value="">
        选填，可自动收取邮件登录
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData" valign="top"> SMTP需要身份验证：</td>
      <td class="TableData">
        <select id="smtpPass" name="smtpPass" class="BigSelect">
          <option value="1" >是</option>
          <option value="0" >否</option>
        </select>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData" valign="top"> 是否检查外部邮件：</td>
      <td class="TableData">
        <select id="checkFlag" name="checkFlag" class="BigSelect">
          <option value="1" >是</option>
          <option value="0" >否</option>
        </select>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 邮箱容量(MB)：</td>
      <td class="TableData">
        <input type="text" name="quotaLimit" id="quotaLimit"size="6" maxlength="10" class="BigStatic" value="0" readonly> 为空或0表示不限制
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData" valign="top"> 默认邮箱：</td>
      <td class="TableData">
        <input type="checkbox" name="isDefault" id="isDefault" value="1"><label for="IS_DEFAULT">做为内部邮件外发默认邮箱（必须设置账户密码）</label>
      </td>
    </tr>
	  <tr>
      <td nowrap class="TableData" valign="top"> 收信设置：</td>
      <td class="TableData">
        <input type="checkbox" name="recvDel" id="recvDel" value="1"><label for="recvDel">收取邮件时，从服务器上删除</label>
      </td>
    </tr>
    <tr align="center" class="TableControl">
      <td colspan="2" nowrap>
        <input type="button" value="保存" class="BigButton" onClick="doSubmit()">&nbsp;&nbsp;
        <input type="button" value="返回" class="BigButton" onClick="location='index.jsp'">
      </td>
    </tr>
  </table>
</form>
</body>
</html>