<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %> 
<%
	String diskPath=request.getParameter("checkId");
	String attachId=request.getParameter("attachId");
	String attachName=request.getParameter("attachName");
	String subject=request.getParameter("subject");
	String module=request.getParameter("module");
	if(module==null){
		module="";
	}
	
	if(diskPath==null){
	  diskPath="";
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
  $("msgDiv").show();
	var url = requestURL + "/transferNetdisk.act";
	var pars = $('form1').serialize()
	var json=getJsonRs(url,pars);
	if(json.rtState == '1'){
		alert(json.rtMsrg);
		return ;				
	}
	var prcsJson=json.rtData;
	var saveOk=prcsJson.ok;
	var saveFail=prcsJson.fail;
	var count=prcsJson.fcount;
	var dirName = prcsJson.dirName;
  if(count == 0){
    $("msgDiv").show();
    $("msgBox").innerHTML="没有文件要转存到目录:【"+ dirName +"】";
  }else{
    $("msgDiv").show();
    var msg = "成功转存到目录:【"+ dirName +"】" + saveOk +"个文件<br>";
        msg +="失败转存到目录:【"+ dirName +"】" + saveFail +"个文件<br>";
        if(saveFail > 0){
          msg += "原因：文件已存在";
        }
    $("msgBox").innerHTML= msg;
  }
}

</script>
</head>
<body onload="doInit();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/save_file.gif" align="middle"><span class="big3">网络硬盘-批量转存</span>
    </td>
    </tr>
</table>
<div id="msgDiv" style="display: none">
	<table class="MessageBox" align="center" width="430">
	  <tr>
	    <td class="msg blank">
	      <div class="content" id="msgBox" style="font-size:12pt">
	                      正在转存文件, 请稍后.....
	      </div>
	    </td>
	  </tr>
	</table>
	<br>
	<div align="center">
	  <input type="button"  value="上一步" class="SmallButtonW" onClick="history.back();">&nbsp;&nbsp;
	  <input type="button"  value="关闭" class="SmallButtonW" onClick="window.close();">
	</div>
</div>


<form name="form1" id="form1" action="" method="post">
	<input type="hidden" name="attachId" value="<%=attachId %>">
	<input type="hidden" name="attachName" value="<%=attachName %>">
	<input type="hidden" name="module" value="<%=module.trim() %>">
	<input type="hidden" name="subject" value="<%=subject %>">
	<input type="hidden" name="diskPath" id="checkId" value="<%=diskPath %>">
</form>
</body>
</html>