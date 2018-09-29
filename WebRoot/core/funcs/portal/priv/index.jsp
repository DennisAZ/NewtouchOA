<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>模块权限设置</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href = "<%=cssPath%>/cmp/ExchangeSelect.css">
<script type="text/javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript">
function doInit() {
  $('portId').value = '${param.portId}';
}
function submitForm() {
  var pars = Form.serialize($('form1'));
  var url = "<%=contextPath %>/yh/core/funcs/setdescktop/fav/act/YHFavAct/addUrl.act";
  var json = getJsonRs(url,pars);
  if (json.rtState == "0"){
  } else{
  }
}
</script>
</head>
<body onload="doInit()">
<form action=""  method="post" id="form1">
<table class="TableBlock" width="1000" height="100%" align="center">
  <tr>
    <td nowrap class="TableContent"" align="center">授权范围：<br>（部门）</td>
    <td class="TableData">
      <input type="hidden" name="dept" id="dept" value="">
      <textarea cols=40 name="deptDesc" id="deptDesc" rows=8 class="BigStatic" wrap="yes" readonly></textarea>
      <a href="javascript:;" class="orgAdd" onClick="selectDept()">添加</a>
      <a href="javascript:;" class="orgClear" onClick="$('dept').value='';$('deptDesc').value='';">清空</a>
    </td>
  </tr>
  <tr>
    <td nowrap class="TableContent"" align="center">授权范围：<br>（角色）</td>
    <td class="TableData">
      <input type="hidden" name="role" id="role" value="">
      <textarea cols=40 name="roleDesc" id="roleDesc" rows=8 class="BigStatic" wrap="yes" readonly></textarea>
      <a href="javascript:;" class="orgAdd" onClick="selectRole();">添加</a>
      <a href="javascript:;" class="orgClear" onClick="$('role').value='';$('roleDesc').value='';">清空</a>
    </td>
  </tr>
  <tr>
    <td nowrap class="TableContent"" align="center">授权范围：<br>（人员）</td>
    <td class="TableData">
      <input type="hidden" name="user" id="user" value="">
      <textarea cols=40 name="userDesc" id="userDesc" rows=8 class="BigStatic" wrap="yes" readonly></textarea>
      <a href="javascript:;" class="orgAdd" onClick="selectUser();">添加</a>
      <a href="javascript:;" class="orgClear" onClick="$('user').value='';$('userDesc').value='';">清空</a>
    </td>
  </tr>
  <tr>
    <td nowrap  class="TableControl" colspan="2" align="center">
      <input type="hidden" name="portId" id="portId" value="">
      <input type="button" value="确定" onclick="submitForm()" class="BigButton">&nbsp;&nbsp;
      <input type="button" value="返回" class="BigButton" onclick="">
    </td>
</table>
</form>
</body>
</html>