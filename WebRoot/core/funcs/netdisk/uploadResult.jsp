<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %> 

<% 
	String diskPath=(String)request.getAttribute("diskPath");
	if(diskPath==null){
		diskPath="";
	}
	String seqIdStr=(String)request.getAttribute("seqId");
	if(seqIdStr==null){
		seqIdStr="0";
	}


%>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script type="text/javascript">
var dirPathStr="<%=diskPath %>";
var dirPathStrEncode=encodeURIComponent(dirPathStr);
function doInit(){
	location.href=contextPath + "/core/funcs/netdisk/fileList.jsp?seqId=<%=seqIdStr%>&DISK_ID=" + dirPathStrEncode ;
}

</script>
</head>
<body onload="doInit();">


</body>
</html>