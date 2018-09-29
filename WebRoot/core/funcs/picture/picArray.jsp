<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %> 
<%
	String seqIdStr=request.getParameter("PIC_ID");
	String subDir=request.getParameter("SUB_DIR");
	String fileName=request.getParameter("FILE_NAME");
	String viewType=request.getParameter("VIEW_TYPE");
	String ascDesc=request.getParameter("ASC_DESC");
	
	int seqId=0;
	if(seqIdStr!=null){
	  seqId=Integer.parseInt(seqIdStr);
	}


%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href = "<%=cssPath%>/cmp/ExchangeSelect.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/DTree1.0.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript">
//alert("picArray.jsp?seqId>>"+'<%=seqIdStr%>');
//alert("seqId:"+'<%=seqId%>'+ " subDir:"+'<%=subDir%>'+ " fileName:"+'<%=fileName%>' + " viewType:"+'<%=viewType%>'+ " ascDesc:"+'<%=ascDesc%>');

var FILE_ATTR_ARRAY=new Array();

var requestURL="<%=contextPath%>/yh/core/funcs/picture/act/YHPictureAct";
var url=requestURL + "/showPicInfo.act?seqId=<%=seqId%>&fileDir=<%=subDir%>";
var json=getJsonRs(url);
//alert("rsText>>:"+rsText);
if(json.rtState == '1'){
	alert(json.rtMsrg);
	//return ;				
}
var prcsJson=json.rtData;
var count=prcsJson.length
if(count>0){
	//alert("count>>"+count);
	for(var i=0;i<count;i++){
		var prcs=prcsJson[i];
		if(i==0){
			FILE_ATTR_ARRAY.push( new Array(i,prcs.picName,prcs.fileTime,prcs.fileSize,prcs.imgWidth,prcs.imgHeight));
		}else{
			FILE_ATTR_ARRAY.push(new Array(i,prcs.picName,prcs.fileTime,prcs.fileSize,prcs.imgWidth,prcs.imgHeight));
		}
		
	}
}
</script>
</head>
<body>
</body>
</html>