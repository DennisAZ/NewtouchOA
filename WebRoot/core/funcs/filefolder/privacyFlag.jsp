<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %> 
<%
	String seqId = request.getParameter("seqId");
	if(seqId == null){
		seqId = "0";
	}

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<link rel="stylesheet"	href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<script type="text/Javascript"	src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/Javascript"  src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/smartclient.js"></script>
<script type="text/javascript">;
function doInit(){
	var requestURL = "<%=contextPath%>/yh/core/funcs/system/filefolder/act/YHFileSortAct/getPrivteById.act?seqId=<%=seqId%>";
	var json = getJsonRs(requestURL);
	//alert(rsText);
	if(json.rtState == "0" ){
		var prcsJson = json.rtData;
		var prcs = prcsJson[0];
		var visiPriv = prcs.visiPriv;
		var ownerPriv = prcs.ownerPriv;
		if(visiPriv == "1" || ownerPriv == "1"){
			location.href = "folder.jsp?seqId=<%=seqId%>";
		}else{
			$("noPriv").show();
		}
	}else{
		alert(json.rtMsrg);
		return;
	}
}

</script>
</head>
<body onload="doInit();">
<div id="noPriv" style="display: none">
<table class="MessageBox" align="center" width="340">
  <tr>
    <td class="msg error">
      <h4 class="title">错误</h4>
      <div class="content" style="font-size:12pt">您没有权限访问该目录</div>
    </td>
  </tr>
</table>
</div>
</body>
</html>