<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<%
  String pathId = request.getParameter("DISK_ID");
			//String folder = request.getParameter("FOLDER_NAME");

	String seqIdStr = request.getParameter("seqId");
	int seqId = 0;
	if (seqIdStr != null) {
		seqId = Integer.parseInt(seqIdStr);
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>新建文件夹</title>
<link href="<%=cssPath%>/style.css" rel="stylesheet" type="text/css" />
<script type="text/Javascript"	src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/smartclient.js"></script>

<script>
//alert("pathId:"+'<%=pathId%>');
//alert("<%=seqId%>");
var currIdStr='<%=pathId%>';
var seqId = "<%=seqId%>";
function checkForm(){
	if(form1.FILE_NAME.value == null || form1.FILE_NAME.value.trim() == "")	{
		alert("文件夹名字不能为空！")
		$("FILE_NAME").select();
		$("FILE_NAME").focus();
		return false
	}
	if(checkStr($("FILE_NAME").value)){
		alert("不能包含有以下字符/\:*<>?\"|！");
		$("FILE_NAME").select();
		$("FILE_NAME").focus();
		return false;
	}
	return true
}
function checkStr(str){ 
	var re=/["\/\\:*?"<>|]/; 
	return str.match(re); 
}

function sendForm(){
	var fileName=$("FILE_NAME").value;
	if(checkForm()){
		//var pathIdStr="<%=pathId%>";
		//var encodeDiskId=encodeURIComponent(pathIdStr);
		//var encodefileName=encodeURIComponent(fileName);
		//var url="<%=contextPath%>/yh/core/funcs/netdisk/act/YHNetDiskAct/addFileFolder.act?seqId=<%=seqId%>&DISK_ID="+ encodeDiskId +"&FILE_NAME="+encodefileName;
		var url="<%=contextPath%>/yh/core/funcs/netdisk/act/YHNetDiskAct/addFileFolder.act";
		var pars = $('form1').serialize();
		var json = getJsonRs(url,pars);
	  //alert("rsText:"+rsText);

		 if(json.rtState == "1"){
		    alert(json.rtMsrg); 
		    return ;
		  }
		 var prc = json.rtData;
		 var sucuss=prc.sucuss;
		 var isExist=prc.isExist;
		 var flag=prc.flag;

		 var returnDiskId=prc.returnDiskId;
		 var nodeName=prc.nodeName;
		 var returnDiskId=prc.returnDiskId;

		  if(flag=="true"){
				//alert("aaa");
				$("folderNameStr").innerHTML=fileName +" 成功";
				//parent.opener.location.reload();
				$("formDiv").hide();
				$("returnDiv").show();	

				var curTree = self.opener.parent.frames["file_list"].tree;
				var curNode = curTree.getNode(currIdStr);
				var imgAddress = "<%=imgPath%>/dtree/folder.gif";
				//alert("curNode>>"+curNode);
				if(curNode){
					var node = {
							parentId:curNode.nodeId,
							nodeId:returnDiskId,
							name:nodeName,
							isHaveChild:0,
							extData:seqId,
							imgAddress:imgAddress
					}
					curTree.addNode(node);
					var url="<%=contextPath%>/core/funcs/netdisk/fileList.jsp?DISK_ID=" + encodeURIComponent(returnDiskId) + "&seqId=<%=seqId%>" ;
					//alert("url>>>>"+url);
					self.opener.parent.frames["file_main"].location.href = url;
				}else{
					//alert("刷新页面>>");
					self.opener.parent.frames["file_list"].location.reload();
				}

			
				
		  }else{

		    if(sucuss == 0){
		      $("formDiv").hide();
			  	$("folderError").innerHTML=$("FILE_NAME").value + " 创建失败";  	
					$("errorDir").show();
					
			   }else if(isExist == 0){
			  	$("formDiv").hide();
			  	$("folderError").innerHTML=$("FILE_NAME").value + " 已存在";  	
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
<body class="" onload="form1.FILE_NAME.focus();">
<div id="formDiv" style="display: ">
<table border="0" width="100%" cellspacing="0" cellpadding="3"
	class="small">
	<tr>
		<td class="Big3"><img src="<%=contextPath%>/core/funcs/netdisk/images/newfolder.gif" align="absmiddle">
		<b>新建文件夹</b></td>
	</tr>
</table>
<br>
<form action="" method="post" name="form1" id="form1"
	onsubmit="javascript:return checkForm();">
<table class="TableBlock" width="90%" align="center">
	<tr class="TableData">
		<td width="60">文件夹名：</td>
		<td><input type="text" class="BigInput" size="20"
			name="FILE_NAME" id="FILE_NAME" value=""></td>
	</tr>
	<tr align="center" class="TableControl">
		<td colspan="5" nowrap>
		<input type="hidden" name="seqId"	value="<%=seqId%>"> 
		<input type="hidden" name="DISK_ID" value="<%=pathId%>"> 
		<input type="hidden" name="ORDER_BY" value="">
		 <input type="hidden" name="ASC_DESC"	value=""> 
		 <input type="button" value="保存"	 onclick="sendForm();" class="BigButton">&nbsp;&nbsp; 
		 <input	type="reset" value="重置" class="BigButton">&nbsp;&nbsp; 
		 <input	type="button" value="关闭" class="BigButton" onclick="window.close();">
		</td>
	</tr>
</table>
</form>
</div>

<div id="returnDiv" style="display: none">
<table class="MessageBox" align="center" width="330">
	<tr>
		<td class="msg info">
		<div class="content" style="font-size: 12pt">新建文件夹 <span 	id="folderNameStr"> </span> </div>
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
		<div class="content" style="font-size: 12pt">文件夹 <span id="folderError"> </span> </div>
		</td>
	</tr>
</table>
<br>
<center><input type="button" class="BigButton" value="返回"
	onclick="toBack();"></center>&nbsp;&nbsp;</div>





</body>
</html>