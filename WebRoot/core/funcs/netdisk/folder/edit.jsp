<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@page import="java.net.URLEncoder" %>
<%
	String pathId = request.getParameter("DISK_ID");
	//pathId = URLEncoder.encode(pathId, "UTF-8");
	String folder = "";
	if(pathId !=null && pathId.lastIndexOf('/') > 0){
		 folder = pathId.substring(pathId.lastIndexOf('/')+1, pathId.length());
	}
	
	String seqIdStr=request.getParameter("seqId");
	int seqId=0;
	if(seqIdStr!=null){
	  seqId=Integer.parseInt(seqIdStr);
	}


%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>重命名文件夹</title>
<link href="<%=cssPath%>/style.css" rel="stylesheet" type="text/css" />
<script type="text/Javascript"	src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/smartclient.js"></script>
<script>
//alert("<%=pathId%>");
var requestURL="<%=contextPath%>/yh/core/funcs/netdisk/act/YHNetDiskAct";
var diskIdStr='<%=pathId%>';
var seqId = "<%=seqId%>";
function checkForm(){
	if(form1.FILE_NAME.value == null || form1.FILE_NAME.value == "")	{
		alert("文件夹名字不能为空！")
		 $("FILE_NAME").focus();
		return false
	}
	if(checkStr($("FILE_NAME").value)){
		alert("不能包含有以下字符/\:*<>?\"|！");
		$("FILE_NAME").select();
		$("FILE_NAME").focus();
		return false;
	}
  if(form1.FILE_NAME.value == form1.OLD_FILE_NAME.value)  {
     alert("新文件夹名与原文件夹名相同");
     $("FILE_NAME").focus();
   	$("FILE_NAME").select();
     return false
  }
  return true
}

function checkStr(str){ 
	var re=/["\/\\:*?"<>|]/; 
	return str.match(re); 
}


function sendForm(){  
  if(checkForm()){
    //var fileNameStr=$("FILE_NAME").value; 
		//var url= requestURL + "/renameFileFolder.act?seqId=<%=seqId%>&diskId=" + diskIdStr +"&fileName=" + fileNameStr;
		var url= requestURL + "/renameFileFolder.act";
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
		  $("folderNameStr").innerHTML=$("FILE_NAME").value +" 成功";
			$("formDiv").hide();
			$("returnDiv").show();	

			var curTree = self.opener.parent.frames["file_list"].tree;
			var curNode = curTree.getNode(diskIdStr);
			var imgAddress = "<%=imgPath%>/dtree/folder.gif";
			if(curNode){
				var node = {
						parentId:curNode.parentId,
						nodeId:returnDiskId,
						name:nodeName,
						isHaveChild:curNode.isHaveChild,
						extData:seqId,
						imgAddress:imgAddress
				}
				curTree.updateNode(curNode.nodeId, node);
				var url="<%=contextPath%>/core/funcs/netdisk/fileList.jsp?DISK_ID=" + encodeURIComponent(returnDiskId) + "&seqId=<%=seqId%>" ;
				self.opener.parent.frames["file_main"].location.href = url;
			}else{
			//alert("刷新页面>>");
				self.opener.parent.frames["file_list"].location.reload();
			}
		}else{

		  if(sucuss == 0){
	      $("formDiv").hide();
		  	$("folderError").innerHTML=$("FILE_NAME").value + " 重命名失败";  	
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
<body>
<body onLoad="form1.FILE_NAME.focus();form1.FILE_NAME.select();">
<div id="formDiv" style="display: ">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big3"><img src="<%=contextPath%>/core/funcs/netdisk/images/folder_edit.gif" WIDTH="22" HEIGHT="20" align="absmiddle"> <b>重命名文件夹</b></td>
  </tr>
</table>
<br>
<form action=""  method="post" name="form1" id="form1" onsubmit="javascript:return checkForm();">
<table class="TableBlock" width="90%" align="center">
    <tr class="TableLine1">
      <td width="80">原文件夹名：</td>
      <td><%=folder%></td>
    </tr>
    <tr class="TableLine2">
      <td width="80">新文件夹名：</td>
      <td><input type="text" class="BigInput" size="20" name="FILE_NAME" id="FILE_NAME" value="<%=folder %>"></td>
    </tr>
    <tr align="center" class="TableControl">
      <td colspan="5" nowrap>
        <input type="hidden" name="OLD_FILE_NAME" value="<%=folder%>">
        <input type="hidden" name="seqId" value="<%=seqId%>">
        <input type="hidden" name="diskId" value="<%=pathId%>">
        
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
		<div class="content" style="font-size: 12pt">重命名文件夹 <span 	id="folderNameStr"> </span> </div>
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
<center><input type="button" class="BigButton" value="返回"	onclick="toBack();"></center>&nbsp;&nbsp;</div>



</body>
</html>