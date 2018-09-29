<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
	String seqId=request.getParameter("seqId");
	String diskId=request.getParameter("diskId");
	if(diskId == null){
		diskId = "";
	}
	if(seqId == null){
		seqId = "0";
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>树形文件目录</title>

<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/DTree1.0.js"></script>
<script type="text/javascript"><!--
//alert("diskID>>>"+'<%=diskId%>' + '<%=seqId%>');
var tree = null;
function doInit()
{
	tree =  new DTree({bindToContainerId:'xtree', 
		requestUrl:contextPath + "/yh/core/funcs/netdisk/act/YHNetDiskAct/getTreebyFileSystem.act?seqId=<%=seqId%>&DISK_ID=<%=diskId%>&id=", 
		isOnceLoad:false, 
		linkPara:{clickFunc:getFilesInfo},
		checkboxPara:{isHaveCheckbox:false}	
		, isWrodWrap:true	
	});
	tree.show();
	var diskPath = "<%=diskId %>";
	if(diskPath.trim()){
		var url="<%=contextPath%>/core/funcs/netdisk/fileList.jsp?DISK_ID=" + encodeURIComponent(diskPath) + "&seqId=<%=seqId%>";
		self.parent["file_main"].location.href = url;
	}
	
}
function getFilesInfo(id){	
	//alert("id<<"+id);
	var node = tree.getRootNode(id);
	var parentNodeId=node.extData;
	var seqId=node.extData;
	//alert(node.extData);seqId用于获取权限 ，但区分不了根目录与子目录的关系
	var encodeDiskId=encodeURIComponent(id);
	var url="<%=contextPath%>/core/funcs/netdisk/fileList.jsp?DISK_ID=" + encodeDiskId + "&seqId=" + seqId;
	//var url=contextPath + "/core/funcs/netdisk/sortTest.jsp?DISK_ID=" + id + "&seqId=" + seqId;  
	//alert(url);
	self.parent["file_main"].location.href = url;
	
}
--></script>
</head>
<body onload="doInit()" class="nocolor">
<div id="xtree"></div>
</body>
</html>