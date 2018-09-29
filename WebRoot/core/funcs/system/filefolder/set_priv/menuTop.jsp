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
<title></title>
<link rel="stylesheet" href = "<%=cssPath %>/style.css">
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath %>/cmp/ExchangeSelect.css" type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/ExchangeSelect1.0.js"></script>

<script type="text/javascript">
//alert('menuTop.jsp:<%=seqId%>');

function doInit(){
	var jso = [
							{title:"访问权限", useTextContent:false ,imgUrl:"", contentUrl:"<%=contextPath %>/core/funcs/system/filefolder/set_priv/setVisit.jsp?seqId=<%=seqId%>" ,useIframe:true}
             ,{title:"新建权限", useTextContent:false , imgUrl:"",contentUrl:"<%=contextPath %>/core/funcs/system/filefolder/set_priv/setCreate.jsp?seqId=<%=seqId%>" , useIframe:true}
	         	 ,{title:"编辑权限", useTextContent:false ,imgUrl:"", contentUrl:"<%=contextPath %>/core/funcs/system/filefolder/set_priv/setManage.jsp?seqId=<%=seqId%>" ,useIframe:true}
	         	 ,{title:"删除权限", useTextContent:false ,imgUrl:"", contentUrl:"<%=contextPath %>/core/funcs/system/filefolder/set_priv/setDelete.jsp?seqId=<%=seqId%>" ,useIframe:true}
             ,{title:"下载/打印权限", useTextContent:false , imgUrl:"",contentUrl:"<%=contextPath %>/core/funcs/system/filefolder/set_priv/setDownload.jsp?seqId=<%=seqId%>" , useIframe:true}
             ,{title:"所有者", useTextContent:false , imgUrl:"",contentUrl:"<%=contextPath %>/core/funcs/system/filefolder/set_priv/setOwner.jsp?seqId=<%=seqId%>" , useIframe:true}
             ,{title:"批量设置", useTextContent:false , imgUrl:"",contentUrl:"<%=contextPath %>/core/funcs/system/filefolder/set_priv/setBatch.jsp?seqId=<%=seqId%>" , useIframe:true}
             ];  
  buildTab(jso, 'contentDiv', 800);
		
}
</script>

</head>
<body onload="doInit();">
<div id="contentDiv"></div>


</body>
</html>