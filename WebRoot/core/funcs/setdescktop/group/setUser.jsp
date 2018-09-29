<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE  html  PUBLIC  "-//W3C//DTD  HTML  4.01  Transitional//EN"  "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>新建用户组管理</title>
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8">
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/Javascript" src="/yh/core/js/orgselect.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js"></script>
<script type="text/Javascript">

function submitForm(){
  var pars = Form.serialize($('form1'));
  
  var url = "<%=contextPath %>/yh/core/funcs/setdescktop/group/act/YHUserGroupAct/setUser.act";
  var json = getJsonRs(url,pars);
  if(json.rtState == "0"){
    window.location.href = "<%=contextPath %>/core/funcs/setdescktop/group/success.jsp";
  }else{
    alert("新增失败");
  }
}

function clearUser(id, desc){
  $(id).value = '';
  $(desc).innerHTML = '';
}

function doInit(){
  $('seqId').value = '${param.seqId}';

  var json = getJsonRs("<%=contextPath %>/yh/core/funcs/setdescktop/group/act/YHUserGroupAct/queryUser.act?seqId=${param.seqId}");

  if(json.rtState == "0"){
    $('userId').value = json.rtData.userStr;
    $('userIdDesc').innerHTML = json.rtData.userDesc;
  }else{
    alert("加载用户失败");
  }
}
</script>
</head>

<body onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/edit.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3"> 设置用户</span></td>
  </tr>
</table>
<br>
<form method="post" name="form1" id="form1">
  <table class="TableBlock" width="600" align="center">
    <tr>
      <td class="TableData" nowrap>
        <input type="hidden" name="userId" id="userId" value="">
        <textarea cols="70" name="userIdDesc" id="userIdDesc" rows="15" style="overflow-y:auto;" class="SmallStatic" wrap="yes" readonly></textarea>
        <a href="javascript:;" class="orgAdd" onClick="selectUser(['userId', 'userIdDesc'])">添加</a>
        <a href="javascript:;" class="orgClear" onClick="clearUser('userId', 'userIdDesc')">清空</a>
      </td>
    </tr>
    <tr align="center" class="TableControl">
      <td colspan="2" nowrap>
        <input type="hidden" name="seqId" id="seqId" value="">
        <input type="button" value="确定" class="BigButton" onclick="submitForm()">&nbsp;&nbsp;
        <input type="button" class="BigButton" value="返 回" onclick="location='<%=contextPath %>/core/funcs/setdescktop/group/index.jsp'">
      </td>
    </tr>
  </table>
</form>
</body>
</html>
