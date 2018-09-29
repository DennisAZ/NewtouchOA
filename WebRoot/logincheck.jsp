<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<%
String userName = YHUtility.null2Empty(request.getParameter("userName"));
String pwd = YHUtility.null2Empty(request.getParameter("pwd"));
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title><%=StaticData.SOFTTITLE%></title>
<link rel="stylesheet" href="<%=cssPath %>/style.css" type="text/css" />
<script type="text/javascript" src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/smartclient.js"></script>
<script type="text/javascript">
/**
 * 用户名密码登录

 */
function loginNamePass() {
  if(!$('userName').present()){
    $('msrgOut').innerHTML = "没有传递用户名";
    return;
  }
  var pars = $('loginForm').serialize() ;
  var url = contextPath + "/yh/core/funcs/system/act/YHSystemAct/doLoginIn.act";
  var json = getJsonRs(url,pars);
  if(json.rtState == "0"){
    loginComplete(json);
  }else{
    $('msrgOut').innerHTML = json.rtMsrg;
  }
}
/**
* 设置cookie
*/
function setCookie(name,value){
 var Days = 30;
 var exp  = new Date();
 exp.setTime(exp.getTime() + Days*24*60*60*1000);
 document.cookie = name + "="+ escape (value) + ";path=/;expires=" + exp.toGMTString();
}

function loginComplete(json) {
  if (json.rtState == "0"){
    if (json.rtData.saveUserName == "1") {
      setCookie('userName',$('userName').value);
    }else {
      setCookie('userName','');
    }
    
    var url = contextPath + (json.rtData.homeAddress || "/core/frame/webos/index.jsp");
    switch (json.rtData.menuType) {
    case '1':
      window.location.href = url;
      break;
    case '2':
      window.open(url, "_blank","top=0,left=0,toolbar=yes," +
           "location=yes, directories=no, status=no, scrollbars=yes," +
           "resizable=yes, copyhistory=no, width=" + window.screen.width + "," + 
           "height=" + window.screen.height);
       window.opener=null;
       window.open("","_self");
       window.close();
      return;
    case '3': 
      window.open(url, "_blank","top=0,left=0,toolbar=no," +
           "location=no, directories=no, status=no, menubar=no, scrollbars=yes," +
           "resizable=yes, copyhistory=no, width=" + window.screen.width + "," +
           "height=" + window.screen.height);
      window.opener=null;
      window.open("", "_self");
      window.close();
      return;
    default:
      window.location.href = url;
    }
  }
}
function doInit(){
  loginNamePass();
}
</script>
</head>
<body onload="doInit()">
<table align="center" class="MessageBox" width="300">
  <tr>
    <td id="msrgOut" class="msg info">
     &nbsp;正在登录，请稍候...    
    </td>
  </tr>
</table>
<%
if (!YHUtility.isNullorEmpty(userName)) {
%>
<form method=post action="/yh/core/funcs/display/index.jsp" id='loginForm'>
  <input type="hidden" name="userName" id="userName" value="<%=userName %>"></input>
  <input type="hidden" name="pwd" id="pwd" value="<%=pwd %>"></input>
</form>
<%
}
%>
</body>
</html>
