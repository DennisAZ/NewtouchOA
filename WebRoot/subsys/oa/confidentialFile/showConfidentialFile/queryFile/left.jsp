<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %> 
<%
	String seqId=request.getParameter("seqId");
	if(seqId==null){
	  seqId="";
	}
%>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/DTree1.0.js"></script>

<script type="text/javascript">
var seqId='<%=seqId%>';
var tree = null;
function doInit(){	
  tree =  new DTree({bindToContainerId:'xtree' 
  ,requestUrl:contextPath + '/yh/subsys/oa/confidentialFile/act/YHShowConfidentialSortAct/getPrivTree.act?seqId=<%=seqId %>&id='
  ,isOnceLoad:false
  ,checkboxPara:{isHaveCheckbox:false}
	,linkPara:{clickFunc:test}
	,treeStructure:{isNoTree:false}
  , isWrodWrap:true	
});

tree.show();
}
function test(id){  
//alert('you click' + id);
//var node=tree.getNode(id);//node得到的是你点击的结点的一个js对象
//var parentId=node.parentId;
//alert("curNode:"+node+"  parentId>>>:"+parentId);

	if(id){
		url="folder.jsp?seqId=" + id 
		//alert(url);
		parent.file_main.location.href=url;
	}
}
</script>
</head>
<body onload="doInit();">
<div id="xtree"></div>
</body>
</html>