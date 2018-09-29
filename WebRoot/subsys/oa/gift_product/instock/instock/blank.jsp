<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %> 
<%
  String type = request.getParameter("type");
  String seqId = request.getParameter("seqId");
  if(type==null){
    type="";
  }
  if(seqId == null){
    seqId = "";
  }
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>礼品入库管理</title>
<link rel="stylesheet"	href="<%=cssPath%>/style.css">
<link rel="stylesheet" href = "<%=cssPath %>/style.css">
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath %>/cmp/ExchangeSelect.css" type="text/css" />
<script type="text/Javascript"	src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/Javascript"  src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/smartclient.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/cmp/select.js"></script>
<script type="text/javascript">
function doOnload(){
  var type = '<%=type%>';
  var seqId = '<%=seqId%>';
  if(seqId!=''){
    window.location.href = "<%=contextPath%>/subsys/oa/gift_product/instock/instock/newgiftinstock2.jsp?seqId="+seqId;
  }else if(type=='1'){
    window.location.href = "<%=contextPath%>/subsys/oa/gift_product/instock/codeitem/newcodeItem.jsp";
  }else if(type=='2'){
    window.location.href = "<%=contextPath%>/subsys/oa/gift_product/instock/queryinstosk/queryinstock.jsp";
  }else{
    window.location.href = "<%=contextPath%>/subsys/oa/gift_product/instock/instock/newgiftinstock.jsp";
  }
}
</script>
</head>
<body onload="doOnload();">

</body>
</html>