<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %> 

<% 
	int seqId=(Integer)request.getAttribute("seqId");
	String subDir=(String)request.getAttribute("subDir");
	if(subDir==null){
		subDir="";
	}
	String ascDescFlag=(String)request.getAttribute("ascDescFlag");
	if(ascDescFlag==null){
		ascDescFlag="";
	}
	String field=(String)request.getAttribute("field");
	if(field==null){
		field="";
	}
	

	

%>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script type="text/javascript">
var subDirStr="<%=subDir %>";
var subDirStrEncode=encodeURIComponent(subDirStr);
function doInit(){
	//alert("subDirStrEncode>>"+subDirStrEncode + "   dir>>>"+'<%=subDir%>');
	location.href=contextPath + "/yh/core/funcs/picture/act/YHPictureAct/getPicInfoById.act?seqId=<%=seqId%>&ascDescFlag=<%=ascDescFlag%>&field=<%=field%>&subDir=" + subDirStrEncode ;
}

</script>
</head>
<body onload="doInit();">


</body>
</html>