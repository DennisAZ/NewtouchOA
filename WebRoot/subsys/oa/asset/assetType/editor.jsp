<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<%@page import="yh.subsys.oa.asset.data.YHCpAssetType"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
YHCpAssetType type = (YHCpAssetType)request.getAttribute("cp");
%>

<html>
<head>
<title>固定资产类型设置</title>
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/tab.js"></script>
<%if (type != null) {%>
<script type="text/javascript">
function checkForm() {
  var num = /^[0-9]*$/;
  if (!$("TYPE_NO").value) {
    alert("排序不能为空！");
    selectLast($("TYPE_NO"));
    document.getElementById("TYPE_NO").select();
    return false;
  }
  if (!num.exec(document.getElementById("TYPE_NO").value)) { 
    alert("排序只能为数字！");
    selectLast($("TYPE_NO"));
    document.getElementById("TYPE_NO").select();
    return false;
  }
  if (!$("TYPE_NAME").value ) { 
    alert("资产类别不能为空！");
    selectLast($("TYPE_NAME"));
    document.getElementById("TYPE_NAME").select();
    return false;
  }
  return true;
}
function doInit () {
  selectLast($("TYPE_NO"));
}
function checkForm2() {
  if (checkForm()) {
      var pars = $('form1').serialize();
      var url = "<%=contextPath%>/yh/subsys/oa/asset/act/YHCpAssetTypeAct/updateType.act";
      var json = getJsonRs(url,pars);
      if (json.rtState == "1") {
        alert(json.rtMsrg);
      } else {
        alert("修改成功");
        window.location = "<%=contextPath%>/subsys/oa/asset/assetType/assetType.jsp";
      }
  }
}
</script>
</head>
<body  topmargin="5" onload="doInit();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/notify_new.gif"  width="16" height="16"> <span class="big3"> 修改资产类别</span>
    </td>
  </tr>
</table>
<form id="form1" name="form1">
<input type="hidden" name="dtoClass" value="yh.subsys.oa.asset.data.YHCpAssetType">
<table class="TableBlock"  width="450"  align="center" >
   <tr>
    <td nowrap class="TableData">排序号：<label style="color: red">*</label></td>
    <td nowrap class="TableData">
        <input type="text" name="typeNo" id="TYPE_NO" class="BigInput" size="2" maxlength="5" value="<%=type.getTypeNo() %>">&nbsp;
    </td>
   </tr>
   <tr>
    <td nowrap class="TableData">资产类别名称：<label style="color: red">*</label></td>
    <td nowrap class="TableData">
    <input type="hidden" name="seqId" id="seqId" class="BigInput" size="25" maxlength="25" value="<%=type.getSeqId()%>">
        <input type="text" name="typeName" id="TYPE_NAME" class="BigInput" size="25" maxlength="25" value="<%=type.getTypeName().replace("\"","'")%>">&nbsp;
    </td>
   </tr>
   <tr class="TableControl">
    <td colspan="2" align="center">
        <input type="button" onClick="checkForm2()" value="修改" class="BigButton" title="修改资产类别" name="button">
        <input type='button' class='BigButton' value='返回' onClick='javascript:history.back();' title='返回' >
    </td>
</table>
 </form>
 <%} else {%>
 <table class="MessageBox" align="center" width="340">
  <tr>
    <td class='msg info'>
    <div class='content' style='font-size: 12pt'>该类型已删除</div>
    </td>
  </tr>
</table>
<div align="center">
<input type='button' class='BigButton' value='返回' onClick='javascript:history.back();' title='返回' ></div>
 <%} %>
</body>
</html>