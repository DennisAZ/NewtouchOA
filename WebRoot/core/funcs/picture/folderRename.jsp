<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %> 
<%
	String seqId=request.getParameter("picId");	
	String subDir=request.getParameter("subDir");	
	if(subDir==null){
		subDir="";
	}
	
%>

<%
	String pageAscDesc=request.getParameter("ascDescFlag");
	String pageField=request.getParameter("field");
	if(pageAscDesc==null || "".equals(pageAscDesc.trim())){
		pageAscDesc="0";
	}
	if(pageField==null || "".equals(pageField.trim())){
		pageField="NAME";
	}

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>重命名文件夹</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/javascript">
var requestURL="<%=contextPath%>/yh/core/funcs/picture/act/YHPictureAct";
function checkForm(){
	if($("folderName").value.trim()==""){
		alert("文件夹名不能为空！");
		$("folderName").focus();
		$("folderName").select();
		return false;
	}
	if(checkStr($("folderName").value)){
		alert("不能包含有以下字符/\:*<>?\"|！");
		$("folderName").select();
		$("folderName").focus();
		return false;
	}
	return true;
}
function checkStr(str){ 
	var re=/["\/\\:*?"<>|]/; 
	return str.match(re); 
}
function sendForm(){
	//alert("aaa");
	if(checkForm()){
		var folderName = $("folderName").value;
		var subDirStr = "<%=subDir%>";
		var url= requestURL+"/folderRename.act?seqId=<%=seqId%>&subDir=" + encodeURIComponent(subDirStr) + "&folderName=" + encodeURIComponent(folderName);
		//alert(url);
	  var json = getJsonRs(url);	 
	  //alert("rsText:"+rsText);
	  if(json.rtState == "1"){
	    alert(json.rtMsrg); 
	    return ;
	  }
	  var prc = json.rtData;
	  
	  var subDir=prc.subDir;
	  var isExist=prc.isExist;
	  var thisDir=prc.thisDir;
	  var flag=prc.flag;
	  var returnSubDir = encodeURIComponent(subDir);
	  //alert("subDir:"+subDir +"  isExist:"+isExist+"  thisDir:"+thisDir);
	  if(flag=="true"){
			parent.opener.location.href="<%=contextPath%>/yh/core/funcs/picture/act/YHPictureAct/getPicInfoById.act?ascDescFlag=<%=pageAscDesc %>&field=<%=pageField %>&subDir=" + returnSubDir +"&seqId=<%=seqId%>";
			$("formDiv").hide();
			$("returnDiv").show();		
			
	  }else{
	  	$("formDiv").hide();
	  	$("folderError").innerHTML=$("folderName").value;  	
			$("errorDir").show();
	  }
	  
	}	

	//window.opener.document.location.reload();	
	//parent.opener.location.reload();
}

function toBack(){
	//alert("tt");
	$("errorDir").hide();
	$("formDiv").show();
	$("folderName").focus();
	$("folderName").select();
}

</script>
</head>
<body>
<div id="formDiv" style="display: ">
<body class="" onload="form1.folderName.focus();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
 		<td class="Big3"><img src="<%=contextPath %>/core/funcs/picture/images/folder_edit.gif" WIDTH="22" HEIGHT="20" align="absmiddle"> <b>重命名文件夹</b></td>
	</tr>
</table>
<br>
<form action=""  method="post" name="form1">
<table class="TableBlock" width="90%" align="center">
    <tr class="TableData">
      <td width="60">文件夹名：</td>
      <td><input type="text" class="BigInput" size="20" name="folderName" id="folderName" value=""></td>
    </tr>
    <tr align="center" class="TableControl">
      <td colspan="5" nowrap>
        <input type="button" value="保存" onclick="sendForm();" class="BigButton">&nbsp;&nbsp;
        <input type="button" value="关闭" class="BigButton" onclick="window.close();">
      </td>
    </tr>
  </table>
</form>
</div>

<div id="returnDiv" style="display: none">
<table class="MessageBox" align="center" width="220">
  <tr>
    <td class="msg info">
      <h4 class="title">提示</h4>
      <div class="content" style="font-size:12pt">操作成功</div>
    </td>
  </tr>
</table>

<div align="center"><input type="button" class="BigButton" value="关闭" onclick="window.close();"></div>
</div>


<div id="errorDir" style="display: none">
<table class="MessageBox" align="center" width="310">
  <tr>
    <td class="msg error">
      <h4 class="title">错误</h4>
      <div class="content" style="font-size:12pt">文件夹 <span id="folderError"> </span>  已存在</div>
    </td>
  </tr>
</table>
<br><center><input type="button" class="BigButton" value="返回" onclick="toBack();"></center>
 
<%--<div align="center"><input type="button" class="BigButton" value="关闭" onclick="window.close();"></div> --%>

</div>



</body>
</html>