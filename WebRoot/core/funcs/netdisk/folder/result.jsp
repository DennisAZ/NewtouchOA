<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="yh.core.global.YHActionKeys"%>
<%@ include file="/core/inc/header.jsp" %>
<%
	String seqIdStr=request.getParameter("seqId");
	String pathId = (String)request.getAttribute("DISK_ID");
	String folder = (String)request.getAttribute("FOLDER_NAME");
	String operate = (String)request.getAttribute("OPERATE");
	String rtState = (String)request.getAttribute(YHActionKeys.RET_STATE);
	String rtMsrg = (String)request.getAttribute(YHActionKeys.RET_MSRG);
	String rtData = (String)request.getAttribute(YHActionKeys.RET_DATA);
	
	String idStr=request.getParameter("DISK_ID");
	if(idStr==null){
	  idStr="";
	}
	
	
	int width = rtMsrg.length()*10 + 180;
	width = width > 500 ? 500 : width;
	
	int seqId=0;
	if(seqIdStr!=null){
		seqId=Integer.parseInt(seqIdStr);  
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>信息页面</title>
<link href="<%=cssPath%>/style.css" rel="stylesheet" type="text/css" />
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/DTree1.0.js"></script>
<script>
//alert('<%=seqId%>');
//alert("idStr>>"+'<%=idStr%>');
var idStr="<%=idStr%>"
var curTree = self.opener.parent.frames["file_list"].tree;
var curNode = curTree.getNode(idStr);
//var curNode = curTree.getCurrNode();
var nodeId = "<%=pathId%>";
var nodeName = "<%=folder%>";
var opt = "<%=operate%>";
var seqId = "<%=seqId%>";
var imgAddress = "<%=imgPath%>/dtree/folder.gif";
if(opt.indexOf("new") == 0){
	var node = {
			parentId:curNode.nodeId,
			nodeId:nodeId,
			name:nodeName,
			isHaveChild:0,
			extData:seqId,
			imgAddress:imgAddress
	}
	curTree.addNode(node);
	//location.reload();
		var url="<%=contextPath%>/core/funcs/netdisk/file_list.jsp?DISK_ID=" +nodeId + "&seqId=<%=seqId%>" ;
		self.opener.parent.frames["file_main"].location.href = encodeURI(url);
		//parent.parent.parent.frames["file_main"].location=encodeURI(url);
}else if(opt.indexOf("rename") == 0){
	var node = {
			parentId:curNode.parentId,
			nodeId:nodeId,
			name:nodeName,
			isHaveChild:curNode.isHaveChild,
			extData:seqId,
			imgAddress:imgAddress
	}
	curTree.updateNode(curNode.nodeId, node);
	var url="<%=contextPath%>/core/funcs/netdisk/file_list.jsp?DISK_ID=" +nodeId + "&seqId=<%=seqId%>" ;
	self.opener.parent.frames["file_main"].location.href = encodeURI(url);
}else if(opt.indexOf("delete") == 0){
  alert(nodeId);
	//curTree.removeNode(nodeId);
	//self.opener.location.href = "<%=contextPath%>/core/funcs/netdisk/file_list.jsp?seqId=<%=seqId%>&DISK_ID=" + curNode.parentId;
}else{
	//alert("error data");
}
//alert('<%=rtMsrg%>');
</script>
</head>
<body>
<table class="MessageBox" align="center" width="<%=width%>">
  <tr>
    <td class="msg info">
      <h4 class="title">文件夹</h4>
      <div class="content" style="font-size:12pt" ><%=rtMsrg%></div>
    </td>
  </tr>
</table>
<br>
<div align="center"><input type="button" class="BigButton" value="关闭" onClick="window.close();"></div>
</body>
</html>