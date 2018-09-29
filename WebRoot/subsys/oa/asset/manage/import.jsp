<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>导入固定资产资料</title>
<link rel="stylesheet" href="<%=cssPath%>/style.css">
</head>
<script Language="JavaScript"> 
function checkForm() {
  if (document.getElementById("csvFile").value == "") {
     alert("请选择要导入的文件！");
     return false;
  }
  if (document.getElementById("csvFile").value != "") {
    var fileTemp = document.getElementById("csvFile").value,fileName;
    var pos;
    pos = fileTemp.lastIndexOf("\\");
    fileName = fileTemp.substring(pos+1,fileTemp.length);
    var numLeg = fileName.lastIndexOf(".");
    document.getElementById("fileName").value = fileName;
    if (fileName.substring(numLeg+1,fileName.length) != "csv") {
      alert("只能导入CSV文件!");
      return false;
    }
  }
  return true;
}

function downCSVTemplet(){
	var downUrl = "<%=contextPath%>/yh/subsys/oa/asset/act/YHCpCpImportAct";
	location.href = downUrl + "/downCSVTemplet.act";
}
</script>
 <body class="bodycolor" topmargin="5">
  <table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
    <tr>
      <td class="Big"><img src="<%=imgPath%>/notify_new.gif"><span class="big3"> 导入固定资产资料</span><br>
      </td>
    </tr>
  </table>
  <br>
  <br>
  <div align="center" class="Big1">
    <form name="form1" id="form1" action="<%=contextPath%>/yh/subsys/oa/asset/act/YHCpCpImportAct/importAsset.act" method="post" enctype="multipart/form-data">
<table class="TableBlock" align="center" width="70%">  
 <tr class="TableData" align="center" height="30">
   <td width="150" align="right"><b>下载导入模板：</b></td>
   <td width="400" align="left">
    <a href="#" onclick="downCSVTemplet();">固定资产模板下载 </a>
   </td>
 </tr>
 <tr class="TableData" align="center" height="30">
   <td width="150" align="right"><b>&nbsp;&nbsp;选择导入文件：</b></td>
   <td align="left" width="400">
    <input type="file" name="csvFile" id="csvFile" class="BigInput" size="30">
    <input type="hidden" name="FILE_NAME">
    <input type="hidden" name="GROUP_ID" value="">
   </td>
 </tr> 
 <tr class="TableData" align="center" height="30">
  <td width="150" align="right"><b>说明：</b></td>
  <td width="400" align="left">
    <span>
    1、使用工资报表模板导入数据，先填内容再导入。

    <br>
    2、将改好的EXECL固定资产报表另存为CSV格式的文件

    </span>
  </td>
 </tr>  
<tr>
  <td nowrap  class="TableControl" colspan="2" align="center">
    <input type="submit" value="导入" class="BigButton">&nbsp;&nbsp;&nbsp;
    <input type="button" value="返回" class="BigButton" onClick="location='<%=contextPath%>/subsys/oa/hr/salary/manage/salFlow/manage.jsp';" title="返回">
  </td>
 </tr> 
</table>
</form>
</div>
</body>

