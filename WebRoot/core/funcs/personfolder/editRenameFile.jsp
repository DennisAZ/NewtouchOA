<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %> 

<%
	String contentId=request.getParameter("contentId");
	if(contentId==null){
		contentId="0";
	}
	String attachName=request.getParameter("attachName");
	if(attachName==null){
	  attachName="";
	}
	String attachId=request.getParameter("attachId");
	if(attachId==null){
	  attachId="";
	}
	String module=request.getParameter("moudel");
	if(module==null){
		module="";
	}
	

%>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>重命名文件</title>
<link rel="stylesheet"	href="<%=cssPath%>/style.css">
<script type="text/Javascript"	src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/smartclient.js"></script>
<script type="text/javascript">
//alert("module>>"+'<%=attachId %>');
var requestURL="<%=contextPath%>/yh/core/funcs/personfolder/act/YHPersonFileContentAct";
function checkForm(){
	var newName=$("newName").value.trim();
	var oldName="<%=attachName%>";
	if(newName == null || newName == "")	{
		alert("文件名不能为空！");
		 $("newName").focus();
		return false
	}
  
   if(newName == oldName)   {
      alert("新文件名与原文件名相同");
      $("newName").focus();
      return false;
   }
   return true;
}


function sendForm(){
	//alert("attachId>>"+'<%=attachId %>' +"  contnetId>>"+'<%=contentId%>' +"  module>>"+'<%=module%>');
  if(checkForm()){
	  var fileNameStr=$("newName").value; 
	  //var url=requestURL + "/renameFile.act?contnetId=<%=contentId%>&attachName=<%=attachName%>&attachId=<%=attachId%>&module=<%=module%>&reName=" + fileNameStr;
	  var url=requestURL + "/renameFile.act";
	  var pars=$("form1").serialize();
	  var json = getJsonRs(url,pars);
	  //alert("rsText:"+rsText);
		 if(json.rtState == "1"){
		    alert(json.rtMsrg); 
		    return ;
		  }
		  var prcs=json.rtData;
		  var successFlag=prcs.successFlag;

		  if(successFlag == "1"){
			  $("formDiv").hide();
			  $("returnDiv").show();
			}else{
				$("formDiv").hide();
				$("errorDir").show();
			}
  }
}



</script>

</head>
<body class="bodycolor" topmargin="5" leftmargin="0" onload="form1.newName.focus();form1.newName.select();">
<div id="formDiv" style="display: ">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big3"><img src="<%=imgPath%>/folder_edit.gif" WIDTH="22" HEIGHT="20" align="absmiddle"> <b>重命名文件 — <%=attachName %></b></td>
  </tr>
</table>
<br>
<form action=""  method="post" name="form1" id="form1" onsubmit="return checkForm();">
	<input type="hidden" name="attachId" value="<%=attachId %>">
	<input type="hidden" name="attachName" value="<%=attachName %>">
	<input type="hidden" name="module" value="<%=module.trim() %>">
	<input type="hidden" name="contentId" value="<%=contentId %>">

<table class="TableBlock" width="90%" align="center">
    <tr class="TableData">
      <td width="60">原文件名：</td>
      <td><%=attachName%></td>
    </tr>
    <tr class="TableData">
      <td width="60">新文件名：</td>
      <td><input type="text" class="BigInput" size="20" name="newName" id="newName" value="<%=attachName%>"></td>
    </tr>
    <tr align="center" class="TableControl">
      <td colspan="5" nowrap>
       <input type="button" value="保存"	 onclick="sendForm();" class="BigButton">&nbsp;&nbsp; 
        <input type="reset" value="重置" class="BigButton">&nbsp;&nbsp;
        <input type="button" value="关闭" class="BigButton" onclick="window.close();">
      </td>
    </tr>
  </table>
</form>
</div>

<div id="returnDiv" style="display: none">
<table class="MessageBox" align="center" width="210">
  <tr>
    <td class="msg blank">
      <h4 class="title">提示</h4>
      <div class="content" style="font-size:12pt">文件重命名成功！</div>
    </td>
  </tr>
</table>
<div class="big1" align="center">
<input type="button"  value="关闭" class="SmallButton" onClick="window.parent.opener.location.reload();window.close();">
</div>
</div>




<div id="errorDir" style="display: none">
<table class="MessageBox" align="center" width="280">
  <tr>
    <td class="msg info">
      <h4 class="title">提示</h4>
      <div class="content" style="font-size:12pt">原文件不存在！</div>
    </td>
  </tr>
</table>
</div>
 
</body>

</html>