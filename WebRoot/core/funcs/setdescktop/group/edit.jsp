<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE  html  PUBLIC  "-//W3C//DTD  HTML  4.01  Transitional//EN"  "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>新建用户组管理</title>
<meta  http-equiv="Content-Type"  content="text/html;  charset=UTF-8">
<link  rel="stylesheet"  href  =  "<%=cssPath%>/cmp/ExchangeSelect.css">
<link  rel="stylesheet"  href  ="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath %>/page.css"/>
<script  type="text/Javascript"  src="<%=contextPath%>/core/js/datastructs.js"  ></script>
<script  type="text/Javascript"  src="<%=contextPath%>/core/js/sys.js"  ></script>
<script  type="text/Javascript"  src="<%=contextPath%>/core/js/prototype.js"  ></script>
<script  type="text/Javascript"  src="<%=contextPath%>/core/js/smartclient.js"  ></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/page.js"></script>
<script  type="text/Javascript">

function doInit(){
  $('groupName').value = "${param.groupName}";
  $('orderNo').value = "${param.orderNo}";
  $('seqId').value = "${param.seqId}";
}

function submitForm(){
  if (!$F('groupName')){
    alert('分组名称不能为空');
    return;
  }
  if (!(/[0-9]+$/).exec($F('orderNo'))){
    alert('排序号必须为数字');
    return;
  }
  
  var pars = Form.serialize($('form1'));
  
  var url = "<%=contextPath %>/yh/core/funcs/setdescktop/group/act/YHUserGroupAct/update.act";
  var json = getJsonRs(url,pars);
  if(json.rtState == "0"){
    window.location.href = "<%=contextPath %>/core/funcs/setdescktop/group/index.jsp";
  }else{
    alert("修改失败");
  }
}
</script>

<body onload="doInit()">

<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/notify_new.gif" align="absmiddle"><span class="big3"> 编辑分组</span>
    </td>
  </tr>
</table>

<br>

<form id="form1" name="form1">
  <table class="TableBlock" width="400" align="center">
    <tr>
      <td nowrap class="TableContent">排 序 号：<span style="color: red;">*</span></td>
      <td class="TableData"><input type="text" name="orderNo" id="orderNo" size="20" class="BigInput"></td>
    </tr>
    <tr>
      <td nowrap class="TableContent" width="80">用户组名称：<span style="color: red;">*</span></td>
      <td class="TableData"><input type="text" name="groupName" id="groupName" size="30" class="BigInput"></td>
    </tr>
    <tr>
      <td nowrap class="TableControl" colspan="2" align="center">
          <input type="hidden" name="seqId" id="seqId">
          <input type="button" value="提交" class="BigButton" title="提交数据" name="button1" OnClick="submitForm()">&nbsp&nbsp&nbsp&nbsp
          <input type="button" value="返回" class="BigButton" title="返回" name="button2" OnClick="location='<%=contextPath %>/core/funcs/setdescktop/group/index.jsp'">
      </td>
    </tr>
  </table>
</form>

</body>
</html>