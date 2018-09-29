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
    window.location = contextPath + "/core/frame/index.jsp";
  }else{
    //$('msrgOut').innerHTML = json.rtMsrg;
  }
}

function doInit(){
  if($("userName")){
    var prc = getRealUserName();
    if(prc){
      $("userName").value  = prc.userName;
      //$("pwd").value = prc.pwd;
    }
    loginNamePass();
  }
 
 
}
/**
 * 解密
 */
function getRealUserName(){
  var url =  "<%=contextPath%>/yh/subsys/jtgwjh/util/YHNetFileAct/getUser.act";
  var json = getJsonRs(url,{userName:$("userName").value});
  if(json.rtState == "0"){
    var prc =  json.rtData;
    return prc;

  }else{
   //
   // return;
    alert( json.rtMsrg);
  }
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
    <input type="hidden" name="GW_JH_TYPE" id="GW_JH_TYPE" value="1"></input>

</form>
<%
}
%>
</body>
</html>
