<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" href ="<%=cssPath%>/style.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/javascript">
function doLogin(){
  if(!$('userName').present()){
	alert("请输入用户名");
	$('userName').focus();
	return ;
  }
  var pars = $('loginForm').serialize() ;
  var url = contextPath + "/yh/core/funcs/system/act/YHSystemAct/doLoginIn.act";
  $('loginDiv').hide();
  $('displayDiv').show();
  var json = getJsonRs(url,pars);
  if(json.rtState == "0"){
	window.location = contextPath + "/raw/lh/display/index.jsp";
  }else{
	alert(json.rtMsrg);	
	$('displayDiv').hide();
	$('loginDiv').show();
	$('loginForm').reset()
	$('userName').focus();
  }
}
</script>
</head>
<body>
<div id="loginDiv" align="center" style="height:800px;display:">
<form action="" id="loginForm" method="post">
<table width="328">
  <tr class="TableLine1" onmouseout="setPointer(this, 'out')" onmouseover="setPointer(this, 'over')">
    <td width="75">用户名：</td>
    <td width="185"><label>
      <input class="SmallInput" type="text" name="userName" id="userName" />
    </label></td>
    
  </tr>
  <tr  class="TableLine2"  onmouseout="setPointer(this, 'out')" onmouseover="setPointer(this, 'over')">
    <td>密码：</td>
    <td><label>
      <input  class="SmallInput" type="password" name="pwd" id="pwd" />
    </label></td>
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td><label>
      <input type="button" class="SmallButtonW " name="button" id="button" value="登陆" onclick="doLogin();"/>
      <input type="reset"  class="SmallButtonW " name="重置" id="重置" value="重置" />
    </label></td>
    <td>&nbsp;</td>
  </tr>
</table>
</form>
</div>
<div id="displayDiv" align="center" style="height:800px;display:none">
<div><img src="img/loading.gif"/></div>
<div>正在进入。。。请稍后</div>
</div>
</body>
</html>
