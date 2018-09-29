<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
	String fileName = request.getParameter("fileName");
	String filePath = request.getParameter("filePath");
	if(fileName==null){
		fileName = "";
	}
	if(filePath==null){
		filePath = "";
	}

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet"	href="<%=cssPath%>/style.css">
<script type="text/Javascript"	src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/smartclient.js"></script>
<title>编辑文本</title>
<script type="text/javascript">
var requestURL="<%=contextPath%>/yh/core/funcs/netdisk/act/YHNetDiskAct";

function doInit() {
	//$("form1").submit
	var pars = Form.serialize($('form1'));
	var url = requestURL + "/getInfoFromText.act";
	var rtJson = getJsonRs(url, pars);
	if (rtJson.rtState == "0") {
    if (Prototype.Browser.IE) {
      $("textContent").value = rtJson.rtData.textData.replace(/&#10;/g, "\r").replace(/&#13;/g, "\n").replace(/&#34;/g, "\"").replace(/&#39;/g, "'").replace(/&#92;/g, "\\");
    }else {
		  $("textContent").update(rtJson.rtData.textData.replace(/&#10;/g, "\r").replace(/&#13;/g, "\n").replace(/&#34;/g, "\"").replace(/&#39;/g, "'").replace(/&#92;/g, "\\"));
    }
	}else {
		alert(rtJson.rtMsrg); 
	}
}

function doSubmit(){
	var pars = Form.serialize($('form1'));
	var url = requestURL + "/saveInfoToText.act";
	var rtJson = getJsonRs(url,pars);
	if(rtJson.rtState == "0"){
		//alert("保存成功");
		location.href = contextPath + "/core/funcs/netdisk/editTextWarn.jsp";
	}else{
		alert(rtJson.rtMsrg); 
	}
}
</script>
</head>
<body class="bodycolor" onload="doInit();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big3"><img src="<%= imgPath%>/folder_edit.gif" WIDTH="22" HEIGHT="20" align="middle"> <b>编辑文件 — <%=fileName%></b></td>
  </tr>
</table>
<form action=""  method="post" name="form1" id="form1">
<table class="TableBlock" width="90%" align="center" cellspacing="0" cellpadding="0">
    <tr align="center" class="TableControl">
      <td nowrap>
        <input type="button" value="保存" onclick="doSubmit();" class="BigButton">&nbsp;&nbsp;
        <input type="button" value="关闭" class="BigButton" onClick="window.close();">
      </td>
    </tr>
    <tr>
      <td>
         <textarea class="BigInput" name="textContent" id="textContent"  style="width:95%;height:400px;">
         <%=filePath %></textarea>
      </td>
    </tr>
    <tr align="center" class="TableControl">
      <td nowrap>
        <input type="hidden" name="filePath" value="<%=filePath %>">
        <input type="hidden" name="fileName" value="<%=fileName %>>">
        <input type="button" value="保存" onclick="doSubmit();" class="BigButton">&nbsp;&nbsp;
        <input type="button" value="关闭" class="BigButton" onClick="window.close();">
      </td>
    </tr>
  </table>
</form>
</body>
</html>