<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %> 
<%
  String seqId=request.getParameter("seqId");
  
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>编辑文件夹</title>
<link rel="stylesheet"	href="<%=cssPath%>/style.css">
<script type="text/Javascript"	src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/smartclient.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/cmp/select.js"></script>

<script type="text/javascript">
var requestURL="<%=contextPath%>/yh/core/funcs/filefolder/act/YHFileSortAct";
var seqId='<%=seqId%>';	
function doInit(){
	document.form1.sortName.focus();
	var url=requestURL+"/getFileSortInfoById.act?seqId=" + seqId;
	var json=getJsonRs(url);
	if(json.rtState == '1'){
		 alert(json.rtMsrg); 
	}
	rtJson=json.rtData;
	if(rtJson.length>0){
		$('sortNo').value=rtJson[0].sortNo;
		$('sortName').value=rtJson[0].sortName;
	}
		
}

function CheckForm(){
  if(document.getElementById('sortName').value==""){
    alert("文件夹名称不能为空！");
    return (false);
  }
  return (true);
}
function sendForm(){
	var url=requestURL + "/updateFileSortInfoById.act?seqId="+seqId;
	if(CheckForm()){
		var rtJson = getJsonRs(url,mergeQueryString($("form1")));
  	if(rtJson.rtState == '0'){    
    	location.href="index.jsp";
    	//history.go('-1');
    }else{
  	 	  alert(rtJson.rtMsrg); 
  	}	
	}
	
}

</script>


</head>
<body class="" topmargin="5" onload="doInit();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=contextPath %>/core/funcs/filefolder/images/edit.gif" WIDTH="22" HEIGHT="20" align="middle"><span class="big3"> 编辑文件夹</span>
    </td>
  </tr>
</table>

<br>
<form action=""  method="post" name="form1" >
<input type="hidden" value="yh.core.funcs.filefolder.data.YHFileSort" name="dtoClass">
<table class="TableBlock" width="450" align="center">
  <tr>
    <td nowrap class="TableData"> 排序号：</td>
    <td class="TableData">
      <input type="text" name="sortNo" id="sortNo" size="20" maxlength="20" class="BigInput" value="">
     </td>
  </tr>
  <tr>
    <td nowrap class="TableData"> 文件夹名称：</td>
    <td class="TableData">
      <input type="text" name="sortName" id="sortName" size="25" maxlength="100" class="BigInput" value="">
    </td>
  </tr>
  <tr align="center" class="TableControl">
    <td colspan="2" nowrap>     
      <input type="button" value="确定" onclick="sendForm();" class="BigButton">&nbsp;&nbsp;
      <input type="button" value="返回" class="BigButton" onClick="javascript:location='<%=contextPath%>/core/funcs/filefolder/folder.jsp?seqId=<%=seqId%>'">
    </td>
  </tr>
</table>
</form>

</body>

</html>