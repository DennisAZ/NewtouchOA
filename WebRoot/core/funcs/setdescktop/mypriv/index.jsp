<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page import="yh.core.funcs.person.data.YHPerson" %>
<!DOCTYPE  html  PUBLIC  "-//W3C//DTD  HTML  4.01  Transitional//EN"  "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>我的帐户</title>
<meta  http-equiv="Content-Type"  content="text/html;  charset=UTF-8">
<link  rel="stylesheet"  href  ="<%=cssPath%>/style.css">
<script  type="text/Javascript"  src="<%=contextPath%>/core/js/datastructs.js"  ></script>
<script  type="text/Javascript"  src="<%=contextPath%>/core/js/sys.js"  ></script>
<script  type="text/Javascript"  src="<%=contextPath%>/core/js/prototype.js"  ></script>
<script  type="text/Javascript"  src="<%=contextPath%>/core/js/smartclient.js"  ></script>
<script  type="text/Javascript">

function doInit(){
  var rtJson = getJsonRs("<%=contextPath %>/yh/core/funcs/setdescktop/mypriv/act/YHMyprivAct/getInfo.act");
  
  if (rtJson.rtState == "0") {
    $('USER_PRIV').innerHTML = rtJson.rtData.USER_PRIV;
    $('BYNAME').value = rtJson.rtData.BYNAME;
    $('USER_PRIV_OTHER').innerHTML = rtJson.rtData.USER_PRIV_OTHER;
    $('POST_PRIV').innerHTML = rtJson.rtData.POST_PRIV;
    var emailCapacty = rtJson.rtData.EMAIL_CAPACITY;
    if (!emailCapacty || emailCapacty ==  '0MB' ) {
      $('EMAIL_CAPACITY').innerHTML = "不限制";
    } else {
      $('EMAIL_CAPACITY').innerHTML = emailCapacty;
    }
    var folderCapacty = rtJson.rtData.FOLDER_CAPACITY;
    if (!folderCapacty ||folderCapacty == '0MB' ) {
      $('FOLDER_CAPACITY').innerHTML = "不限制";
    } else {
      $('FOLDER_CAPACITY').innerHTML = folderCapacty;
    }
    $('NOT_VIEW_USER').checked = (rtJson.rtData.NOT_VIEW_USER == '1'?true:false);
    $('NOT_VIEW_TABLE').checked = (rtJson.rtData.NOT_VIEW_TABLE == '1'?true:false);
    $('USEING_KEY').checked = (rtJson.rtData.USEING_KEY == '1'?true:false);
  } else {
    alert('获取属性失败');
  }
}

function checkByName(){
  $('form1').submit();
}
</script>
<%YHPerson user = (YHPerson)request.getSession().getAttribute("LOGIN_USER"); %>
</head>
<body topmargin="5" onload="doInit()">
<div class="PageHeader">
<table border="0" width="90%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/system.gif" align="absmiddle"><span class="big3"> 我的帐户（<%=user.getUserName() %>）</span><br>
    </td>
  </tr>
</table>
</div>

<form action="<%=contextPath %>/yh/core/funcs/setdescktop/mypriv/act/YHMyprivAct/setByName.act"  method="post" name="form1" id="form1">
  <table class="TableBlock" width="90%" align="center">
    <tr>
      <td nowrap class="TableHeader" colspan="2"><b>&nbsp;登录别名设置</b></td>
    </tr>
    <tr class="TableData">
      <td nowrap  width="20%"> 别名：</td>
      <td><input type="input" name="BYNAME" id="BYNAME" class="BigInput" value="" /></td>
    </tr>
    <tr>
      <td class="TableControl" colspan="2" align="center">
        <input type="button" class="BigButton" value="保存修改" onclick="checkByName()">
      </td>
    </tr>
  </table>
</form>
<br>

<table class="TableBlock" width="90%" align="center">
    <tr>
      <td class="TableHeader" colspan="2"><b>&nbsp;用户角色与管理范围</b></td>
    </tr>
    <tr class="TableData" height="25">
      <td nowrap  width="20%"> 主角色：</td>
      <td width="80%" id="USER_PRIV"></td>
    </tr>
    <tr class="TableData" height="25">
      <td nowrap  width="20%"> 辅助角色：</td>
      <td id="USER_PRIV_OTHER"></td>
    </tr>
    <tr class="TableData" height="25">
      <td nowrap  width="20%"> 管理范围：</td>
      <td id="POST_PRIV"></td>
    </tr>
</table>
<br>

<table class="TableBlock" width="90%" align="center">
    <tr>
      <td nowrap class="TableHeader" colspan="2"><b>&nbsp;系统使用权限</b></td>
    </tr>
   <tr class="TableData">
    <td nowrap >访问控制：</td>
    <td nowrap >
        <input type="checkbox" disabled name="NOT_VIEW_USER" id="NOT_VIEW_USER" >禁止查看用户列表&nbsp;
        <input type="checkbox" disabled name="NOT_VIEW_TABLE" id="NOT_VIEW_TABLE" >禁止显示桌面&nbsp;
        <input type="checkbox" disabled name="USEING_KEY" id="USEING_KEY" >使用用户KEY登陆
    </td>
   </tr>
   <tr class="TableData">
    <td nowrap >内部邮箱容量：</td>
    <td nowrap id="EMAIL_CAPACITY"></td>
   </tr>
   <tr class="TableData">
    <td nowrap >个人文件柜容量：</td>
    <td nowrap id="FOLDER_CAPACITY"></td>
   </tr>
</table>

</body>
</html>
