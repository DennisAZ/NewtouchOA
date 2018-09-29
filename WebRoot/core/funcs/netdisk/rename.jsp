<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
	String pathId = request.getParameter("DISK_ID");
	String fileName = request.getParameter("FILE_NAME");
	fileName = fileName.substring(0, fileName.length() - 1);
	String seqIdStr=request.getParameter("seqId");
	int seqId=0;
	if(seqIdStr!=null){
	  seqId=Integer.parseInt(seqIdStr);
	}
	
	if(fileName==null){
	  fileName="";	  
	}
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>重命名文件</title>
<link href="<%=cssPath%>/style.css" rel="stylesheet" type="text/css" />
<script type="text/Javascript"	src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/smartclient.js"></script>
<script>
//alert('<%=seqId%>');
var requestURL="<%=contextPath%>/yh/core/funcs/netdisk/act/YHNetDiskAct";
function checkForm(){

	if(form1.FILE_NAME.value == null || form1.FILE_NAME.value == "")	{
		alert("文件名不能为空！")
		 $("FILE_NAME").focus();
		return false
	}
	if(checkStr($("FILE_NAME").value)){
		alert("不能包含有以下字符/\:*<>?\"|！");
		$("FILE_NAME").select();
		$("FILE_NAME").focus();
		return false;
	}  
  if(document.form1.FILE_NAME.value == $("oldFileName").value)   {
    alert("新文件名与原文件名相同");
    $("FILE_NAME").focus();
   	$("FILE_NAME").select();
    return false;
   }
   return true;
}
function checkStr(str){ 
	var re=/["\/\\:*?"<>|]/; 
	return str.match(re); 
}


function sendForm(){

  if(checkForm()){
    //var fileNameStr=$("FILE_NAME").value; 
		//var url= requestURL + "/renameFile.act?seqId=<%=seqId%>&diskId=<%=pathId%>&oldFileName=<%=fileName%>&newFileName=" + fileNameStr;
		var url= requestURL + "/renameFile.act";
    var pars = $('form1').serialize();
		var json = getJsonRs(url,pars);
	  //alert("rsText:"+rsText);
		 if(json.rtState == "1"){
		    alert(json.rtMsrg); 
		    return ;
		  }

		var prc = json.rtData;
		var flag=prc.flag;
		var sucuss=prc.sucuss;
		var isExist=prc.isExist;
		if(flag == 1){
		  opener.location.reload();
		  window.close();
		}else{

			if(sucuss == 0){
				//"文件重命名失败"
			  $("formDiv").hide();
		  	$("folderError").innerHTML="文件 " + $("FILE_NAME").value + " 重命名失败";  	
				$("errorDir").show();

			}else if(isExist == 1){
				//"文件已存在"
			  $("formDiv").hide();
		  	$("folderError").innerHTML="文件 " + $("FILE_NAME").value + " 已存在";  	
				$("errorDir").show();
			}
		}
  }
}


function toBack(){
	//alert("tt");
	$("errorDir").hide();
	$("formDiv").show();
	$("FILE_NAME").focus();
	$("FILE_NAME").select();
}


</script>
</head>
<body onload="form1.FILE_NAME.focus();form1.FILE_NAME.select();">
<div id="formDiv" style="display: ">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big3"><img src="<%=contextPath%>/core/funcs/netdisk/images/folder_edit.gif" WIDTH="22" HEIGHT="20" align="absmiddle"> <b>重命名文件 — <%=fileName %></b></td>
  </tr>
</table>
<br>
<form action="<%=contextPath%>/yh/core/funcs/netdisk/act/YHNetDiskAct/renameFile.act"  method="post" name="form1" id="form1" onsubmit="return checkForm();">
<table class="TableBlock" width="90%" align="center">
    <tr class="TableData">
      <td width="60">原文件名：</td>
      <td><%=fileName%></td>
    </tr>
    <tr class="TableData">
      <td width="60">新文件名：</td>
      <td><input type="text" class="BigInput" size="20" name="FILE_NAME" id="FILE_NAME" value="<%=fileName%>"></td>
    </tr>
    <tr align="center" class="TableControl">
      <td colspan="5" nowrap>
        <input type="hidden" name="seqId" value="<%=seqId %>">
        <input type="hidden" name="diskId" value="<%=pathId%>">        
        <input type="hidden" id="oldFileName" name="oldFileName" value="<%=fileName%>">
        
       	<input type="button" value="保存"	 onclick="sendForm();" class="BigButton">&nbsp;&nbsp; 
        <input type="reset" value="重置" class="BigButton">&nbsp;&nbsp;
        <input type="button" value="关闭" class="BigButton" onclick="window.close();">
      </td>
    </tr>
  </table>
</form>
</div>

<div id="returnDiv" style="display: none">
<table class="MessageBox" align="center" width="330">
	<tr>
		<td class="msg info">
		<div class="content" style="font-size: 12pt"><span 	id="folderNameStr"> </span> </div>
		</td>
	</tr>
</table>
<br>
<div align="center"><input type="button" class="BigButton"
	value="关闭" onclick="window.close();"></div>
</div>


<div id="errorDir" style="display: none">
<table class="MessageBox" align="center" width="310">
	<tr>
		<td class="msg error">
		<h4 class="title">错误</h4>
		<div class="content" style="font-size: 12pt"> <span id="folderError"> </span> </div>
		</td>
	</tr>
</table>
<br>
<center><input type="button" class="BigButton" value="返回"	onclick="toBack();"></center>&nbsp;&nbsp;</div>

</body>
</html>