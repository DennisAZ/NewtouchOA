<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %> 
<%
	String seqId=request.getParameter("checkId");
	String attachId=request.getParameter("attachId");
	String attachName=request.getParameter("attachName");
	String subject=request.getParameter("subject");
	String module=request.getParameter("module");
	if(module==null){
		module="";
	}
	
	if(seqId==null){
	  seqId="0";
	}
	if(subject==null){
	  subject="";
	}

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<title>文件转存</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type='text/javascript' src='<%=contextPath %>/subsys/inforesource/tree1/js/const.js'></script>
<script type="text/javascript">

function doInit(){
	var pars = $('form1').serialize()
	var url=requestURL + "/transferFolder.act";
	var rtJson=getJsonRs(url,pars);
	if(rtJson.rtState == '0'){
		var prcsJson=rtJson.rtData;
		var flag=prcsJson.flag;
		if(flag=="true"){
			$("listDiv").show();
		}else{
			$("failDiv").show();
		}
	  
  }else{
	 	  alert(rtJson.rtMsrg); 
  }

  
}


</script>
</head>
<body onload="doInit();" style="margin:0px;padding:0px">

<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/save_file.gif" align="middle"><span class="big3">个人文件柜—批量转存</span>
    </td>
  </tr>
</table>

<div id="listDiv" style="display: none" >

<table class="MessageBox" align="center" width="300" >
  <tr>
    <td class="msg blank">
      <h4 class="title">提示</h4>
      <div class="content" style="font-size:12pt">文件转存至个人文件柜成功</div>
    </td>
  </tr>
</table>
 
<div class="big1" align="center">
<input type="button"  value="关闭" class="SmallButtonW" onClick="window.close();">
</div>
</div>

<div id="failDiv" style="display: none">
<table class="MessageBox" align="center" width="300">
  <tr>
    <td class="msg blank">
      <h4 class="title">提示</h4>
      <div class="content" style="font-size:12pt">文件转存至个人文件柜失败，可能原文件不存在！</div>
    </td>
  </tr>
</table>
 
<div class="big1" align="center">
<input type="button"  value="关闭" class="SmallButtonW" onClick="window.close();">
</div>

</div>




<form name="form1" id="form1" action="" method="post">
	<input type="hidden" name="attachId" value="<%=attachId %>">
	<input type="hidden" name="attachName" value="<%=attachName %>">
	<input type="hidden" name="module" value="<%=module.trim() %>">
	<input type="hidden" name="subject" value="<%=subject %>">
	<input type="hidden" name="checkId" id="checkId" value="<%=seqId %>">
</form>
</body>
</html>



