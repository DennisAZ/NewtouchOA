<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%
String flag = request.getParameter("flag");
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>导入用户</title>
<link rel="stylesheet" href="<%=cssPath %>/views.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath %>/style.css" type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/person/js/personUtil.js"></script>
<script Language="JavaScript">
var flag = "<%=flag %>";
function doInit(){
	if(flag == "0"){
	    showCntrl('listContainer');
	    var mrs = "数据已存在，导入失败!";
	    WarningMsrgLong(mrs, 'msrg');
	}
	else if(flag == "1"){
		showCntrl('listContainer');
		var mrs = "共导入 1 条数据!";
		WarningMsrgLong(mrs, 'msrg');
	}
}
/**
 * 导入用户
 * 
 * @return
 */
function importSecureCard(){
  if($("csvFile").value == ""){ 
    alert("请选择要导入的文件！");
    $("csvFile").focus();
    return false;
  }
  
  var csvFile = $("csvFile").value;
  var csvStr = csvFile.substr(csvFile.length - 3, csvFile.length);
  if(csvStr != "smd"){
    alert("错误,只能导入smd文件!");
    $("csvFile").focus();
    $("csvFile").select();
    return false;
  }
  
  if($("csvFile").value != ""){
    $('form1').submit();
  }
}
</script>
</head>
<body topmargin="5" onload="doInit();">
<div id="cionDiv">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
    <tr>
      <td class="Big"><img src="<%=imgPath%>/sys_config.gif" align="absmiddle"><span class="big3">&nbsp;导入动态密码卡信息</span><br>
      </td>
    </tr>
  </table>
  <br>
  <br>
  <div align="center" class="Big1">
  <b>请指定用于导入的smd文件：</b>
  <form id="form1" name="form1" method="post" action="<%=contextPath%>/yh/core/funcs/person/act/YHSecureCardAct/importSecureCard.act" enctype="multipart/form-data">
    <input type="file" name="csvFile" id="csvFile" class="BigInput" size="30">
    <input type="hidden" name="FILE_NAME">
    <input type="hidden" name="GROUP_ID" value="">
    <input type="button" value="导入" class="BigButton" onclick="importSecureCard();">
  </form>
  </div>
</div>
  <div id="listDiv" align="center"></div>
  <br>
  <div id="listContainer" style="display:none">
</div>
<div id="msrg"></div>
  <br>
  <div align="center" id="turnDiv">
   <input type="button" value="返回" class="BigButton" onClick="location='<%=contextPath%>/core/funcs/person/secureCard.jsp';" title="返回">
</div>
</body>
</body>
</html>