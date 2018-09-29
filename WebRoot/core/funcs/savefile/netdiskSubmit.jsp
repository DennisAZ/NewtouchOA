<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %> 
<%
	String diskPath=request.getParameter("checkId");
	String attachId=request.getParameter("attachId");
	String attachName=request.getParameter("attachName");
	String subject=request.getParameter("subject");
	String module=request.getParameter("module");
	if(module==null){
		module="";
	}
	
	if(diskPath==null){
	  diskPath="";
	}
	if(subject==null){
	  subject="";
	}
	

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>文件转存</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>

<script type="text/javascript">
//alert("diskPath:"+'<%=diskPath%>' +" attachId:"+'<%=attachId%>' + "  attachName:"+'<%=attachName%>' +"  module:"+'<%=module%>');
var requestURL="<%=contextPath%>/yh/core/funcs/system/netdisk/act/YHNetdiskAct";
function doInit(){
	var url = requestURL + "/transferNetdisk.act";
	var pars = $('form1').serialize()
	var json=getJsonRs(url,pars);
	//alert("rsText>>:"+rsText);
	if(json.rtState == '1'){
		alert(json.rtMsrg);
		return ;				
	}
	var prcsJson=json.rtData;
	var flag=prcsJson.flag;
	var exist=prcsJson.exist;
	var dirName=prcsJson.dirName;
	var existName=prcsJson.existName;
	if(exist == "true"){
		$("isExist").show();
		$("existDirName").innerHTML=dirName;
		$("existName").innerHTML=existName;
	}else if(flag == "true"){
		$("listDiv").show();
		$("dirName").innerHTML=dirName;
	}else{
		$("failDiv").show();
		$("failDirName").innerHTML=dirName;
	}

	
}

</script>
</head>
<body onload="doInit();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/save_file.gif" align="middle"><span class="big3"> <%=attachName %></span>
    </td>
    </tr>
</table>

<div id="listDiv" style="display: none">
	<table class="MessageBox" align="center" width="260">
	  <tr>
	    <td class="msg blank">
	      <div class="content" style="font-size:12pt">文件转存至【<span id="dirName"></span>】成功</div>
	    </td>
	  </tr>
	</table>	 
	<div class="big1" align="center">
		<input type="button"  value="关闭" class="SmallButtonW" onClick="window.close();">
	</div>
</div>

<div id="failDiv" style="display: none">
	<table class="MessageBox" align="center" width="260">
	  <tr>
	    <td class="msg blank">
	      <div class="content" style="font-size:12pt">文件转存至【<span id="failDirName"></span>】失败</div>
	    </td>
	  </tr>
	</table>	 
	<div class="big1" align="center">
		<input type="button"  value="关闭" class="SmallButtonW" onClick="window.close();">
	</div>
</div>

<div id="isExist" style="display: none">
	<table class="MessageBox" align="center" width="430">
	  <tr>
	    <td class="msg blank">
	      <div class="content" style="font-size:12pt">【<span id="existDirName"></span>】目录下文件【<span id="existName"></span>】已经存在</div>
	    </td>
	  </tr>
	</table>
	<br>
	<div align="center">
	  <input type="button"  value="上一步" class="SmallButtonW" onClick="history.back();">&nbsp;&nbsp;
	  <input type="button"  value="关闭" class="SmallButtonW" onClick="window.close();">
	</div>
</div>


<form name="form1" id="form1" action="" method="post">
	<input type="hidden" name="attachId" value="<%=attachId %>">
	<input type="hidden" name="attachName" value="<%=attachName %>">
	<input type="hidden" name="module" value="<%=module.trim() %>">
	<input type="hidden" name="subject" value="<%=subject %>">
	<input type="hidden" name="diskPath" id="checkId" value="<%=diskPath %>">
</form>
</body>
</html>