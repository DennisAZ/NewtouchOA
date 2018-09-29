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
{title:"按文件夹显示", useTextContent:false ,imgUrl:"<%=imgPath %>/notify_open.gif", contentUrl:"<%=contextPath %>/subsys/oa/confidentialFile/setConfidentialFile/showPriv/bySort.jsp?seqId=<%=seqId%>" ,useIframe:true} 
,{title:"按用户显示", useTextContent:false ,imgUrl:"<%=imgPath %>/node_dept.gif", contentUrl:"<%=contextPath %>/subsys/oa/confidentialFile/setConfidentialFile/showPriv/byUser.jsp?seqId=<%=seqId%>" ,useIframe:true} 
]; 
buildTab(jso, 'contentDiv', 800); 

} 
</script> 

</head> 
<body onload="doInit();"> 
<div id="contentDiv"></div> 


</body> 
</html>
