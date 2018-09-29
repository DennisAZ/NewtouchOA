<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ include file="/core/funcs/doc/workflowUtility/workflowheader.jsp" %>
<%
String sSealForm = (String)request.getAttribute("sealForm");
int sealForm = 2 ;
if (sSealForm != null ){
  sealForm = Integer.parseInt(sSealForm);
}
String flowRemindBefore = (String)request.getAttribute("flowRemindBefore");
if (flowRemindBefore == null) {
  flowRemindBefore = "";
}
String unitBefore = (String)request.getAttribute("unitBefore");
if (unitBefore == null) {
  unitBefore = "m";
}

String flowRemindAfter = (String)request.getAttribute("flowRemindAfter");
if (flowRemindAfter == null) {
  flowRemindAfter = "";
}
String unitAfter = (String)request.getAttribute("unitAfter");
if (unitAfter == null) {
  unitAfter = "d";
}

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>工作流参数设置</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath %>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript">
function save() {
  var url = contextPath + "<%=moduleSrcPath %>/act/YHConfigAct/saveConfig.act";
  var json = getJsonRs(url , $('configForm').serialize()) ;
  if (json.rtState == "0") {
    alert('保存成功！');
  }
}

</script>
 </head>
<body topmargin="5" >
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/sys.gif" align="absmiddle"><span class="big3"> 工作流参数设置</span>
    </td>
  </tr>
</table>
<div align="center">
<form name="configForm" id="configForm">
<table class="TableList" width="90%">
  <tr class="TableHeader" align="center">
    <td width="120">选项</td>
    <td>参数</td>
    <td width="250">备注</td>
  </tr>
  <tr class="TableData" align="center">
    <td width="120"><b>电子印章来源</b></td>
    <td align="left">
       <input type="radio" name="sealForm" id="sealForm1" value="1" <%=(sealForm == 1 ? "checked" : "")%>><label for="sealForm1">文件</label>
       <input type="radio" name="sealForm" id="sealForm2" value="2" <%=(sealForm == 2 ? "checked" : "")%>><label for="sealForm2">数据库</label>
       <input type="radio" name="sealForm" id="sealForm3" value="3" <%=(sealForm == 3 ? "checked" : "")%>><label for="sealForm3">USB KEY</label>
    </td>
    <td width="250" align="left">
     工作流表单加盖印章的来源,普通用户可选择文件形式或者从数据库获取 <br><font color=red>（*USB KEY方式具有更高的安全性，与数字证书绑定，需单独购买授权)</font>
    </td>
  </tr>
  <tr class="TableData" align="center" height="30" style="">
    <td width="120"><b>工作流超时提醒</b></td>
    <td align="left">
       超时前 <Input type="text"  value="<%=flowRemindBefore %>"
         onkeyup="value=value.replace(/[^\d]/g,'')" onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))" name="flowRemindBefore" id="flowRemindBefore" class="SmallInput" size="2" maxlength="2" style="text-align:center;">
       <select name="unitBefore">
       	<option value="m" <%=("m".equals(unitBefore) ? "selected" : "")%>>分钟</option>
        <option value="h" <%=("h".equals(unitBefore) ? "selected" : "")%>>小时</option>
        <option value="d" <%=("d".equals(unitBefore) ? "selected" : "")%>>天</option>
        <option value="%" <%=("%".equals(unitBefore) ? "selected" : "")%>>%</option>
       </select> 开始提醒<br>
       超时后 <Input type="text"  value="<%=flowRemindAfter %>"
         onkeyup="value=value.replace(/[^\d]/g,'')" onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))" name="flowRemindAfter" id="flowRemindAfter" class="SmallInput" size="2" maxlength="2" style="text-align:center;">
       <select name="unitAfter">
       	<option value="m"  <%=("m".equals(unitAfter) ? "selected" : "")%>>分钟</option>
        <option value="h" <%=("h".equals(unitAfter) ? "selected" : "")%>>小时</option>
        <option value="d" <%=("d".equals(unitAfter) ? "selected" : "")%>>天</option>
        <option value="%" <%=("%".equals(unitAfter) ? "selected" : "")%>>%</option>
       </select> 结束提醒
    </td>
    <td width="250" align="left">
       开启工作流超时提醒功能,请先到系统管理-后台服务管理 开启<工作流超时催办>任务。<br>
       百分比含义是针对流程步骤设置的办理时限的。
    </td>
  </tr> 
  <tr class="TableControl" align="center">
    <td colspan="3">
       <Input type="button" name="submit" onclick="save()" class="BigButton" value="保存">
    </td>
  </tr>

</table>
 </form>
</div>
</body>
</html>